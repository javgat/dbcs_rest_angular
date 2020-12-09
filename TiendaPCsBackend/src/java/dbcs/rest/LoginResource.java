/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcs.rest;

import dbcs.dominio.Empleado;
import dbcs.persistencia.EmpleadoFacadeLocal;
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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Javier
 */
@Path("/login")
public class LoginResource {
    EmpleadoFacadeLocal empF = lookupEmpleadoFacadeLocal();

    @Context
    private UriInfo context;
    
    private static final String AUTH_OK="Todo correcto";
    private static final String AUTH_WRONG ="Clave incorrecta";
    private static final String AUTH_MISSING ="No hay un empleado con ese nif";
    private static final String AUTH_ERROR= "Error en el acceso";

    /**
     * Creates a new instance of LoginResource
     */
    public LoginResource() {
    }

    /**
     * Retrieves representation of an instance of dbcs.rest.LoginResource
     * @return an instance of java.lang.String
     */
    @Path("/{nif}")
    @GET
    @Produces("application/json")
    public Response getLogin(@PathParam("nif") String nif, @Context HttpHeaders headers) {
        try{
            String password = headers.getRequestHeader("Authorization").get(0);
            System.out.println("Usuario: "+nif+", Password:"+password);
            Empleado emp = empF.find(nif);
            if(emp == null){
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(makeResponseEntity(nif, AUTH_MISSING))
                        .build();
            }
            if(authenticate(emp, password))
                return Response.status(Response.Status.OK)
                    .entity(makeResponseEntity(nif, AUTH_OK))
                    .build();
            else
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(makeResponseEntity(nif, AUTH_WRONG))
                        .build();
        }catch(Exception e){
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)//Los status son correctos?
                    .entity(makeResponseEntity(nif, AUTH_ERROR))
                    .build();
        }
    }
    
    private boolean authenticate(Empleado emp, String password){
        // Version basica, cambiarla en el futuro seguramente (cifrado)
        return emp.getUsuario().getPassword().equals(password);
    }
    
    private String makeResponseEntity(String nif, String message){
        return "{ \"nif\" : \""+nif+"\","
                + "\"message\" : \""+message+"\"}";
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
