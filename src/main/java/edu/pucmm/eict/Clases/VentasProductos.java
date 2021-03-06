package edu.pucmm.eict.Clases;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VentasProductos {
    private int id;
    private Date FechaCompra;
    private String nombreCliente;
    private List<CarroCompra> listaCarrito;
    double total ;

//////////////////////////////////////////////////////////////////////////////////////////////

    public VentasProductos(Date fechaCompra, String nombreCliente, double total) {
        this.id = id;
        FechaCompra = fechaCompra;
        this.nombreCliente = nombreCliente;
        this.total = total;
        this.listaCarrito = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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




}
