/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcs.persistencia;

import dbcs.dominio.Pedidopc;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Javier
 */
@Local
public interface PedidopcFacadeLocal {

    void create(Pedidopc pedidopc);

    void edit(Pedidopc pedidopc);

    void remove(Pedidopc pedidopc);

    Pedidopc find(Object id);

    List<Pedidopc> findAll();

    List<Pedidopc> findRange(int[] range);

    int count();
    
}
