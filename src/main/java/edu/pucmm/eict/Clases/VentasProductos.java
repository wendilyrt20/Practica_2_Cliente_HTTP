package edu.pucmm.eict.Clases;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VentasProductos {
    private long id;
    private Date FechaCompra;
    private String nombreCliente;
    private List<CarroCompra> listaCarrito;
    double total ;
    public static int incremento=1;

//////////////////////////////////////////////////////////////////////////////////////////////

    public VentasProductos(Date fechaCompra, String nombreCliente, double total) {
        this.id = incremento;
        FechaCompra = fechaCompra;
        this.nombreCliente = nombreCliente;
        this.total = total;
        this.listaCarrito = new ArrayList<>();
        incremento++;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFechaCompra() {
        return FechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        FechaCompra = fechaCompra;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public List<CarroCompra> getListaCarrito() {
        return listaCarrito;
    }

    public void setListaCarrito(List<CarroCompra> listaCarrito) {
        this.listaCarrito = listaCarrito;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
//////////////////////////////////////////////////////////////////////////////////////////////

    public void addCarritoV(CarroCompra carro){
        listaCarrito.add(carro);
    }

    public double cantProducto(List<VentasProductos>ven) {
        double valor=0.0;
        for (CarroCompra car: listaCarrito) {
            for (Producto producto : car.getListaProductos()) {
                valor = valor + (producto.getCant() * producto.getPrecio());
            }
        }
        return valor;
    }




}
