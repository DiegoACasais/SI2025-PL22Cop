package app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import app.dto.EntregarReportajeDTO;
import app.model.EntregarReportajeModel;
import app.view.EntregarReportajeView;
import giis.demo.util.SwingUtil;

public class EntregarReportajeController {

    private static final String FILTRO_PENDIENTES = "Pendientes de entrega";

    private EntregarReportajeModel model;
    private EntregarReportajeView view;
    private String nombreReportero;
    private List<EntregarReportajeDTO> versionesCargadas;

    public EntregarReportajeController(EntregarReportajeModel model, EntregarReportajeView view, String nombreReportero) {
        this.model = model;
        this.view = view;
        this.nombreReportero = nombreReportero;
        this.versionesCargadas = new ArrayList<>();
        initView();
        initController();
    }

    private void initView() {
        recargarEventosSegunFiltro();
        view.setVisible(true);
    }

    private void initController() {
        view.addFiltroListener(e -> recargarEventosSegunFiltro());
        view.addEventoListener(e -> cargarContenidoEventoSeleccionado());
        view.addVersionListener(e -> cargarVersionSeleccionada());
        view.addAceptarListener(e -> aceptarCambios());
        view.addRestaurarVersionListener(e -> restaurarVersionSeleccionada());
    }

    private boolean isModoPendientes() {
        Object item = view.getComboFiltro().getSelectedItem();
        return FILTRO_PENDIENTES.equals(item);
    }

    private void recargarEventosSegunFiltro() {
        view.getComboEventos().removeAllItems();

        List<EntregarReportajeDTO> eventos;
        if (isModoPendientes()) {
            eventos = model.getEventosPendientes(nombreReportero);
            view.setTituloEditable(true);
            view.setContenidoEditable(true);
            view.setTextoBotonAceptar("Entregar");
            view.setMensajePermisos("");
            view.setSeccionVersionesVisible(false);
            view.limpiarCampos();
        } else {
            eventos = model.getEventosEntregados(nombreReportero);
            view.setTituloEditable(false);
            view.setTextoBotonAceptar("Guardar cambios");
        }

        for (EntregarReportajeDTO evento : eventos) {
            view.getComboEventos().addItem(evento);
        }

        cargarContenidoEventoSeleccionado();
    }

    private void cargarContenidoEventoSeleccionado() {
        EntregarReportajeDTO seleccionado = (EntregarReportajeDTO) view.getComboEventos().getSelectedItem();

        if (seleccionado == null) {
            view.limpiarCampos();
            view.limpiarVersionSeleccionada();
            view.getComboVersiones().removeAllItems();
            view.setTituloEditable(isModoPendientes());
            view.setContenidoEditable(isModoPendientes());
            view.setSeccionVersionesVisible(false);
            versionesCargadas = new ArrayList<>();
            return;
        }

        if (isModoPendientes()) {
            view.limpiarCampos();
            view.limpiarVersionSeleccionada();
            view.getComboVersiones().removeAllItems();
            view.setTituloEditable(true);
            view.setContenidoEditable(true);
            view.setMensajePermisos("");
            view.setSeccionVersionesVisible(false);
            versionesCargadas = new ArrayList<>();
            return;
        }

        EntregarReportajeDTO reportaje = model.getReportajePorEvento(seleccionado.getId_evento(), nombreReportero);
        if (reportaje == null) {
            view.limpiarCampos();
            view.limpiarVersionSeleccionada();
            view.getComboVersiones().removeAllItems();
            view.setContenidoEditable(false);
            view.setSeccionVersionesVisible(false);
            view.setMensajePermisos("No se encontró contenido para el evento seleccionado.");
            return;
        }

        view.setTitulo(reportaje.getTitulo());
        view.setSubtitulo(reportaje.getSubtitulo());
        view.setCuerpo(reportaje.getCuerpo());
        view.setTituloEditable(false);

        boolean editable = reportaje.isEditablePorReportero();
        view.setContenidoEditable(editable);
        view.setSeccionVersionesVisible(editable);
        if (editable) {
            view.setMensajePermisos("Puedes modificar subtítulo/cuerpo o restaurar una versión previa.");
        } else {
            view.setMensajePermisos("Solo el reportero que entregó el reportaje puede modificar o restaurar.");
        }

        cargarVersiones(reportaje.getId_reportaje());
    }

    private void cargarVersiones(int idReportaje) {
        versionesCargadas = model.getVersionesReportaje(idReportaje);
        view.getComboVersiones().removeAllItems();

        for (EntregarReportajeDTO version : versionesCargadas) {
            String etiqueta = version.getFecha_cambio() + " " + version.getHora_cambio() + " - "
                    + version.getResumenCambios();
            view.getComboVersiones().addItem(etiqueta);
        }

        cargarVersionSeleccionada();
    }

