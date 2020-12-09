/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcs.persistencia;

import dbcs.dominio.Configuracionpc;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Javier
 */
@Local
public interface ConfiguracionpcFacadeLocal {

    void create(Configuracionpc configuracionpc);

    void edit(Configuracionpc configuracionpc);

    void remove(Configuracionpc configuracionpc);

    Configuracionpc find(Object id);

    List<Configuracionpc> findAll();

    List<Configuracionpc> findRange(int[] range);

    int count();
    
    public boolean removeConfig(Integer idConf);
    
    public Boolean editConfiguracion(int idConfiguracion, int velCPU, int capRAM,
            int capDD, int velTarGraf, int memTarGraf, String tipoCPU, float precio);
    
    public Boolean addConfiguracion(int velCPU, int capRAM, int capDD, int velTarGraf,
            int memTarGraf, String tipoCPU, float precio);
    
}
