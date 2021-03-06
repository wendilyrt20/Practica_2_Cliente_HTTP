package edu.pucmm.eict.Clases;

import edu.pucmm.eict.DataBase.DataBase;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controladora{
    private List<Producto> listaProductos; //Productos en Stock
    private List<CarroCompra> listaCarrocompra; //Compra con los productos que se tienen compraron
    private List<Usuario> listaUsuario;
    private List<VentasProductos> listaVentasProductos;

    public Controladora() {
        this.listaProductos = new ArrayList<Producto>();
        this.listaCarrocompra = new ArrayList<CarroCompra>();;
        this.listaUsuario = new ArrayList<Usuario>();;
        this.listaVentasProductos = new ArrayList<VentasProductos>();;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public List<CarroCompra> getListaCarrocompra() {
        return listaCarrocompra;
    }

    public void setListaCarrocompra(List<CarroCompra> listaCarrocompra) {
        this.listaCarrocompra = listaCarrocompra;
    }

    public List<Usuario> getListaUsuario() {
        return listaUsuario;
    }

    public void setListaUsuario(List<Usuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }

    public List<VentasProductos> getListaVentasProductos() {
        return listaVentasProductos;
    }

    public void setListaVentasProductos(List<VentasProductos> listaVentasProductos) {
        this.listaVentasProductos = listaVentasProductos;
    }
//////////////////////////////////////////////////////////////////////
////////////////////////////METODOS///////////////////////////////////
//////////////////////////////////////////////////////////////////////

    public void addProductoDB(Producto producto) throws SQLException {
        String sqlQuery = "INSERT INTO PRODUCTO(NOMBRE, PRECIO, CANTIDAD) values(?,?,?);";
        Connection connection = DataBase.getInstancia().getConexion();
        PreparedStatement prep = connection.prepareStatement(sqlQuery);
        prep.setString(1, producto.getNombre() );
        prep.setDouble(2, producto.getPrecio());
        prep.setInt(3, producto.getCant());
        prep.execute();
        prep.close();
        connection.close();
        System.out.println("PRODUCTO AñADIDO!");
    }

    public void mostrarProducto() throws SQLException {
        String sqlQuery = "SELECT * FROM PRODUCTO";
        Connection connection = DataBase.getInstancia().getConexion();
        PreparedStatement prep =connection.prepareStatement(sqlQuery);
        ResultSet res = prep.executeQuery();

        while(res.next()){
            Producto p = new Producto();
            p.setId(res.getInt("ID"));
            p.setNombre(res.getString("NOMBRE"));
            p.setPrecio(res.getDouble("PRECIO"));
            p.setCant(res.getInt("CANTIDAD"));
            listaProductos.add(p);
        }
        connection.close();
        System.out.println("PRODUCTO MOSTRADO-->!");

    }

    public void editarProducto(Producto p) throws SQLException {
            String sqlQuery = "UPDATE PRODUCTO SET NOMBRE=?, PRECIO=?, CANTIDAD=? WHERE ID=?;";
            Connection connection = DataBase.getInstancia().getConexion();
            PreparedStatement prep = connection.prepareStatement(sqlQuery);
            prep.setString(1, p.getNombre());
            prep.setDouble(2, p.getPrecio());
            prep.setInt(3, p.getCant());
            prep.setInt(4, p.getId());

            prep.executeUpdate();
            prep.close();
            connection.close();
            System.out.println("PRODUCTO EDITADO");

    }

    public void borrarProducto(Producto p) throws SQLException {
        String sqlQuery = "DELETE PRODUCTO  WHERE ID=?;";
        Connection connection= DataBase.getInstancia().getConexion();
        PreparedStatement prep = connection.prepareStatement(sqlQuery);
        prep.setInt(1, p.getId());
        prep.executeUpdate();
        prep.close();
        connection.close();
        System.out.println("PRODUCTO ELIMINADO");
    }




    public void addUserDB(Usuario user) throws SQLException {
        String sqlQuery = "INSERT INTO USUARIO(USERNAME, NOMBRE, PASSWORD) values(?,?,?);";
        Connection connection = DataBase.getInstancia().getConexion();
        PreparedStatement prep = connection.prepareStatement(sqlQuery);
        prep.setString(1, user.getUsuaio() );
        prep.setString(2, user.getNombre());
        prep.setString(3, user.getPassword());
        prep.execute();
        prep.close();
        connection.close();
        listaUsuario.add(user);
        System.out.println("USUARIO AñADIDO!");
    }



    public void addVentaDB(VentasProductos venta) throws SQLException {
        String sqlQuery = "INSERT INTO VENTAS(FECHA, NOMBRE, TOTAL) values(?,?,?);";
        Connection connection = DataBase.getInstancia().getConexion();
        PreparedStatement prep = connection.prepareStatement(sqlQuery);
        prep.setDate(1, (Date) venta.getFechaCompra());
        prep.setString(2, venta.getNombreCliente());
        prep.setDouble(3, venta.getTotal());
        prep.execute();
        prep.close();
        connection.close();
        System.out.println("VENTA AñADIDO!");

    }
    public int  buscarVentaDB() throws SQLException {
        int id_venta =0;
        String sqlQuery = "select id from ventas order by id desc limit 1 ";
        Connection connection = DataBase.getInstancia().getConexion();
        PreparedStatement prep = connection.prepareStatement(sqlQuery);
        ResultSet rs = prep.executeQuery();
        while (rs.next()){
            id_venta=rs.getInt("ID");
        }
        prep.execute();
        prep.close();
        connection.close();
        System.out.println("VENTA AñADIDO!");
        return id_venta;
    }




    public void addCompradoDB(int id_venta, int id_producto, int cantidad) throws SQLException {
        String sqlQuery = "INSERT INTO COMPRADO(ID_VENTA, ID_PRODUCTO, CANT) values(?,?,?);";
        Connection connection = DataBase.getInstancia().getConexion();
        PreparedStatement prep = connection.prepareStatement(sqlQuery);
        prep.setInt(1,id_venta);
        prep.setInt(2, id_producto);
        prep.setInt(3,cantidad);
        prep.execute();
        connection.close();
        System.out.println("ID VENTA " + id_venta);
        System.out.println("ID PRODUCTO"+ id_producto);
        System.out.println("CANT"+ cantidad);
        System.out.println("VENTA COMPRADO ANADIDO!");

    }

    public void mostrarVentas() throws SQLException {
        String sqlQueryVentas = "SELECT *FROM VENTAS";
        String sqlQueryProductos = "SELECT COMPRADO.ID_VENTA, COMPRADO.ID_PRODUCTO, COMPRADO.CANT, PRODUCTO.NOMBRE NOMBRE_PRODUCTO, PRODUCTO.PRECIO\n" +
                "FROM COMPRADO\n" +
                "INNER JOIN PRODUCTO ON PRODUCTO.ID = COMPRADO.ID_PRODUCTO\n" +
                "WHERE COMPRADO.ID_VENTA=?";

        Connection connection = DataBase.getInstancia().getConexion();
        PreparedStatement prepV = connection.prepareStatement(sqlQueryVentas);
        ResultSet resVentas = prepV.executeQuery();

        while (resVentas.next()){
            int id = resVentas.getInt("ID");
            java.util.Date fecha = resVentas.getDate("FECHA");
            String nombre = resVentas.getString("NOMBRE");
            double total = resVentas.getDouble("TOTAL");
            VentasProductos ven = new VentasProductos(fecha, nombre,total);
            ven.setId(id);

            PreparedStatement prepP = connection.prepareStatement(sqlQueryProductos);
            prepP.setInt(1,id);
            ResultSet resProducto = prepP.executeQuery();
            ArrayList <Producto> producto = new ArrayList<Producto>();
            CarroCompra carrito = new CarroCompra();

            while (resProducto.next()){
                int id_producto = resProducto.getInt("ID_PRODUCTO");
                String nombreproducto = resProducto.getString("NOMBRE_PRODUCTO");
                double precio = resProducto.getDouble("PRECIO");
                int cantidad = resProducto.getInt("CANT");
                Producto p = new Producto(id_producto, nombreproducto,precio,cantidad);
                producto.add(p);
                carrito.setListaProductos(producto);
            }
            ven.addCarritoV(carrito);
            listaVentasProductos.add(ven);
            prepP.close();
            System.out.println("PRODUCTOS ANADIDOS A CARRITO - VENTA");

        }

        prepV.close();
        connection.close();
        System.out.println("VENTA PROCESADA BIEN-->!");


    }


    public void addUsuario(Usuario user){
        listaUsuario.add(user);
    }


    public Producto buscarProducto(int id){
        Producto product = null;
        for (Producto pro: listaProductos) {
            if (pro.getId()==id) {
                product =pro;
                break;
            }
        }
        return product;
    }





}
