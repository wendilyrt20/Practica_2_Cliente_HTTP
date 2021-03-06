package edu.pucmm.eict;

import edu.pucmm.eict.Clases.*;
import edu.pucmm.eict.DataBase.DataBase;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinFreemarker;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import org.jasypt.util.text.StrongTextEncryptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.Cookie;
import javax.swing.*;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static io.javalin.apibuilder.ApiBuilder.*;


public class Main {
    static String tempURI = "";
    static String path="";
    public static void main(String[] args) throws SQLException {

////////////////////////BDD////////////////////////////////////////////////

        //  DataBase.stopDb();
        DataBase.startDb(); //Inicio server

        DataBase.crearTablasProductos();
        DataBase.crearTablaUsuario();
        DataBase.crearTablaVenta();
        DataBase.crearTablaComprado();
////////////////////////////////////////////////////////////////////////////


        Controladora control = new Controladora(); //Instancia controladora

        Usuario userAdmin = new Usuario("Wendily", "admin", "admin");
        Producto p1 = new Producto(1,"Carro1", 500.0, 80);
        Producto p2 = new Producto(2,"Carro2", 300.0, 10);
        Producto p3 = new Producto(3,"Carro3", 100.0, 15);
/*
        control.addProductoDB(p1);
        control.addProductoDB(p2);
        control.addProductoDB(p3);
*/
        //control.addUsuario(user);

        control.mostrarProducto();
        control.mostrarVentas();
        control.getListaUsuario().clear();
        //control.addUserDB(userAdmin);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");//Instancia motor de plantilla Thymeleaf.

        //Creando la instancia del servidor.
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/publico"); //desde la carpeta de resources --> Ruta estatica.
        });
        app.start(7000);
        System.out.println("holaaaaaa");


        app.get("/", ctx -> {

            int cantidad = 0;
            //control.mostrarProducto();
            List<Producto> producto = control.getListaProductos();
            Map<String,Object> modelo = new HashMap<>();
            modelo.put("listaP", producto);
            if(ctx.sessionAttribute("SesionCarrito")!= null){
               CarroCompra c = ctx.sessionAttribute("SesionCarrito");
                cantidad= c.buscarCantCarrito(c);
                control.getListaProductos().removeAll(control.getListaProductos());
                control.mostrarProducto();
                System.out.println(" PPPP--SIZE P CARRITOOOOO--->" + cantidad);

            }
            ctx.sessionAttribute("cantidad", cantidad);
            modelo.put("cantidad",cantidad);
            ctx.render("/templates/comprar.html", modelo);
        });

        app.post("/addCarrito", ctx -> {
            int id= Integer.parseInt(ctx.formParam("id"));
            int cant = Integer.parseInt(ctx.formParam("cant"));
            Producto aux= control.buscarProducto(id);
            Producto pCarrito = new Producto(aux.getId(),aux.getNombre(), aux.getPrecio(), cant );

            //Crar sesion para carrito
            if (ctx.sessionAttribute("SesionCarrito")== null) { //comprobación de sesión carrito
               CarroCompra carrito = new CarroCompra();
               carrito.addCarrito(pCarrito);
                ctx.sessionAttribute("SesionCarrito", carrito); //crear sesión
                System.out.println("SESION CARRITO CREADA");
                System.out.println("SIZE: " + carrito.getListaProductos().size());
                ctx.redirect("/");
            }
            else if (ctx.sessionAttribute("SesionCarrito") != null){ //ya está creada
                CarroCompra carrito = ctx.sessionAttribute("SesionCarrito"); //añadir a carrito en la sesion creada
                carrito.addCarrito(pCarrito);
                System.out.println("Sesion carrito estuvo creada");
                System.out.println("Carrito--> " + carrito.getListaProductos().size());
                ctx.redirect("/");
            }
        });

        app.get("/limpiarC", ctx -> {
            ctx.sessionAttribute("SesionCarrito",null);
            ctx.redirect("/");
        });
///////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////// BOTONES SUPERIORES ///////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////

        app.get("/carrito", ctx -> {
            try{
                CarroCompra carro = ctx.sessionAttribute("SesionCarrito");
                List<Producto> p =  carro.getListaProductos();
                Double total = carro.cantProducto(p);
                ctx.sessionAttribute("total", total);
                Map<String,Object> modelo = new HashMap<>();
                modelo.put("lista",p);
                modelo.put("total", total);
                int cantidad = ctx.sessionAttribute("cantidad");
                modelo.put("cantidad",cantidad);
                ctx.render("/Templates/carrito.html", modelo);
            }catch(NullPointerException ex){
            ctx.redirect("/");
            }
        });


        app.get("/ventasR",ctx -> {
            if (ctx.sessionAttribute("admin") == null) {
                System.out.println(" SESION= NULL");
                tempURI= ctx.req.getRequestURI();
                ctx.render("/Templates/login.html");

            }
            else if (ctx.sessionAttribute("admin") != null){
                control.mostrarVentas();
                List<VentasProductos> venta = control.getListaVentasProductos();
                System.out.println("VENTA SESION BIEN");
                System.out.println("ADMIN PRODUCT");
                int cantidad = ctx.sessionAttribute("cantidad");
                CarroCompra car = ctx.sessionAttribute("SesionCarrito");
                Map<String,Object> modelo = new HashMap<>();
                modelo.put("listaV", venta);
                modelo.put("cantidad",cantidad);
                //  modelo.put("total", total);
                ctx.render("/Templates/listadoVentasRealizadas.html",modelo);
            }
            else {
                ctx.redirect("/");
            }
        });


        app.get("/adminP",ctx -> {

            if (ctx.sessionAttribute("admin") == null) {
                System.out.println("NO SESION= NULL");
                tempURI= ctx.req.getRequestURI();
                ctx.path();
                ctx.render("/Templates/login.html");
            }
            else if (ctx.sessionAttribute("admin") != null){
                control.getListaProductos().clear();
                control.mostrarProducto();
                List<Producto> product = control.getListaProductos();
                Map<String,Object> modelo = new HashMap<>();
                modelo.put("listaP", product);
                System.out.println("ADMIN PRODUCT");
                int cantidad = ctx.sessionAttribute("cantidad");
                modelo.put("cantidad",cantidad);
                ctx.render("/Templates/adminProducto.html",modelo);
            }

        });


        app.get("/comprar", ctx -> {
            ctx.redirect("/");

        });