    private void cargarVersionSeleccionada() {
        if (versionesCargadas == null || versionesCargadas.isEmpty()) {
            view.limpiarVersionSeleccionada();
            return;
        }

        int idx = view.getIndiceVersionSeleccionada();
        if (idx < 0 || idx >= versionesCargadas.size()) {
            view.limpiarVersionSeleccionada();
            return;
        }

        EntregarReportajeDTO version = versionesCargadas.get(idx);
        String subtitulo = version.getSubtitulo_guardado() == null ? "(sin cambio en esta versión)"
                : version.getSubtitulo_guardado();
        String cuerpo = version.getCuerpo_guardado() == null ? "(sin cambio en esta versión)"
                : version.getCuerpo_guardado();

        view.setSubtituloVersion(subtitulo);
        view.setCuerpoVersion(cuerpo);
    }

    private void aceptarCambios() {
        if (isModoPendientes()) {
            entregarNuevoReportaje();
        } else {
            modificarReportajeExistente();
        }
    }

    private void entregarNuevoReportaje() {
        EntregarReportajeDTO eventoSeleccionado = (EntregarReportajeDTO) view.getComboEventos().getSelectedItem();
        if (eventoSeleccionado == null) {
            SwingUtil.showMessage("No tienes eventos pendientes de reportaje.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String titulo = view.getTitulo().trim();
        String subtitulo = view.getSubtitulo().trim();
        String cuerpo = view.getCuerpo().trim();

        if (titulo.isEmpty() || subtitulo.isEmpty() || cuerpo.isEmpty()) {
            SwingUtil.showMessage("Título, subtítulo y cuerpo son obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (model.existeTitulo(titulo)) {
            SwingUtil.showMessage("Ya existe un reportaje con ese título.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        model.insertarReportaje(eventoSeleccionado.getId_evento(), nombreReportero, titulo, subtitulo, cuerpo);
        SwingUtil.showMessage("Reportaje entregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        recargarEventosSegunFiltro();
    }

    private void modificarReportajeExistente() {
        EntregarReportajeDTO eventoSeleccionado = (EntregarReportajeDTO) view.getComboEventos().getSelectedItem();
        if (eventoSeleccionado == null) {
            SwingUtil.showMessage("No hay eventos entregados para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        EntregarReportajeDTO actual = model.getReportajePorEvento(eventoSeleccionado.getId_evento(), nombreReportero);
        if (actual == null) {
            SwingUtil.showMessage("No se encontró reportaje para el evento seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!actual.isEditablePorReportero()) {
            SwingUtil.showMessage("Solo el reportero que hizo la entrega puede modificar este reportaje.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nuevoSubtitulo = view.getSubtitulo().trim();
        String nuevoCuerpo = view.getCuerpo().trim();

        if (nuevoSubtitulo.isEmpty() || nuevoCuerpo.isEmpty()) {
            SwingUtil.showMessage("Subtítulo y cuerpo son obligatorios para modificar.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        registrarActualizacion(actual, nuevoSubtitulo, nuevoCuerpo, "Cambios guardados y versión registrada correctamente.");
    }

    private void restaurarVersionSeleccionada() {
        EntregarReportajeDTO eventoSeleccionado = (EntregarReportajeDTO) view.getComboEventos().getSelectedItem();
        if (eventoSeleccionado == null || isModoPendientes()) {
            SwingUtil.showMessage("Debes seleccionar un evento entregado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        EntregarReportajeDTO actual = model.getReportajePorEvento(eventoSeleccionado.getId_evento(), nombreReportero);
        if (actual == null || !actual.isEditablePorReportero()) {
            SwingUtil.showMessage("No tienes permisos para restaurar este reportaje.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (versionesCargadas == null || versionesCargadas.isEmpty()) {
            SwingUtil.showMessage("No hay versiones para restaurar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idx = view.getIndiceVersionSeleccionada();
        if (idx < 0 || idx >= versionesCargadas.size()) {
            SwingUtil.showMessage("Debes seleccionar una versión.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        EntregarReportajeDTO version = versionesCargadas.get(idx);
        String nuevoSubtitulo = version.getSubtitulo_guardado() == null ? actual.getSubtitulo() : version.getSubtitulo_guardado();
        String nuevoCuerpo = version.getCuerpo_guardado() == null ? actual.getCuerpo() : version.getCuerpo_guardado();

        registrarActualizacion(actual, nuevoSubtitulo, nuevoCuerpo,
                "Versión restaurada y nueva versión registrada correctamente.");
    }

    private void registrarActualizacion(EntregarReportajeDTO actual, String nuevoSubtitulo, String nuevoCuerpo,
            String mensajeExito) {
        boolean cambiaSubtitulo = !nuevoSubtitulo.equals(actual.getSubtitulo());
        boolean cambiaCuerpo = !nuevoCuerpo.equals(actual.getCuerpo());

        if (!cambiaSubtitulo && !cambiaCuerpo) {
            SwingUtil.showMessage("No hay cambios para guardar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        model.actualizarReportaje(actual.getId_reportaje(), nuevoSubtitulo, nuevoCuerpo);
        model.insertarVersion(actual.getId_reportaje(), cambiaSubtitulo ? nuevoSubtitulo : null,
                cambiaCuerpo ? nuevoCuerpo : null);

        SwingUtil.showMessage(mensajeExito, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        recargarEventosSegunFiltro();
    }
}
