package edu.pucmm.eict.Clases;

import java.math.BigDecimal;

public class Producto {
    private int id;
    private String nombre;
    private Double precio;
    private int cant; //cantidad de producto en stock
    public static int incremento =1;
//////////////////////////////////////////////////////////////////////////////////////////////

    public Producto(String nombre, Double precio, int cant) {
        this.id = incremento;
        this.nombre = nombre;
        this.precio = precio;
        this.cant = cant;
        incremento++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public int getCant() {
        return cant;
    }

    public void setCant(int cant) {
        this.cant = cant;
    }
//////////////////////////////////////////////////////////////////////////////////////////////

}
