/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcs.persistencia;

import dbcs.dominio.Empresa;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Javier
 */
@Stateless
public class EmpresaFacade extends AbstractFacade<Empresa> implements EmpresaFacadeLocal {
    @PersistenceContext(unitName = "TiendaPCsBackendPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmpresaFacade() {
        super(Empresa.class);
    }
    
}
