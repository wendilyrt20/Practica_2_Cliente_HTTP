package edu.pucmm.eict.Clases;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    public void addProducto(Producto producto){
    listaProductos.add(producto);
    }

    public void addUsuario(Usuario user){
        listaUsuario.add(user);
    }
    public void addVenta(VentasProductos venta){
        listaVentasProductos.add(venta);
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


    public void borrarProducto(Producto p){
        listaProductos.remove(p);

    }



/*
    public void borrarProducto(int id, String name, BigDecimal precio,int cant){
        ArrayList<Producto> producto = new ArrayList<>();
        for (Producto prod: producto) {
            if (prod.getId() == id && prod.getNombre().equalsIgnoreCase(name)){
                producto.remove(prod);
            }
        }

    }

    public int cantProducto(int idCarrito){
        int valor=0;
        for (CarroCompra carro: this.listaCarrocompra) {
            for (Producto product: carro.listaProductos) {
                if (carro.id == idCarrito){
                    valor+=product.cant;
                }
            }
        }
        return valor;
    }



    public Double totalCarrito(int id){
        Double total = 0.0;
        for (CarroCompra carro: this.listaCarrocompra) {
            for (Producto pro: carro.listaProductos) {
                total += pro.getPrecio();
            }
        }
        return total;
    }
/////////////////////////////////////////////////////////////////////////
*/



}
