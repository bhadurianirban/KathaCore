/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ksaman.core.db.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author dgrfiv
 */
@Entity
@Table(name = "ubacha")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ubacha.findAll", query = "SELECT u FROM Ubacha u")})
public class Ubacha implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "bachan")
    private String bachan;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ubachaId")
    private List<Maintext> maintextList;

    public Ubacha() {
    }

    public Ubacha(Integer id) {
        this.id = id;
    }

    public Ubacha(Integer id, String name, String bachan) {
        this.id = id;
        this.name = name;
        this.bachan = bachan;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBachan() {
        return bachan;
    }

    public void setBachan(String bachan) {
        this.bachan = bachan;
    }

    @XmlTransient
    public List<Maintext> getMaintextList() {
        return maintextList;
    }

    public void setMaintextList(List<Maintext> maintextList) {
        this.maintextList = maintextList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ubacha)) {
            return false;
        }
        Ubacha other = (Ubacha) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dgrf.ksamancore.db.entities.Ubacha[ id=" + id + " ]";
    }
    
}
