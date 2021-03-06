package edu.pucmm.eict.Clases;

public class Usuario {
    String nombre;
    String usuario;
    String password;

    public Usuario() {
    }
//////////////////////////////////////////////////////////////////////////////////////////////

    public Usuario(String nombre, String usuario, String password) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuaio() {
        return usuario;
    }

    public void setUsuaio(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
////////////////////////////////////////////////////////////////////////////////////////


}

