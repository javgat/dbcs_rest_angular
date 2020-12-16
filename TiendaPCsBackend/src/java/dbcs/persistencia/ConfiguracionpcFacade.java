/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcs.persistencia;

import dbcs.dominio.Configuracionpc;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Javier
 */
@Stateless
public class ConfiguracionpcFacade extends AbstractFacade<Configuracionpc> implements ConfiguracionpcFacadeLocal {
    @PersistenceContext(unitName = "TiendaPCsBackendPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConfiguracionpcFacade() {
        super(Configuracionpc.class);
    }
    
    @Override
    public boolean removeConfig(Integer idConf){
        Configuracionpc conf = find(idConf);
        if(conf==null)
            return false;
        remove(conf);
        return true;
    }

    @Override
    public Boolean editConfiguracion(int idConfiguracion, int velCPU, int capRAM,
            int capDD, int velTarGraf, int memTarGraf, String tipoCPU, float precio) {
        Configuracionpc conf = find(idConfiguracion);
        if(velCPU<0 || capRAM<0 || capDD<0 || velTarGraf<0 || memTarGraf < 0 || precio <0)
            return false;
        //se comprueba que la configuracion asociada al identificador dado existe
        if(conf!=null){
            //si el valor es 0 no se modifica en la base de datos
            if(velCPU!=0)
                conf.setVelocidadcpu(velCPU);
            if(capRAM!=0)
                conf.setCapacidadram(capRAM);
            if(capDD!=0)
                conf.setCapacidaddd(capDD);
            if(velTarGraf!=0)
                conf.setVelocidadtarjetagrafica(velTarGraf);
            if(memTarGraf!=0)
                conf.setMemoriatarjetagrafica(memTarGraf);
            if(tipoCPU!=null && !tipoCPU.equals(""))
                conf.setTipocpu(tipoCPU);
            if(precio!=0)
                conf.setPrecio(precio);
            try{
                edit(conf);
            }catch(Exception e){
                return false;
            }
            return true;
        }
        return false;
    }
    
    /*
    * Metodo que obtiene el nuevo identificador para la base de datos
    */
    private Integer newIdConfig(){
        List<Configuracionpc> cat = findAll();
        int id = 0;
        //Busqueda de un identificador valido para la base de datos
        for(Configuracionpc c:cat){
            if (id <= c.getIdconfiguracion())
                id=c.getIdconfiguracion()+1;
        }
        return id;
    }
    
    /**
     * 
     * @param velCPU
     * @param capRAM
     * @param capDD
     * @param velTarGraf
     * @param memTarGraf
     * @param tipoCPU
     * @param precio
     * @return El idConfiguracion asociado, o -1 en caso de no poder realizarlo
     */
    @Override
    public int addConfiguracion(int velCPU, int capRAM, int capDD, int velTarGraf,
            int memTarGraf, String tipoCPU, float precio) {
        Integer idConfig = newIdConfig();
        Configuracionpc conf = new Configuracionpc(idConfig);
        conf.setVelocidadcpu(velCPU);
        conf.setCapacidadram(capRAM);
        conf.setCapacidaddd(capDD);
        conf.setVelocidadtarjetagrafica(velTarGraf);
        conf.setMemoriatarjetagrafica(memTarGraf);
        conf.setTipocpu(tipoCPU);
        conf.setPrecio(precio);

        if(velCPU<0 || capRAM<0 || capDD<0 || velTarGraf<0 || memTarGraf < 0 ||
                tipoCPU==null || tipoCPU.equals("") || precio <0)
            return -1;
        try{
            create(conf);
        }catch(Exception e){
            return -1;
        }
        return idConfig;
    }
}
