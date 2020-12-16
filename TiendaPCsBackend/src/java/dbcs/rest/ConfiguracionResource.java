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
 * Recurso REST de Configuracion y Configuraciones.
 * 
 * @author Javier Gaton Herguedas y Javier Moro Garcia
 */
@Path("/configuracion")
public class ConfiguracionResource implements ContainerResponseFilter {
    EmpleadoFacadeLocal empleadoFacade = lookupEmpleadoFacadeLocal();
    ConfiguracionpcFacadeLocal confF = lookupConfiguracionpcFacadeLocal();

    @Context
    private UriInfo context;
    
    private static final String SERV_ERR="Error en el sistema al acceder a la configuracion";
    private static final String CONF_BORR_ERROR="Error al intentar borrar la configuracion seleccionada";
    private static final String CONF_ADD_MISSING="Faltan datos o los nombres de los parametros no son correctos";
    private static final String CONF_ADD_WRONG="Los tipos de datos pasados son incorrectos";
    private static final String CONF_ADD_FAIL="Configuracion no se pudo introducir";
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
    
    /*
     * Devuelve true si la cabecera de autorizacion es valida, false si no
     * Tiene que corresponder a la concatenacion de <nif>:<password> de cualquier empleado
     */
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
     * Devuelve todas las Configuracionpc que hay en la base de datos
     * @param headers Cabecera con la autentificacion
     * @return Respuesta con el codigo de operacion, si es exito devuelve un JsonARRAY con las configuraciones
     * y si es error un mensaje de error
     */
    @GET
    @Produces("application/json")
    public Response getCatalogo(@Context HttpHeaders headers) {
        try{
            if(!isAuth(headers))//Si no esta autenticado
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(getMessage(ERR_AUTH))
                        .build();
            return Response.status(Response.Status.OK)// Si si esta autenticado
                    .entity(confF.findAll().toArray(new Configuracionpc[0]))
                    .build();
        }catch(Exception e){ //En caso de que el servidor tenga un error al acceder
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(getMessage(SERV_ERR))
                    .build();
        }
    }
    
    /*
     * Transforma una cadena (String) en una cadena que sigue el formato json {message: <mensaje>}
     * Es para los outputs para informar en detalle del error
     */
    private String getMessage(String message){
        return "{\"message\" : \""+message+"\"}";
    }
    
    /**
     * Elimina una configuracion
     * @param idConf Id de la configuracion a eliminar
     * @param headers Cabecera con la autentificacion
     * @return Response con el codigo de operacion, y en caso de error el mensaje del error
     */
    @DELETE
    @Path("{idconfig}")
    @Produces("application/json")
    public Response deleteConfig(@PathParam("idconfig") Integer idConf, @Context HttpHeaders headers){
        try{
            if(!isAuth(headers))//Si no esta autenticado o no es valida la autenticacion
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(getMessage(ERR_AUTH))
                        .build();
            // Si si que esta autenticado
            if(confF.removeConfig(idConf))//Si consigue eliminar con exito la configuracion
                return Response
                        .status(Response.Status.OK)
                        .build();
            else // Si no consigue eliminar con exito la configuracion porque no existe ese id o por otra razon
                return Response
                        .status(Response.Status.FORBIDDEN)
                        .entity(getMessage(CONF_BORR_ERROR))
                        .build();
        }catch(Exception e){ // Si hay un error en el servidor
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(getMessage(SERV_ERR))
                    .build();
        }
    }

    /**
     * Recibe una configuracion del cliente y la guarda
     * @param conf Configuracion en formato JSON
     * @param headers Cabecera con autenticacion
     * @return Response con el codigo de operacion, si es exito la ubicacion de la configuracion y si no un mensaje de error
     */
    @POST
    @Produces("application/json")
    public Response postConfig(JsonObject conf, @Context HttpHeaders headers){
        try{
            if(!isAuth(headers)) //Si la autenticacion no es correcta
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(getMessage(ERR_AUTH))
                        .build();
            if(!(conf.containsKey("velocidadcpu") && conf.containsKey("capacidadram") &&
                    conf.containsKey("capacidaddd") && conf.containsKey("velocidadtarjetagrafica") &&
                    conf.containsKey("memoriatarjetagrafica") && conf.containsKey("tipocpu"))){
                // Si el objeto json no contiene los parametros necesarios
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
                int idConfig = confF.addConfiguracion(velCPU, capRAM, capDD, velTarGraf, memTarGraf, tipoCPU, precio);
                String id = Integer.toString(idConfig);
                if(idConfig!=-1)//Si se genera el objeto json devuelve su ubicacion
                    return Response.status(Response.Status.CREATED)
                            .entity("{\"Location\" : \"/configuracion/"+id+"\"}")
                            .build();
                else // Si no se puede generar el objeto json (la id corresponde a -1 entonces)
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(getMessage(CONF_ADD_FAIL))
                            .build();
            }catch(ClassCastException cce){ // Si hay un error en el formato de los datos de entrada
                Logger.getLogger(getClass().getName()).log(Level.WARNING, "exception caught", cce);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(getMessage(CONF_ADD_WRONG))
                        .build();
            }
        }catch(Exception e){ // Si hay un error en el servidor
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(getMessage(SERV_ERR))
                    .build();
        }
    }
    
    /**
     * Modifica una configuracion existente
     * @param idConf Id de la configuracion a modificar
     * @param conf Datos de la configuracion a cambiar. Lo que este como 0 o no este, significa mantener como estaba
     * @param headers Cabecera de autenticacion
     * @return Response con el codigo de operacion, si es un error tambien el mensaje de error
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
    
    /*
    * Metodo para que si en un objeto json no hay cierta clave, devuelve un 0 en su lugar
    */
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