/////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////

        app.post("/login",ctx -> {
            String usuario= ctx.formParam("username");
            String pass = ctx.formParam("password");
            System.out.println("LOGIN");
            if (usuario.equalsIgnoreCase("admin") && pass.equalsIgnoreCase("admin")){
                System.out.println("USER CORRECTO");
                Usuario admin = new Usuario("admin", usuario, pass);
                control.addUsuario(admin);
                ctx.sessionAttribute("admin", admin);

                if ( ctx.formParam("rememberMe")!= null){
                    StrongTextEncryptor enc = new StrongTextEncryptor();
                    enc.setPassword("pass");
                    String adminEncryptor = enc.encrypt(admin.getPassword());
                    ctx.cookie("rememberMe", adminEncryptor,60480); //Coockie con duracion 1 semana
                    ctx.result("Cookie creada...");
                    ctx.redirect(tempURI);
                }
            } else {
                ctx.redirect("/adminP");
            }
        });


        app.post("/comprar/carrito", ctx -> {
            String user = ctx.formParam("user");
            Date date = new Date();
            java.sql.Date datesql = new java.sql.Date(date.getTime());
            CarroCompra carro = ctx.sessionAttribute("SesionCarrito");
            Double total = carro.cantProducto(carro.getListaProductos());

                VentasProductos venta = new VentasProductos(datesql,user,total);
                control.addVentaDB(venta);
            int id_venta = control.buscarVentaDB();
            System.out.println("CARRITOO size productos-> " + carro.getListaProductos().size());
            for (Producto p : carro.getListaProductos()) {
                control.addCompradoDB(id_venta,p.getId(),p.getCant());
            }
               // ctx.sessionAttribute("ventas", control.getListaVentasProductos());
                    System.out.println("VENTA REALIZADA--> ID VENTA--> " + id_venta);
                   // System.out.println("---->" + control.getListaVentasProductos().size());
                 //   System.out.println(venta.getListaCarrito());
                ctx.sessionAttribute("SesionCarrito",null);
                ctx.redirect("/");

        });





        app.get("/crearProd", ctx -> {
            System.out.println("Aqui en crear productos");
           Producto producto = new Producto(0," ",0.0,0);
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("producto", producto);
            ctx.render("/Templates/crearProducto.html",modelo);
        });

        app.post("/producto/new", ctx -> {
            int id = ctx.formParam("idp",Integer.class).get();
            String nombre= ctx.formParam("productname");
             Double precio = ctx.formParam("price", Double.class).get();
            Integer  cantidad = Integer.parseInt(ctx.formParam("cantidad"));
            System.out.println("------------------------->"+ id);

            if (control.buscarProducto(id) !=null) {
                System.out.println("MODIFICANDO PRODUCTO");
                Producto producto= control.buscarProducto(id);
                producto.setCant(cantidad);
                producto.setPrecio(precio);
                producto.setNombre(nombre);
                control.editarProducto(producto);
                control.getListaProductos().clear();
                control.mostrarProducto();
            }
            else if(control.buscarProducto(id) == null){
                Producto producto =new Producto(id,nombre,precio,cantidad);
                control.addProductoDB(producto);
                System.out.println("NUEVO PRODUCTO");
            }
            ctx.redirect("/adminP");
        });

        app.post("/delete/product/compra", ctx -> {
            Integer id = Integer.parseInt(ctx.formParam("id"));
            String nombre = ctx.formParam("nombre");
            CarroCompra carrito = ctx.sessionAttribute("SesionCarrito");
            carrito.deleteProducto(id,nombre);
            ctx.redirect("/carrito");
        });

        app.get("/cancel", ctx -> {
            ctx.redirect(tempURI);
        });

        app.get("/productoEditar/:id", ctx -> {
            int id= ctx.pathParam("id",Integer.class).get();
            Producto producto = control.buscarProducto(id);
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("producto", producto);
            System.out.println("ID PARA MODIFICARRRR" + id + modelo);
            ctx.render("/Templates/crearProducto.html",modelo);
        });

        app.get("/productoEliminar/:id",ctx -> {
            Integer id= ctx.pathParam("id",Integer.class).get();
            Producto producto = control.buscarProducto(id);
            control.borrarProducto(producto);
            ctx.redirect("/adminP");

        });



    }
}





