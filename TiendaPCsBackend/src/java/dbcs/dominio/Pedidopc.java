/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcs.dominio;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier
 */
@Entity
@Table(name = "PEDIDOPC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pedidopc.findAll", query = "SELECT p FROM Pedidopc p")})
public class Pedidopc implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IDPEDIDO")
    private Integer idpedido;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDADSOLICITADA")
    private int cantidadsolicitada;
    @JoinColumn(name = "CONFIGURACIONSOLICITADA", referencedColumnName = "IDCONFIGURACION")
    @ManyToOne(optional = false)
    private Configuracionpc configuracionsolicitada;
    @JoinColumn(name = "ENCARGADOPOR", referencedColumnName = "NIFCIF")
    @ManyToOne(optional = false)
    private Empresa encargadopor;
    @JoinColumn(name = "ESTADO", referencedColumnName = "IDESTADOVENTA")
    @ManyToOne(optional = false)
    private Estadoventapcs estado;

    public Pedidopc() {
    }

    public Pedidopc(Integer idpedido) {
        this.idpedido = idpedido;
    }

    public Pedidopc(Integer idpedido, int cantidadsolicitada) {
        this.idpedido = idpedido;
        this.cantidadsolicitada = cantidadsolicitada;
    }

    public Integer getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(Integer idpedido) {
        this.idpedido = idpedido;
    }

    public int getCantidadsolicitada() {
        return cantidadsolicitada;
    }

    public void setCantidadsolicitada(int cantidadsolicitada) {
        this.cantidadsolicitada = cantidadsolicitada;
    }

    public Configuracionpc getConfiguracionsolicitada() {
        return configuracionsolicitada;
    }

    public void setConfiguracionsolicitada(Configuracionpc configuracionsolicitada) {
        this.configuracionsolicitada = configuracionsolicitada;
    }

    public Empresa getEncargadopor() {
        return encargadopor;
    }

    public void setEncargadopor(Empresa encargadopor) {
        this.encargadopor = encargadopor;
    }

    public Estadoventapcs getEstado() {
        return estado;
    }

    public void setEstado(Estadoventapcs estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpedido != null ? idpedido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pedidopc)) {
            return false;
        }
        Pedidopc other = (Pedidopc) object;
        if ((this.idpedido == null && other.idpedido != null) || (this.idpedido != null && !this.idpedido.equals(other.idpedido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dbcs.dominio.Pedidopc[ idpedido=" + idpedido + " ]";
    }
    
}
