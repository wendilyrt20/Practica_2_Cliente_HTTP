package edu.pucmm.eict.Clases;

public class Usuario {
    String nombre;
    String usuaio;
    String password;

    public Usuario() {
    }
//////////////////////////////////////////////////////////////////////////////////////////////

    public Usuario(String nombre, String usuaio, String password) {
        this.nombre = nombre;
        this.usuaio = usuaio;
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuaio() {
        return usuaio;
    }

    public void setUsuaio(String usuaio) {
        this.usuaio = usuaio;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
////////////////////////////////////////////////////////////////////////////////////////


}

