package app.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Font;
import java.awt.Color;

public class OfrecerReportajesView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	// Componentes para el controlador
	public JTable tableEventos;
	public JTable tableEmpresas;
	public JTable tableOfertasEnCurso;
	
	// Filtro de la HU #33531
	public JComboBox<String> comboFiltroEmpresas;
	
	// Botones
	public JButton btnOfertar;
	public JButton btnQuitarOfrecimiento; 
	public JButton btnCancelar;
	public JButton btnAceptarTodo;
	public JButton btnLimpiarSeleccion;
	public JLabel lblAgenciaSeleccionada;

	public OfrecerReportajesView() {
		setTitle("Gestión y Modificación de Ofrecimientos de Reportajes");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1050, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// --- CABECERA ---
		lblAgenciaSeleccionada = new JLabel("Agencia: -");
		lblAgenciaSeleccionada.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblAgenciaSeleccionada.setBounds(20, 5, 400, 20);
		contentPane.add(lblAgenciaSeleccionada);

		// --- COLUMNA 1: EVENTOS ---
		JLabel lblEventos = new JLabel("EVENTOS CON REPORTEROS ASIGNADOS:");
		lblEventos.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEventos.setBounds(20, 30, 300, 14);
		contentPane.add(lblEventos);

		JScrollPane scrollEventos = new JScrollPane();
		scrollEventos.setBounds(20, 55, 300, 480);
		contentPane.add(scrollEventos);
		
		tableEventos = new JTable();
		scrollEventos.setViewportView(tableEventos);

		// --- COLUMNA 2: GESTIÓN DE EMPRESAS (OFRECER) ---
		JLabel lblEmpresas = new JLabel("GESTIÓN DE EMPRESAS COMUNICACIÓN:");
		lblEmpresas.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEmpresas.setBounds(340, 30, 300, 14);
		contentPane.add(lblEmpresas);

		comboFiltroEmpresas = new JComboBox<String>();
		comboFiltroEmpresas.setModel(new DefaultComboBoxModel<String>(new String[] {
			"Ver empresas SIN OFERTA", 
			"Ver empresas YA OFERTADAS"
		}));
		comboFiltroEmpresas.setBounds(340, 55, 330, 25);
		contentPane.add(comboFiltroEmpresas);

		JScrollPane scrollEmpresas = new JScrollPane();
		scrollEmpresas.setBounds(340, 90, 330, 350);
		contentPane.add(scrollEmpresas);
		
		tableEmpresas = new JTable();
		scrollEmpresas.setViewportView(tableEmpresas);

		btnOfertar = new JButton("OFERTAR");
		btnOfertar.setBounds(340, 450, 330, 35);
		contentPane.add(btnOfertar);

		btnCancelar = new JButton("CANCELAR");
		btnCancelar.setBounds(340, 500, 330, 35);
		contentPane.add(btnCancelar);

		// --- COLUMNA 3: OFERTAS EN CURSO (MODIFICAR) ---
		JLabel lblOfertas = new JLabel("OFERTAS EN CURSO (Estado):");
		lblOfertas.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblOfertas.setBounds(690, 30, 300, 14);
		contentPane.add(lblOfertas);

		JScrollPane scrollOfertas = new JScrollPane();
		scrollOfertas.setBounds(690, 55, 320, 335); // Ajustado para dejar espacio al botón
		contentPane.add(scrollOfertas);
		
		tableOfertasEnCurso = new JTable();
		scrollOfertas.setViewportView(tableOfertasEnCurso);

		// REUBICACIÓN SOLICITADA: Botón Quitar bajo la lista de ofertas
		btnQuitarOfrecimiento = new JButton("QUITAR OFRECIMIENTO");
		btnQuitarOfrecimiento.setForeground(new Color(200, 0, 0)); // Rojo oscuro para visibilidad
		btnQuitarOfrecimiento.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnQuitarOfrecimiento.setBounds(690, 400, 320, 35); 
		contentPane.add(btnQuitarOfrecimiento);

		btnAceptarTodo = new JButton("ACEPTAR TODO");
		btnAceptarTodo.setBounds(690, 450, 320, 35);
		contentPane.add(btnAceptarTodo);

		btnLimpiarSeleccion = new JButton("LIMPIAR TABLAS");
		btnLimpiarSeleccion.setBounds(690, 500, 320, 35);
		contentPane.add(btnLimpiarSeleccion);
	}
}