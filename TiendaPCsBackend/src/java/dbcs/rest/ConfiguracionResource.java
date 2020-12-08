/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcs.rest;

import dbcs.persistencia.ConfiguracionpcFacadeLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * REST Web Service
 *
 * @author Javier
 */
@Path("/configuracion")
public class ConfiguracionResource {
    ConfiguracionpcFacadeLocal confF = lookupConfiguracionpcFacadeLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ConfiguracionResource
     */
    public ConfiguracionResource() {
    }

    /**
     * Retrieves representation of an instance of dbcs.rest.ConfiguracionResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public Response getJson() {
        return Response.status(Response.Status.OK)
                    .entity(confF.findAll().get(0))
                    .build();
    }

    /**
     * PUT method for updating or creating an instance of ConfiguracionResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }

    private ConfiguracionpcFacadeLocal lookupConfiguracionpcFacadeLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ConfiguracionpcFacadeLocal) c.lookup("java:global/TiendaPCsBackend/ConfiguracionpcFacade!dbcs.persistencia.ConfiguracionpcFacadeLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}