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
import java.util.Base64;
import java.util.List;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

/**
 * Recurso REST para el acceso a los empleados
 *
 * @author Javier Gaton Herguedas y Javier Moro Garcia
 */
@Path("/empleado")
public class EmpleadoResource implements ContainerResponseFilter{
    EmpleadoFacadeLocal empleadoFacade = lookupEmpleadoFacadeLocal();

    @Context
    private UriInfo context;
    
    private static final String EMP_NOT_FOUND = "No existe ese empleado";
    private static final String EMP_ERROR = "Error interno del servidor";
    private static final String EMP_AUTH = "No tienes permisos para acceder a esa informacion";

    /**
     * Creates a new instance of EmpleadoResource
     */
    public EmpleadoResource() {
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext response) {
        response.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        response.getHeaders().putSingle("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE");
        response.getHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
    
    /*
     * 
     * @param emp Empleado a intentar autenticar
     * @param auth Cadena con el formato <nif>:<password> en base64.
     * @return True si corresponde con el empleado, false si no
     */
    private boolean isAuth(Empleado emp, String auth){
        String decoded = new String(Base64.getDecoder().decode(auth.getBytes()));
        String[] split = decoded.split(":");
        String nif = split[0];
        String pass = split[1];
        if(!nif.equals(emp.getNifcif()))
            return false;
        if(!pass.equals(emp.getUsuario().getPassword()))
            return false;
        return true;
    }
    
    /**
     * Obtiene los datos
     * @param nif Nif o Cif del empleado del que intenta obtener los datos
     * @param headers Cabeceras con la Autenticacion
     * @return Respuesta con codigo de operacion y el empleado pedido en caso de exito,
     * o un mensaje de error si falla.
     */
    @GET
    @Path("{nif}")
    @Produces("application/json")
    public Response getEmpleado(@PathParam("nif") String nif, @Context HttpHeaders headers){// Habra que exigir autenticacion
        try{
            Empleado emp = empleadoFacade.find(nif);
            if(emp==null){//Si no existe un empleado con ese nif
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("{ \"message\": \""+EMP_NOT_FOUND+"\"}")
                    .build();
            }
            List<String> heads = headers.getRequestHeader("Authorization");
            if(heads==null || heads.isEmpty() || !isAuth(emp, heads.get(0)))//Si la autenticacion falta o es incorrecta
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("{ \"message\": \""+EMP_AUTH+"\"}")
                        .build();
            String pais = emp.getUsuario().getPais();
            return Response.status(Response.Status.OK)// En este punto devuelve el nif y el pais del empleado
                    .entity("{\"nif\" : \""+nif+"\","
                            + "\"pais\" : \""+pais+"\"}")
                    .build();
        }catch(Exception e){ //En caso de que fallara el servidor
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{ \"message\": \""+EMP_ERROR+"\"}")
                .build();
        }
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
