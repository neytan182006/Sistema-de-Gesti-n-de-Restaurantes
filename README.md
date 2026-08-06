# Sistema de Gestión de Restaurantes

Gestión de mesas y pedidos de un restaurante (Java + MySQL) para el curso de **Bases de Datos**.

## Funcionalidades

- Listar mesas y su estado (libre/ocupada).
- Abrir pedido en una mesa (la ocupa y crea el pedido en una transacción).
- Agregar platos a un pedido.
- Ver la cuenta de un pedido con el total calculado.
- Cerrar cuenta (libera la mesa).

## Estructura

```
src/
├── dao/ConexionBD.java, MesaDAO.java, PedidoDAO.java
└── app/Main.java
```

## Base de datos

[`database/restaurante.sql`](database/restaurante.sql): `MESAS`, `PLATOS`, `PEDIDOS`, `DETALLE_PEDIDO`.

## Cómo ejecutarlo

```bash
mysql -u root -p < database/restaurante.sql
javac -d bin -cp "lib/mysql-connector-j-9.5.0.jar" src/dao/*.java src/app/*.java
java -cp "bin;lib/mysql-connector-j-9.5.0.jar" app.Main
```

> Compilado y verificado con `javac` sin errores; conexión real a MySQL no probada en este entorno (sin servidor corriendo, como acordamos).

## Capturas

_Pendiente: agregar capturas en `capturas/`._

## Licencia

MIT — ver [LICENSE](LICENSE).
