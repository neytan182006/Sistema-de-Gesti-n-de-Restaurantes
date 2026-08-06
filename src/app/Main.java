package app;

import dao.MesaDAO;
import dao.PedidoDAO;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    private static final Scanner TECLADO = new Scanner(System.in);
    private static final MesaDAO mesaDAO = new MesaDAO();
    private static final PedidoDAO pedidoDAO = new PedidoDAO();

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");

            try {
                switch (opcion) {
                    case 1 -> mesaDAO.listar();
                    case 2 -> abrirPedido();
                    case 3 -> agregarPlato();
                    case 4 -> verCuenta();
                    case 5 -> cerrarCuenta();
                    case 0 -> System.out.println("Hasta luego.");
                    default -> System.out.println("Opcion invalida.");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println("\n=== GESTION DE RESTAURANTE ===");
        System.out.println("1. Listar mesas");
        System.out.println("2. Abrir pedido en una mesa");
        System.out.println("3. Agregar plato a un pedido");
        System.out.println("4. Ver cuenta de un pedido");
        System.out.println("5. Cerrar cuenta (libera la mesa)");
        System.out.println("0. Salir");
    }

    private static void abrirPedido() throws SQLException {
        int idMesa = leerEntero("Id de la mesa (ver opcion 1): ");
        int idPedido = pedidoDAO.abrirPedido(idMesa);
        System.out.println(idPedido == -1 ? "Esa mesa no esta libre." : "Pedido abierto con id " + idPedido);
    }

    private static void agregarPlato() throws SQLException {
        int idPedido = leerEntero("Id del pedido: ");
        int idPlato = leerEntero("Id del plato: ");
        int cantidad = leerEntero("Cantidad: ");
        pedidoDAO.agregarPlato(idPedido, idPlato, cantidad);
        System.out.println("Plato agregado al pedido.");
    }

    private static void verCuenta() throws SQLException {
        int idPedido = leerEntero("Id del pedido: ");
        pedidoDAO.mostrarCuenta(idPedido);
    }

    private static void cerrarCuenta() throws SQLException {
        int idPedido = leerEntero("Id del pedido: ");
        int idMesa = leerEntero("Id de la mesa asociada: ");
        pedidoDAO.cerrarCuenta(idPedido, idMesa);
        System.out.println("Cuenta cerrada, mesa liberada.");
    }

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!TECLADO.hasNextInt()) {
            System.out.print("Ingrese un numero valido: ");
            TECLADO.next();
        }
        int valor = TECLADO.nextInt();
        TECLADO.nextLine();
        return valor;
    }
}
