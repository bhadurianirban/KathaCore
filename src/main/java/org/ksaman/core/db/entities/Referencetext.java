/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ksaman.core.db.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dgrfiv
 */
@Entity
@Table(name = "referencetext")
@XmlRootElement
@NamedQueries({
@NamedQuery(name = "Referencetext.findAll", query = "SELECT r FROM Referencetext r")})
public class Referencetext implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ReferencetextPK referencetextPK;
    @Basic(optional = false)
    @Lob
    @Column(name = "text")
    private String text;
    @Column(name = "referencetextpos")
    private Integer referencetextpos;
    @JoinColumns({
        @JoinColumn(name = "maintext_parva_id", referencedColumnName = "parva_id", insertable = false, updatable = false),
        @JoinColumn(name = "maintext_adhyayid", referencedColumnName = "adhyayid", insertable = false, updatable = false),
        @JoinColumn(name = "maintext_shlokanum", referencedColumnName = "shlokanum", insertable = false, updatable = false),
        @JoinColumn(name = "maintext_shlokaline", referencedColumnName = "shlokaline", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Maintext maintext;

    public Referencetext() {
    }

    public Referencetext(ReferencetextPK referencetextPK) {
        this.referencetextPK = referencetextPK;
    }

    public Referencetext(ReferencetextPK referencetextPK, String text) {
        this.referencetextPK = referencetextPK;
        this.text = text;
    }

    public Referencetext(int maintextParvaId, int maintextAdhyayid, int maintextShlokanum, int maintextShlokaline, int reftextid) {
        this.referencetextPK = new ReferencetextPK(maintextParvaId, maintextAdhyayid, maintextShlokanum, maintextShlokaline, reftextid);
    }

    public ReferencetextPK getReferencetextPK() {
        return referencetextPK;
    }

    public void setReferencetextPK(ReferencetextPK referencetextPK) {
        this.referencetextPK = referencetextPK;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getReferencetextpos() {
        return referencetextpos;
    }

    public void setReferencetextpos(Integer referencetextpos) {
        this.referencetextpos = referencetextpos;
    }

    public Maintext getMaintext() {
        return maintext;
    }

    public void setMaintext(Maintext maintext) {
        this.maintext = maintext;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (referencetextPK != null ? referencetextPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Referencetext)) {
            return false;
        }
        Referencetext other = (Referencetext) object;
        if ((this.referencetextPK == null && other.referencetextPK != null) || (this.referencetextPK != null && !this.referencetextPK.equals(other.referencetextPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dgrf.ksamancore.db.entities.Referencetext[ referencetextPK=" + referencetextPK + " ]";
    }
    
}
