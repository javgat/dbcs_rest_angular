/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcs.rest;

import dbcs.dominio.Configuracionpc;
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
import javax.json.JsonObject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;

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
    public Response getCatalogo() {
        return Response.status(Response.Status.OK)
                    .entity(confF.findAll().toArray(new Configuracionpc[0]))
                    .build();
    }
    
    @DELETE
    @Path("{idconfig}")
    @Produces("application/json")
    public Response deleteConfig(@PathParam("idconfig") Integer idConf){
        if(confF.removeConfig(idConf))
            return Response
                    .status(Response.Status.OK)
                    .entity("{ \"message\": \"" + "Configuracion Borrada con exito" + "\"}")
                    .build();
        else
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("{ \"message\": \"" + "Error al borrar la configuracion" + "\"}")
                    .build();
    }

    
    @POST
    @Produces("application/json")
    public Response postConfig(JsonObject conf){
        if(!(conf.containsKey("velocidadcpu") && conf.containsKey("capacidadram") &&
                conf.containsKey("capacidaddd") && conf.containsKey("velocidadtarjetagrafica") &&
                conf.containsKey("memoriatarjetagrafica") && conf.containsKey("tipocpu"))){
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("\"mensaje\":\"faltan datos o las claves no son correctas\"").build();
        }
        int velCPU = conf.getInt("velocidadcpu");
        int capRAM = conf.getInt("capacidadram");
        int capDD = conf.getInt("capacidaddd");
        int velTarGraf = conf.getInt("velocidadtarjetagrafica");
        int memTarGraf = conf.getInt("memoriatarjetagrafica");
        String tipoCPU = conf.getString("tipocpu");
        float precio = conf.containsKey("precio") ? (float)conf.getJsonNumber("precio").doubleValue(): 0;
        if(confF.addConfiguracion(velCPU, capRAM, capDD, velTarGraf, memTarGraf, tipoCPU, precio))
            return Response
                    .status(Response.Status.OK).build();
        else
            return Response.status(Response.Status.FORBIDDEN).build();
    }
    
    
    /**
     * PUT method for updating or creating an instance of ConfiguracionResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Path("{idConfig}")
    @Consumes("application/json")
    public Response modificarConfiguracion(@PathParam("idConfig") Integer idConf, JsonObject conf) {
        int idConfiguracion = idConf;
        int velCPU = getIntDefault("velocidadcpu", conf);
        int capRAM = getIntDefault("capacidadram", conf);
        int capDD = getIntDefault("capacidaddd", conf);
        int velTarGraf = getIntDefault("velocidadtarjetagrafica", conf);
        int memTarGraf = getIntDefault("memoriatarjetagrafica", conf);
        String tipoCPU = conf.containsKey("tipocpu") ? conf.getString("tipocpu") : null;
        float precio = conf.containsKey("precio") ? Float.valueOf(conf.getString("precio")) : 0;
        if(confF.editConfiguracion(idConfiguracion, velCPU, capRAM, capDD, velTarGraf, memTarGraf, tipoCPU, precio))
            return Response.status(Response.Status.OK).build();
        else
            return Response.status(Response.Status.FORBIDDEN).build();
    }
    
    private int getIntDefault(String clave, JsonObject conf){
        return conf.containsKey(clave) ? conf.getInt(clave) : 0;
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
