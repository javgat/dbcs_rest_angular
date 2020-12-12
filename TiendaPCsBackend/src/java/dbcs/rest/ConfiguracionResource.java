/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcs.rest;

import dbcs.dominio.Configuracionpc;
import dbcs.dominio.Empleado;
import dbcs.persistencia.ConfiguracionpcFacadeLocal;
import dbcs.persistencia.EmpleadoFacadeLocal;
import java.util.Base64;
import java.util.List;
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
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;

/**
 * REST Web Service
 *
 * @author Javier
 */
@Path("/configuracion")
public class ConfiguracionResource implements ContainerResponseFilter {
    EmpleadoFacadeLocal empleadoFacade = lookupEmpleadoFacadeLocal();
    ConfiguracionpcFacadeLocal confF = lookupConfiguracionpcFacadeLocal();

    @Context
    private UriInfo context;
    
    private static final String SERV_ERR="Error en el sistema al acceder a la configuracion";
    private static final String CONF_BORR_OK="Configuracion borrada con exito";
    private static final String CONF_BORR_ERROR="Error al intentar borrar la configuracion seleccionada";
    private static final String CONF_ADD_MISSING="Faltan datos o los nombres de los parametros no son correctos";
    private static final String CONF_ADD_WRONG="Los tipos de datos pasados son incorrectos";
    private static final String CONF_ADD_OK="Configuracion introducida correctamente";
    private static final String CONF_ADD_FAIL="Configuracion no se pudo introducir";
    private static final String CONF_EDIT_OK="Configuracion editada con exito";
    private static final String CONF_EDIT_NOT_FOUND="La configuracion que intentas editar no existe, o los parametros introducidos no son validos";
    private static final String ERR_AUTH="No tiene permisos para realizar esa operacion";
    
    /**
     * Creates a new instance of ConfiguracionResource
     */
    public ConfiguracionResource() {
    }
    
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext response) {
        response.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        response.getHeaders().putSingle("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE");
        response.getHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
    
    private boolean isAuth(HttpHeaders headers){
        List<String> heads = headers.getRequestHeader("Authorization");
        if(heads==null || heads.isEmpty())
            return false;
        String decoded = new String(Base64.getDecoder().decode(heads.get(0).getBytes()));
        String[] split = decoded.split(":");
        String nif = split[0];
        String pass = split[1];
        Empleado emp = empleadoFacade.find(nif);
        if(emp==null)
            return false;
        if(!emp.getUsuario().getPassword().equals(pass))
            return false;
        return true;
    }

    /**
     * Retrieves representation of an instance of dbcs.rest.ConfiguracionResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public Response getCatalogo(@Context HttpHeaders headers) {
        try{
            if(!isAuth(headers))
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(getMessage(ERR_AUTH))
                        .build();
            return Response.status(Response.Status.OK)
                    .entity(confF.findAll().toArray(new Configuracionpc[0]))
                    .build();
        }catch(Exception e){
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(getMessage(SERV_ERR))
                    .build();
        }
    }
    
    private String getMessage(String message){
        return "{\"message\" : \""+message+"\"}";
    }
    
    @DELETE
    @Path("{idconfig}")
    @Produces("application/json")
    public Response deleteConfig(@PathParam("idconfig") Integer idConf, @Context HttpHeaders headers){
        try{
            if(!isAuth(headers))
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(getMessage(ERR_AUTH))
                        .build();
            if(confF.removeConfig(idConf))
                return Response
                        .status(Response.Status.OK)
                        .entity(getMessage(CONF_BORR_OK))
                        .build();
            else
                return Response
                        .status(Response.Status.FORBIDDEN)
                        .entity(getMessage(CONF_BORR_ERROR))
                        .build();
        }catch(Exception e){
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(getMessage(SERV_ERR))
                    .build();
        }
    }

    
    @POST
    @Produces("application/json")
    public Response postConfig(JsonObject conf, @Context HttpHeaders headers){
        try{
            if(!isAuth(headers))
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(getMessage(ERR_AUTH))
                        .build();
            if(!(conf.containsKey("velocidadcpu") && conf.containsKey("capacidadram") &&
                    conf.containsKey("capacidaddd") && conf.containsKey("velocidadtarjetagrafica") &&
                    conf.containsKey("memoriatarjetagrafica") && conf.containsKey("tipocpu"))){
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(getMessage(CONF_ADD_MISSING))
                        .build();
            }
            try{
                int velCPU = conf.getInt("velocidadcpu");
                int capRAM = conf.getInt("capacidadram");
                int capDD = conf.getInt("capacidaddd");
                int velTarGraf = conf.getInt("velocidadtarjetagrafica");
                int memTarGraf = conf.getInt("memoriatarjetagrafica");
                String tipoCPU = conf.getString("tipocpu");
                float precio = conf.containsKey("precio") ? (float)conf.getJsonNumber("precio").doubleValue(): 0f;
                if(confF.addConfiguracion(velCPU, capRAM, capDD, velTarGraf, memTarGraf, tipoCPU, precio))
                    return Response.status(Response.Status.OK)
                            .entity(getMessage(CONF_ADD_OK))
                            .build();
                else
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(getMessage(CONF_ADD_FAIL))//?? no deberia suceder este creo, que escriba en log?
                            .build();
            }catch(ClassCastException cce){
                Logger.getLogger(getClass().getName()).log(Level.WARNING, "exception caught", cce);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(getMessage(CONF_ADD_WRONG))
                        .build();
            }
        }catch(Exception e){
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(getMessage(SERV_ERR))
                    .build();
        }
    }
    
    
    /**
     * PUT method for updating or creating an instance of ConfiguracionResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Path("{idConfig}")
    @Consumes("application/json")
    public Response modificarConfiguracion(@PathParam("idConfig") Integer idConf, JsonObject conf, @Context HttpHeaders headers) {
        try{
            if(!isAuth(headers))
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(getMessage(ERR_AUTH))
                        .build();
            int idConfiguracion = idConf;
            try{
                int velCPU = getIntDefault("velocidadcpu", conf);
                int capRAM = getIntDefault("capacidadram", conf);
                int capDD = getIntDefault("capacidaddd", conf);
                int velTarGraf = getIntDefault("velocidadtarjetagrafica", conf);
                int memTarGraf = getIntDefault("memoriatarjetagrafica", conf);
                String tipoCPU = conf.containsKey("tipocpu") ? conf.getString("tipocpu") : null;
                float precio = conf.containsKey("precio") ? (float)conf.getJsonNumber("precio").doubleValue() : 0f;
                if(confF.editConfiguracion(idConfiguracion, velCPU, capRAM, capDD, velTarGraf, memTarGraf, tipoCPU, precio))
                    return Response.status(Response.Status.OK)
                            .entity(getMessage(CONF_EDIT_OK))
                            .build();
                else
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity(getMessage(CONF_EDIT_NOT_FOUND))
                            .build();
            }catch(ClassCastException cce){
                Logger.getLogger(getClass().getName()).log(Level.WARNING, "exception caught", cce);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(getMessage(CONF_ADD_WRONG))
                        .build();
            }
        }catch(Exception e){
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(getMessage(SERV_ERR))
                    .build();
        }
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
