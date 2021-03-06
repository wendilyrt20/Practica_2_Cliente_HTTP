package edu.pucmm.eict.DataBase;


import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DataBase {

    private String URL = "jdbc:h2:tcp://localhost/~/bddCarrito"; //Modo Server...
    private static DataBase instancia;
    private static Server server; //Conexion al server

//////////////////////////////////////////////////////////////////////////////////////////////
    /*Implementando el patron Singleton*/

    private  DataBase(){
        registrarDriver(); ///Funcion para registrar Driver
    }

    public static DataBase getInstancia(){
        if(instancia==null){
            instancia = new DataBase();
        }
        return instancia;
    }
//////////////////////////////////////////////////////////////////////////////////////////////
    //Metodo para el registro de driver de conexión.

    private void registrarDriver() {
        try {
            Class.forName("org.h2.Driver"); //Driver
            System.out.println("Registro de Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("No Registro de Driver");

        }
    }


    //Inicio bdd a modo server
    public static void startDb() throws SQLException {
        server = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-ifNotExists").start();
        System.out.println("Inicio de BDD correcto");
    }

    public Connection getConexion() {  //Conexion
    Connection conexion = null;
    try {
        conexion = DriverManager.getConnection(URL, "sa", "");
    } catch (SQLException ex) {
        System.out.println("Error en la conexion");
    }
    return conexion;
}



    public static void crearTablasProductos() throws  SQLException{
        String sql = "CREATE TABLE IF NOT EXISTS PRODUCTO\n" +
                "(\n" +
                " ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                " NOMBRE VARCHAR(50) NOT NULL,\n" +
                " PRECIO DOUBLE NOT NULL,\n" +
                " CANTIDAD INTEGER NOT NULL\n" +
                ");";
        Connection conexion = DataBase.getInstancia().getConexion();
        Statement statement = conexion.createStatement();
        statement.execute(sql);
        statement.close();
        conexion.close();

        System.out.println("TABLA PRODUCTO CREADA");

    }


    public static void crearTablaUsuario() throws  SQLException{
        String sql = "CREATE TABLE IF NOT EXISTS USUARIO\n" +
                "(\n" +
                " USERNAME VARCHAR(50) PRIMARY KEY NOT NULL,\n" +
                " NOMBRE VARCHAR(100) NOT NULL,\n" +
                " PASSWORD VARCHAR(50) NOT NULL\n" +
                ");";
        Connection con = DataBase.getInstancia().getConexion();
        Statement statement = con.createStatement();
        statement.execute(sql);
        statement.close();
        con.close();
        System.out.println("TABLA USUARIO CREADA");

    }


    public static void crearTablaVenta() throws  SQLException{
        String sql = "CREATE TABLE IF NOT EXISTS VENTAS\n" +
                "(\n" +
                " ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                " FECHA DATE NOT NULL,\n" +
                " NOMBRE VARCHAR(100) NOT NULL,\n" +
                " TOTAL DOUBLE NOT NULL\n" +
                ");";
        Connection con = DataBase.getInstancia().getConexion();
        Statement statement = con.createStatement();
        statement.execute(sql);
        statement.close();
        con.close();
        System.out.println("TABLA VENTA CREADA");

    }

    public static void crearTablaComprado() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS COMPRADO\n" +
                "(\n" +
                "ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                "ID_VENTA INTEGER NOT NULL,\n" +
                "ID_PRODUCTO INTEGER NOT NULL,\n" +
                "CANT INTEGER NOT NULL\n" +
                ");";
        Connection conn = DataBase.getInstancia().getConexion();
        Statement st = conn.createStatement();
        st.execute(sql);
        st.close();
        conn.close();
        System.out.println("TABLA COMPRADO BIEN");
    }

}
