import java.sql.*;
public class CheckTarifa {
  public static void main(String[] args) throws Exception {
    Class.forName("org.sqlite.JDBC");
    try (Connection c = DriverManager.getConnection("jdbc:sqlite:C:/Users/diego/git/SI2025-PL22Cop/DemoDB.db")) {
      String q1 = "SELECT ec.id_empresa, ec.nombre, COALESCE(aet.tarifa_plana,0) tarifa FROM Empresa_Comunicacion ec LEFT JOIN Agencia ag ON ag.nombre=? LEFT JOIN Agencia_Empresa_Tarifa aet ON aet.id_empresa=ec.id_empresa AND aet.id_agencia=ag.id_agencia WHERE ec.id_empresa NOT IN (SELECT id_empresa FROM Ofrecimiento WHERE id_evento=?) ORDER BY ec.nombre";
      String q2 = q1.replace("ORDER BY ec.nombre", "AND COALESCE(aet.tarifa_plana,0) > 0 ORDER BY ec.nombre");
      try (PreparedStatement ps = c.prepareStatement(q1)) {
        ps.setString(1, "Agencia Central de Noticias");
        ps.setInt(2, 1);
        try (ResultSet rs = ps.executeQuery()) {
          System.out.println("SIN FILTRO");
          while (rs.next()) System.out.println(rs.getInt(1)+"|"+rs.getString(2)+"|"+rs.getDouble(3));
        }
      }
      try (PreparedStatement ps = c.prepareStatement(q2)) {
        ps.setString(1, "Agencia Central de Noticias");
        ps.setInt(2, 1);
        try (ResultSet rs = ps.executeQuery()) {
          System.out.println("CON TARIFA");
          while (rs.next()) System.out.println(rs.getInt(1)+"|"+rs.getString(2)+"|"+rs.getDouble(3));
        }
      }
    }
  }
}
