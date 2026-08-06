package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PedidoDAO {

    private final MesaDAO mesaDAO = new MesaDAO();

    public int abrirPedido(int idMesa) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion()) {
            con.setAutoCommit(false);
            try {
                boolean ocupada = mesaDAO.ocupar(con, idMesa);
                if (!ocupada) {
                    con.rollback();
                    return -1;
                }
                int idPedido;
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO PEDIDOS (IdMesa) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, idMesa);
                    ps.executeUpdate();
                    try (ResultSet generadas = ps.getGeneratedKeys()) {
                        generadas.next();
                        idPedido = generadas.getInt(1);
                    }
                }
                con.commit();
                return idPedido;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public void agregarPlato(int idPedido, int idPlato, int cantidad) throws SQLException {
        String sql = "INSERT INTO DETALLE_PEDIDO (IdPedido, IdPlato, Cantidad) VALUES (?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPedido);
            ps.setInt(2, idPlato);
            ps.setInt(3, cantidad);
            ps.executeUpdate();
        }
    }

    public void cerrarCuenta(int idPedido, int idMesa) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE PEDIDOS SET Estado = 'CERRADO' WHERE IdPedido = ?")) {
                    ps.setInt(1, idPedido);
                    ps.executeUpdate();
                }
                mesaDAO.liberar(con, idMesa);
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public void mostrarCuenta(int idPedido) throws SQLException {
        String sql = "SELECT p.Nombre, d.Cantidad, p.Precio FROM DETALLE_PEDIDO d "
                + "INNER JOIN PLATOS p ON d.IdPlato = p.IdPlato WHERE d.IdPedido = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                double total = 0;
                while (rs.next()) {
                    double subtotal = rs.getInt("Cantidad") * rs.getDouble("Precio");
                    total += subtotal;
                    System.out.printf("%-25s x%d = $%.2f%n", rs.getString("Nombre"), rs.getInt("Cantidad"), subtotal);
                }
                System.out.printf("TOTAL: $%.2f%n", total);
            }
        }
    }
}
