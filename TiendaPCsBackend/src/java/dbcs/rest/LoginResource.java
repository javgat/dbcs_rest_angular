/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcs.rest;

import dbcs.dominio.Empleado;
import dbcs.persistencia.EmpleadoFacadeLocal;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 * Recurso REST para la autenticacion inicial
 * Utilizamos base64 pero en una aplicacion real seria mas complejo con por ejemplo tokens
 * y tendria sentido utilizar un recurso especifico para el login, como hacemos.
 *
 * @author Javier Gaton Herguedas y Javier Moro Garcia
 */
@Path("/login")
public class LoginResource implements ContainerResponseFilter{
    EmpleadoFacadeLocal empF = lookupEmpleadoFacadeLocal();

    @Context
    private UriInfo context;
    
    private static final String AUTH_OK="Inicio de sesion con exito";
    private static final String AUTH_WRONG ="Clave incorrecta";
    private static final String AUTH_MISSING ="No hay un empleado con ese nif";
    private static final String AUTH_ERROR= "Error en el acceso";

    /**
     * Creates a new instance of LoginResource
     */
    public LoginResource() {
    }
    
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext response) {
        response.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        response.getHeaders().putSingle("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE");
        response.getHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    /**
     * Indica si consigue iniciar sesion, y si no lo consigue explica cual es el error
     * @param nif Nif o cif del empleado que intenta acceder
     * @param headers Cabeceras con la autenticacion (password)
     * @return Response que tiene el codigo de operacion y un mensaje describiendo como ha ido la operacion al detalle
     */
    @Path("/{nif}")
    @GET
    @Produces("application/json")
    public Response getLogin(@PathParam("nif") String nif, @Context HttpHeaders headers) {
        try{
            String pass_encoded = headers.getRequestHeader("Authorization").get(0);
            //Comprueba la password con el mensaje decodificado en base64
            String password = new String(Base64.getDecoder().decode(pass_encoded.getBytes()));
            System.out.println("Usuario: "+nif+", Password:"+password);
            Empleado emp = empF.find(nif);
            if(emp == null){// Si no existe el empleado
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(makeResponseEntity(nif, AUTH_MISSING))
                        .build();
            }
            if(authenticate(emp, password))// Si si existe y la autenticacion es correcta
                return Response.status(Response.Status.OK)
                    .entity(makeResponseEntity(nif, AUTH_OK))
                    .build();
            else// Si existe pero la autenticacion es incorrecta
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(makeResponseEntity(nif, AUTH_WRONG))
                        .build();
        }catch(Exception e){// Si el servidor falla
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(makeResponseEntity(nif, AUTH_ERROR))
                    .build();
        }
    }
    
    /*
     * Comprueba si la password de un empleado coincide con la indicada
    */
    private boolean authenticate(Empleado emp, String password){
        return emp.getUsuario().getPassword().equals(password);
    }
    
    /*
     * Transforma un mensaje en un objeto JSON del tipo {mensaje : mensaje}
     * Quiza interesaria responder con el nif, asi que lo pedimos en la operacion
     * por si fuera a tener que cambiarse en un futuro
    */
    private String makeResponseEntity(String nif, String message){
        return "{\"mensaje\" : \""+message+"\"}";
    }

    private EmpleadoFacadeLocal lookupEmpleadoFacadeLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (EmpleadoFacadeLocal) c.lookup("java:global/TiendaPCsBackend/EmpleadoFacade!dbcs.persistencia.EmpleadoFacadeLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
