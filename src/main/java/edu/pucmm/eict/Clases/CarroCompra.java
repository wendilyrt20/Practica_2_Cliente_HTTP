package edu.pucmm.eict.Clases;

import java.util.ArrayList;
import java.util.List;

public class CarroCompra {
    private long id;
    private List<Producto> listaProductos;
    public static int incremento =1;
//////////////////////////////////////////////////////////////////////////////////////////////

    public CarroCompra() {
        this.id = incremento;
        this.listaProductos = new ArrayList<>();
        incremento++;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void addProductoCompra(Producto producto){
        listaProductos.add(producto);
    }

    public void addCarrito(Producto p){
        listaProductos.add(p);
    }

    public double cantProducto(List<Producto> p) {
       double valor=0.0;
        for (Producto producto : p) {
            valor = valor + (producto.getCant() * producto.getPrecio());
        }
    return valor;
    }

    public  void deleteProducto(int id, String nombre) {
        for (Producto p : listaProductos) {
            if (p.getId() == id && p.getNombre().equalsIgnoreCase(nombre)){
                listaProductos.remove(p);
                break;
            }
        }
    }

    public int buscarCantCarrito(CarroCompra carro){
        int valor=0;

        for (Producto pro: carro.getListaProductos()) {
            valor += pro.getCant();
        }

        return valor;
    }


}
