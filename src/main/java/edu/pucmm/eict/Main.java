package edu.pucmm.eict;

import edu.pucmm.eict.Clases.*;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinFreemarker;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static io.javalin.apibuilder.ApiBuilder.*;


public class Main {
    static String tempURI = "";
    static String path="";
    public static void main(String[] args) {
        Controladora control = new Controladora(); //Instancia controladora
        /////////////////////////////////////////////////////////////////////////////////////////////////

        //Usuario user = new Usuario("Wendily", "admin", "admin");
        Producto p1 = new Producto("Carro1", 100.0, 10);
        Producto p2 = new Producto("Carro2", 100.0, 10);
        Producto p3 = new Producto("Carro3", 100.0, 10);

        control.addProducto(p1);
        control.addProducto(p2);
        control.addProducto(p3);

        //control.addUsuario(user);


        System.out.println("ndjksdnfjknsdjkfnkjdsnfdf");
        ///////////////////////////////////////////////////////////////////////////////////////////////////
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");//Instancia motor de plantilla Thymeleaf.

        //Creando la instancia del servidor.
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/publico"); //desde la carpeta de resources --> Ruta estatica.
        });
        app.start(7000);
        System.out.println("holaaaaaa");

////////////////////////////////////////////////////////////////////////////////////////

        app.get("/", ctx -> {
            List<Producto> producto = control.getListaProductos();
            Map<String,Object> modelo = new HashMap<>();
            modelo.put("listaP", producto);
            System.out.println(" PPPP" + producto.size());
            ctx.render("/templates/comprar.html", modelo);
        });

        app.post("/addCarrito", ctx -> {
            int id= Integer.parseInt(ctx.formParam("id"));
            int cant = Integer.parseInt(ctx.formParam("cant"));
            Producto aux= control.buscarProducto(id);
            Producto pCarrito = new Producto( aux.getNombre(), aux.getPrecio(), cant );

            //Crar sesion para carrito
            if (ctx.sessionAttribute("SesionCarrito")== null) {
               CarroCompra carrito = new CarroCompra();
               carrito.addCarrito(pCarrito);
                ctx.sessionAttribute("SesionCarrito", carrito); //sesion creada
                System.out.println("SESION CARRITO CREADA");
                System.out.println("SIZE: " + carrito.getListaProductos().size());
                ctx.redirect("/");
            }
            else if (ctx.sessionAttribute("SesionCarrito") != null){
                CarroCompra carrito = ctx.sessionAttribute("SesionCarrito");
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

                    ///////    BOTONES SUPERIORES  ////////
        app.get("/carrito", ctx -> {
            try{
                CarroCompra carro = ctx.sessionAttribute("SesionCarrito");
                List<Producto> p =  carro.getListaProductos();
                Double total = carro.cantProducto(p);
                Map<String,Object> modelo = new HashMap<>();
                modelo.put("lista",p);
                modelo.put("total", total);
                ctx.render("/Templates/carrito.html", modelo);
            }catch(NullPointerException ex){
            ctx.redirect("/");
            }



        });

        app.get("/comprar", ctx -> {
            ctx.redirect("/");

        });

        app.get("/ventasR",ctx -> {
            if (ctx.sessionAttribute("admin") == null) {
                System.out.println("NO SESION= NULL");
                tempURI= ctx.req.getRequestURI();
                ctx.render("/Templates/login.html");
            }
            else if (ctx.sessionAttribute("admin") != null){
                List<VentasProductos> venta = ctx.sessionAttribute("ventas");
                System.out.println("VENTA SESION BIEN");
                //List<CarroCompra> compra = ctx.sessionAttribute("SesionCarrito");
                Map<String,Object> modelo = new HashMap<>();
                //modelo.put("listaC", compra);
                modelo.put("listaV", venta);

                System.out.println("ADMIN PRODUCT");
                ctx.render("/Templates/listadoVentasRealizadas.html",modelo);
            }
        });
        app.post("/comprar/carrito", ctx -> {
            String user = ctx.formParam("user");
            Date date = new Date();
            CarroCompra carro = ctx.sessionAttribute("SesionCarrito");

                VentasProductos venta = new VentasProductos(date,user);
                venta.addCarritoV(carro);
                control.addVenta(venta);
                ctx.sessionAttribute("ventas", control.getListaVentasProductos());
                System.out.println("VENTA REALIZADA " + venta.getNombreCliente());
                System.out.println("---->" + control.getListaVentasProductos().size());
                System.out.println(venta.getListaCarrito());
                ctx.sessionAttribute("SesionCarrito",null);
                ctx.redirect("/");

               // VentasProductos venta = ctx.sessionAttribute("ventas");
              //  venta.addCarritoV(carro);
               // control.addVenta(venta);
               // System.out.println("VENTA--> " + venta.getNombreCliente());
               // System.out.println("VENTA PROCESADA----->" + control.getListaVentasProductos().size());
            //    ctx.sessionAttribute("SesionCarrito",null);
            //    ctx.redirect("/");

        });

        app.get("/adminP",ctx -> {

            if (ctx.sessionAttribute("admin") == null) {
                System.out.println("NO SESION= NULL");
                tempURI= ctx.req.getRequestURI();
                ctx.path();
                ctx.render("/Templates/login.html");
            }
            else if (ctx.sessionAttribute("admin") != null){
                List<Producto> product = control.getListaProductos();
                Map<String,Object> modelo = new HashMap<>();
                //System.out.println("bla bla bla"+ctx.sessionAttribute("admin").toString());
                modelo.put("listaP", product);
                System.out.println("ADMIN PRODUCT");
                ctx.render("/Templates/adminProducto.html",modelo);
            }

        });


        app.post("/login",ctx -> {
            String usuario= ctx.formParam("username");
            String pass = ctx.formParam("password");
            System.out.println("LOGIN");
            if (usuario.equalsIgnoreCase("admin") && pass.equalsIgnoreCase("admin")){
                System.out.println("USER CORRECTO");
                Usuario admin = new Usuario("admin", usuario, pass);
                control.addUsuario(admin);
                ctx.sessionAttribute("admin", admin);
                ctx.redirect(tempURI);
            }
        });

        app.get("/crearProd", ctx -> {
            System.out.println("Aqui en crear productos");
            Producto producto = new Producto(" ",0.0,0);
            producto.setId(0);
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("producto", producto);


            ctx.render("/Templates/crearProducto.html", modelo);
        });

        app.post("/producto/new", ctx -> {
            int id = ctx.formParam("idp",Integer.class).get();
            String nombre= ctx.formParam("productname");
                     System.out.println("AGREGANDO NOMBRE AL PRODUCTO");
             Double precio = ctx.formParam("price", Double.class).get();
                 System.out.println("AGREGANDO NOMBRE AL PRODUCTO");
            Integer  cantidad = Integer.parseInt(ctx.formParam("cantidad"));
            System.out.println("AGREGANDO NOMBRE AL PRODUCTO");

            if (control.buscarProducto(id)!= null ){
                System.out.println("MODIFUCANDO");
                Producto p = control.buscarProducto(id);
                p.setNombre(nombre);
                p.setPrecio(precio);
                p.setCant(cantidad);
            }
            else if(control.buscarProducto(id) == null){
                Producto producto=new Producto(nombre,precio,cantidad);
                control.addProducto(producto);
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
        /*
         String name = ctx.formParam("name");
                    List<ShoppingCart> listProductCart = ctx.sessionAttribute("cart");
                    Date date = new Date();

                  InvoiceProduct invp = new InvoiceProduct(idInvoice,name,date);
                    invp.addProduct(listProductCart);
                    float pr = invp.totalPrice();
                    invp.setTotalPrice(pr);
                    storeController.addInvoice(invp);

                    ctx.sessionAttribute("sales",storeController.getListSaleProduct());
                    ctx.sessionAttribute("cart",null);
                    idInvoice++;
         */




/*
 if (ctx.sessionAttribute("SesionCarrito")== null) {
               CarroCompra carrito = new CarroCompra();
               carrito.addCarrito(pCarrito);
                ctx.sessionAttribute("SesionCarrito", carrito); //sesion creada
                System.out.println("Sesion creada");
                System.out.println("SIZE: " + carrito.getListaProductos().size());
                ctx.redirect("/");
            }
            else if (ctx.sessionAttribute("SesionCarrito") != null){
                CarroCompra carrito = ctx.sessionAttribute("SesionCarrito");
                carrito.addCarrito(pCarrito);
                System.out.println("Sesion estuvo creada");
                System.out.println("Carrito--> " + carrito.getListaProductos().size());
                ctx.redirect("/");
 */




/*
                app.get("/producto/editar", ctx -> {
                    System.out.println("ksdnfkjsdkfjkdhfjdnjfhbnfv");
                    //Integer id = ctx.pathParam("id", Integer.class).get();
                    Integer id = Integer.valueOf(ctx.formParam("id"));
                    Producto producto = control.buscarProducto(id);
                    Map<String,Object> modelo = new HashMap<>();
                    modelo.put("producto", producto);
                    System.out.println("dsfdf"+ id);
                    ctx.render("/Templates/crearProducto.html",modelo);
                });

*/




        /*app.get("/ventas/realizadas", ctx -> {

        });
*/





    }

}





