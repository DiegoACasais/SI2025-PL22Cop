package app.model;

import java.util.List;

import app.dto.EntregarReportajeDTO;
import giis.demo.util.Database;

public class EntregarReportajeModel {

    private Database db = new Database();

    public List<EntregarReportajeDTO> getEventosPendientes(String nombreReportero) {
        String sql = "SELECT e.id_evento, e.descripcion "
                + "FROM Evento e "
                + "JOIN Asignacion a ON e.id_evento = a.id_evento "
                + "JOIN Reportero r ON a.id_reportero = r.id_reportero "
                + "LEFT JOIN Reportaje rep ON rep.id_evento = e.id_evento "
                + "WHERE r.nombre = ? AND rep.id_reportaje IS NULL "
                + "ORDER BY e.fecha";
        return db.executeQueryPojo(EntregarReportajeDTO.class, sql, nombreReportero);
    }

    public List<EntregarReportajeDTO> getEventosEntregados(String nombreReportero) {
        String sql = "SELECT e.id_evento, e.descripcion, rep.id_reportaje, rep.id_reportero AS id_reportero_entrega, "
                + "autor.nombre AS nombre_reportero_entrega, rep.titulo, rep.subtitulo, rep.cuerpo, "
                + "CASE WHEN autor.nombre = ? THEN 1 ELSE 0 END AS editable "
                + "FROM Evento e "
                + "JOIN Asignacion a ON e.id_evento = a.id_evento "
                + "JOIN Reportero r ON a.id_reportero = r.id_reportero "
                + "JOIN Reportaje rep ON rep.id_evento = e.id_evento "
                + "LEFT JOIN Reportero autor ON autor.id_reportero = rep.id_reportero "
                + "WHERE r.nombre = ? "
                + "ORDER BY e.fecha";
        return db.executeQueryPojo(EntregarReportajeDTO.class, sql, nombreReportero, nombreReportero);
    }

    public EntregarReportajeDTO getReportajePorEvento(int idEvento, String nombreReportero) {
        String sql = "SELECT e.id_evento, e.descripcion, rep.id_reportaje, rep.id_reportero AS id_reportero_entrega, "
                + "autor.nombre AS nombre_reportero_entrega, rep.titulo, rep.subtitulo, rep.cuerpo, "
                + "CASE WHEN autor.nombre = ? THEN 1 ELSE 0 END AS editable "
                + "FROM Evento e "
                + "JOIN Asignacion a ON e.id_evento = a.id_evento "
                + "JOIN Reportero r ON a.id_reportero = r.id_reportero "
                + "JOIN Reportaje rep ON rep.id_evento = e.id_evento "
                + "LEFT JOIN Reportero autor ON autor.id_reportero = rep.id_reportero "
                + "WHERE e.id_evento = ? AND r.nombre = ?";
        List<EntregarReportajeDTO> rows = db.executeQueryPojo(EntregarReportajeDTO.class, sql, nombreReportero, idEvento,
                nombreReportero);
        return rows.isEmpty() ? null : rows.get(0);
    }

    public List<EntregarReportajeDTO> getVersionesReportaje(int idReportaje) {
        String sql = "SELECT id_version, fecha_cambio, hora_cambio, subtitulo_guardado, cuerpo_guardado "
                + "FROM Version_Reportaje WHERE id_reportaje = ? ORDER BY fecha_cambio DESC, hora_cambio DESC, id_version DESC";
        return db.executeQueryPojo(EntregarReportajeDTO.class, sql, idReportaje);
    }

    public boolean existeTitulo(String titulo) {
        String sql = "SELECT id_reportaje FROM Reportaje WHERE UPPER(titulo) = UPPER(?)";
        List<EntregarReportajeDTO> rows = db.executeQueryPojo(EntregarReportajeDTO.class, sql, titulo);
        return !rows.isEmpty();
    }

    public void insertarReportaje(int idEvento, String nombreReportero, String titulo, String subtitulo, String cuerpo) {
        String sql = "INSERT INTO Reportaje (id_evento, id_reportero, titulo, subtitulo, cuerpo, fecha_entrega) "
                + "VALUES (?, (SELECT id_reportero FROM Reportero WHERE nombre = ?), ?, ?, ?, datetime('now'))";
        db.executeUpdate(sql, idEvento, nombreReportero, titulo, subtitulo, cuerpo);
    }

    public void actualizarReportaje(int idReportaje, String subtitulo, String cuerpo) {
        String sql = "UPDATE Reportaje SET subtitulo = ?, cuerpo = ? WHERE id_reportaje = ?";
        db.executeUpdate(sql, subtitulo, cuerpo, idReportaje);
    }

    public void insertarVersion(int idReportaje, String subtituloGuardado, String cuerpoGuardado) {
        String sql = "INSERT INTO Version_Reportaje (id_reportaje, fecha_cambio, hora_cambio, subtitulo_guardado, cuerpo_guardado) "
                + "VALUES (?, date('now'), time('now'), ?, ?)";
        db.executeUpdate(sql, idReportaje, subtituloGuardado, cuerpoGuardado);
    }
}
