/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcs.dominio;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Javier
 */
@Entity
@Table(name = "EMPRESA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empresa.findAll", query = "SELECT e FROM Empresa e")})
public class Empresa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "NIFCIF")
    private String nifcif;
    @Column(name = "ESCLIENTE")
    private Short escliente;
    @Column(name = "ESPROVEEDOR")
    private Short esproveedor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "encargadopor")
    private List<Pedidopc> pedidopcList;
    @JoinColumn(name = "NIFCIF", referencedColumnName = "NIFCIF", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Usuario usuario;

    public Empresa() {
    }

    public Empresa(String nifcif) {
        this.nifcif = nifcif;
    }

    public String getNifcif() {
        return nifcif;
    }

    public void setNifcif(String nifcif) {
        this.nifcif = nifcif;
    }

    public Short getEscliente() {
        return escliente;
    }

    public void setEscliente(Short escliente) {
        this.escliente = escliente;
    }

    public Short getEsproveedor() {
        return esproveedor;
    }

    public void setEsproveedor(Short esproveedor) {
        this.esproveedor = esproveedor;
    }

    @XmlTransient
    public List<Pedidopc> getPedidopcList() {
        return pedidopcList;
    }

    public void setPedidopcList(List<Pedidopc> pedidopcList) {
        this.pedidopcList = pedidopcList;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nifcif != null ? nifcif.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.nifcif == null && other.nifcif != null) || (this.nifcif != null && !this.nifcif.equals(other.nifcif))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dbcs.dominio.Empresa[ nifcif=" + nifcif + " ]";
    }
    
}
