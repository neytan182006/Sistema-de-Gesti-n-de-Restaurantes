package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MesaDAO {

    public void listar() throws SQLException {
        String sql = "SELECT IdMesa, Numero, Capacidad, Estado FROM MESAS ORDER BY Numero";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                System.out.printf("[%d] Mesa %d (capacidad %d) - %s%n",
                        rs.getInt("IdMesa"), rs.getInt("Numero"), rs.getInt("Capacidad"), rs.getString("Estado"));
            }
        }
    }

    public boolean ocupar(Connection con, int idMesa) throws SQLException {
        String sql = "UPDATE MESAS SET Estado = 'OCUPADA' WHERE IdMesa = ? AND Estado = 'LIBRE'";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMesa);
            return ps.executeUpdate() > 0;
        }
    }

    public void liberar(Connection con, int idMesa) throws SQLException {
        String sql = "UPDATE MESAS SET Estado = 'LIBRE' WHERE IdMesa = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMesa);
            ps.executeUpdate();
        }
    }
}
