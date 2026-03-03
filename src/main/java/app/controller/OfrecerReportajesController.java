package app.controller;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import app.model.OfrecerReportajesModel;
import app.view.OfrecerReportajesView;
import app.dto.OfrecerReportajesDTO;
import giis.demo.util.SwingUtil;

public class OfrecerReportajesController {

	private OfrecerReportajesModel model;
	private OfrecerReportajesView view;
	private String agenciaActual;
	
	// Contador de cambios realizados en la sesión actual
	private int operacionesRealizadas = 0;

	public OfrecerReportajesController(OfrecerReportajesModel m, OfrecerReportajesView v, String agencia) {
		this.model = m;
		this.view = v;
		this.agenciaActual = agencia;
		this.initView();
	}

	public void initView() {
		this.view.lblAgenciaSeleccionada.setText("Agencia: " + agenciaActual);
		cargarEventos();

		// Actualizar tablas al seleccionar un evento
		view.tableEventos.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				actualizarTablasDependientes();
			}
		});

		// Filtro de empresas (Sin oferta / Ya ofertadas)
		view.comboFiltroEmpresas.addActionListener(e -> {
			int fila = view.tableEventos.getSelectedRow();
			if (fila != -1) {
				cargarEmpresasSegunFiltro((int) view.tableEventos.getValueAt(fila, 0));
			}
		});

		// --- ACCIONES DE BOTONES ---
		
		view.btnOfertar.addActionListener(e -> ejecutarOferta());
		
		view.btnQuitarOfrecimiento.addActionListener(e -> ejecutarQuitarOfrecimiento());

		// Botón ACEPTAR TODO: Muestra el resumen de cambios de la sesión
		view.btnAceptarTodo.addActionListener(e -> {
			if (operacionesRealizadas == 0) {
				SwingUtil.showMessage("No se han realizado cambios en esta sesión.", "Aviso", 1);
			} else {
				String mensaje = "Sesión finalizada con éxito.\nSe han registrado " + operacionesRealizadas + " cambios en la base de datos.";
				SwingUtil.showMessage(mensaje, "Éxito", 1);
				operacionesRealizadas = 0; 
			}
		});

		view.btnCancelar.addActionListener(e -> view.dispose());
		
		view.btnLimpiarSeleccion.addActionListener(e -> {
			view.tableEventos.clearSelection();
			limpiarTablas();
			operacionesRealizadas = 0;
		});

		view.setVisible(true);
	}

	private void actualizarTablasDependientes() {
		int fila = view.tableEventos.getSelectedRow();
		if (fila != -1) {
			int idEvento = (int) view.tableEventos.getValueAt(fila, 0);
			cargarEmpresasSegunFiltro(idEvento);
			cargarOfertasEnCurso(idEvento);
		} else {
			limpiarTablas();
		}
	}

	private void ejecutarQuitarOfrecimiento() {
		int filaEvento = view.tableEventos.getSelectedRow();
		int filaOferta = view.tableOfertasEnCurso.getSelectedRow();
		
		if (filaOferta == -1) {
			SwingUtil.showMessage("Seleccione una empresa de 'OFERTAS EN CURSO' para quitarla.", "Aviso", 1);
			return;
		}

		int idEvento = (int) view.tableEventos.getValueAt(filaEvento, 0);
		int idEmpresa = (int) view.tableOfertasEnCurso.getValueAt(filaOferta, 0);

		OfrecerReportajesDTO detalle = model.getDetalleOfrecimiento(idEvento, idEmpresa);

		// Restricción: No se puede quitar si tiene acceso (1)
		if (detalle.getTiene_acceso() == 1) {
			SwingUtil.showMessage("No se puede quitar: Acceso ya concedido.", "Error", 0);
			return;
		}

		// Notificación básica si estaba ACEPTADO
		if ("ACEPTADO".equals(detalle.getEstado())) {
			SwingUtil.showMessage("Se ha notificado por email a " + detalle.getEmail() + " la cancelación.", "Email enviado", 1);
		}

		model.eliminarOfrecimiento(idEvento, idEmpresa);
		operacionesRealizadas++; // Incrementamos contador
		actualizarTablasDependientes();
	}

	private void ejecutarOferta() {
		int filaEvento = view.tableEventos.getSelectedRow();
		if (filaEvento == -1) return;
		
		int idEvento = (int) view.tableEventos.getValueAt(filaEvento, 0);
		boolean cambios = false;

		for (int r = 0; r < view.tableEmpresas.getRowCount(); r++) {
			Boolean check = (Boolean) view.tableEmpresas.getValueAt(r, 2);
			if (check != null && check) {
				model.insertarOfrecimientos(idEvento, (int) view.tableEmpresas.getValueAt(r, 0));
				operacionesRealizadas++; // Incrementamos contador
				cambios = true;
			}
		}
		
		if (cambios) {
			actualizarTablasDependientes();
		} else {
			SwingUtil.showMessage("No hay empresas seleccionadas.", "Aviso", 1);
		}
	}

	private void cargarEmpresasSegunFiltro(int idEvento) {
		int filtro = view.comboFiltroEmpresas.getSelectedIndex();
		if (filtro == 0) { // Empresas sin oferta
			configurarTablaConChecks(model.getEmpresasSinOferta(idEvento));
			view.btnOfertar.setEnabled(true);
		} else { // Empresas ya ofertadas
			String[] cols = {"id_empresa", "nombre_empresa", "estado"};
			view.tableEmpresas.setModel(SwingUtil.getTableModelFromPojos(model.getEmpresasConOferta(idEvento), cols));
			view.btnOfertar.setEnabled(false);
		}
		SwingUtil.autoAdjustColumns(view.tableEmpresas);
	}

	private void cargarOfertasEnCurso(int idEvento) {
		List<OfrecerReportajesDTO> ofertas = model.getEmpresasConOferta(idEvento);
		String[] columnas = {"id_empresa", "nombre_empresa", "estado"};
		view.tableOfertasEnCurso.setModel(SwingUtil.getTableModelFromPojos(ofertas, columnas));
		SwingUtil.autoAdjustColumns(view.tableOfertasEnCurso);
	}

	private void cargarEventos() {
		List<OfrecerReportajesDTO> eventos = model.getEventosConReportero(agenciaActual);
		String[] columnas = {"id_evento", "nombre_evento", "reportero_asignado"};
		view.tableEventos.setModel(SwingUtil.getTableModelFromPojos(eventos, columnas));
		SwingUtil.autoAdjustColumns(view.tableEventos);
	}

	private void configurarTablaConChecks(List<OfrecerReportajesDTO> empresas) {
		String[] columnas = {"id_empresa", "nombre_empresa"};
		DefaultTableModel tm = (DefaultTableModel) SwingUtil.getTableModelFromPojos(empresas, columnas);
		tm.addColumn("Seleccionar");
		view.tableEmpresas.setModel(new DefaultTableModel() {
			private static final long serialVersionUID = 1L;
			@Override public int getRowCount() { return tm.getRowCount(); }
			@Override public int getColumnCount() { return tm.getColumnCount(); }
			@Override public Object getValueAt(int r, int c) { return tm.getValueAt(r, c); }
			@Override public void setValueAt(Object v, int r, int c) { tm.setValueAt(v, r, c); fireTableCellUpdated(r, c); }
			@Override public Class<?> getColumnClass(int c) { return c == 2 ? Boolean.class : Object.class; }
			@Override public boolean isCellEditable(int r, int c) { return c == 2; }
			@Override public String getColumnName(int c) { return tm.getColumnName(c); }
		});
	}

	private void limpiarTablas() {
		view.tableEmpresas.setModel(new DefaultTableModel());
		view.tableOfertasEnCurso.setModel(new DefaultTableModel());
	}
}