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
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Javier
 */
@Path("/empleado")
public class EmpleadoResource {
    EmpleadoFacadeLocal empleadoFacade = lookupEmpleadoFacadeLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of EmpleadoResource
     */
    public EmpleadoResource() {
    }

    /**
     * Retrieves representation of an instance of dbcs.rest.EmpleadoResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }
    
    @GET
    @Path("{nif}")
    @Produces("application/json")
    public Response getEmpleado(@PathParam("nif") String nif){
        try{
            Empleado emp = empleadoFacade.find(nif);
            if(emp==null){
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("{ \"message\": \"Empleado no encontrado\"}")
                    .build();
            }
            String pais = emp.getUsuario().getPais();
            return Response.status(Response.Status.OK)
                    .entity("{\"nif\" : \""+nif+"\","
                            + "\"pais\" : \""+pais+"\"}")
                    .build();
        }catch(Exception ex){
            Logger.getLogger(EmpleadoResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.NOT_FOUND)
                .entity("{ \"message\": \"Empleado no encontrado\"}")
                .build();
        }
    }

    /**
     * PUT method for updating or creating an instance of EmpleadoResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
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
