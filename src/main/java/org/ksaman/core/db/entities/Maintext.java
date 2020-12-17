/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ksaman.core.db.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author dgrfiv
 */
@Entity
@Table(name = "maintext")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Maintext.findAll", query = "SELECT m FROM Maintext m")
})
public class Maintext implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MaintextPK maintextPK;
    @Basic(optional = false)
    @Lob
    @Column(name = "shlokatext")
    private String shlokatext;
    @Basic(optional = false)
    @Column(name = "firstchar")
    private String firstchar;
    @Basic(optional = false)
    @Column(name = "endchar")
    private String endchar;
    @Lob
    @Column(name = "translatedtext")
    private String translatedtext;
    @Basic(optional = false)
    @Column(name = "lastupdatedts")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastupdatedts;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "maintext")
    private List<Referencetext> referencetextList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "maintext", orphanRemoval=true)
    private List<Words> wordsList;
    @JoinColumn(name = "parva_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Parva parva;
    @JoinColumn(name = "ubacha_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Ubacha ubachaId;

    public Maintext() {
    }

    public Maintext(MaintextPK maintextPK) {
        this.maintextPK = maintextPK;
    }

    public Maintext(MaintextPK maintextPK, String shlokatext, String firstchar, String endchar, Date lastupdatedts) {
        this.maintextPK = maintextPK;
        this.shlokatext = shlokatext;
        this.firstchar = firstchar;
        this.endchar = endchar;
        this.lastupdatedts = lastupdatedts;
    }

    public Maintext(int parvaId, int adhyayid, int shlokanum, int shlokaline) {
        this.maintextPK = new MaintextPK(parvaId, adhyayid, shlokanum, shlokaline);
    }

    public MaintextPK getMaintextPK() {
        return maintextPK;
    }

    public void setMaintextPK(MaintextPK maintextPK) {
        this.maintextPK = maintextPK;
    }

    public String getShlokatext() {
        return shlokatext;
    }

    public void setShlokatext(String shlokatext) {
        this.shlokatext = shlokatext;
    }

    public String getFirstchar() {
        return firstchar;
    }

    public void setFirstchar(String firstchar) {
        this.firstchar = firstchar;
    }

    public String getEndchar() {
        return endchar;
    }

    public void setEndchar(String endchar) {
        this.endchar = endchar;
    }

    public String getTranslatedtext() {
        return translatedtext;
    }

    public void setTranslatedtext(String translatedtext) {
        this.translatedtext = translatedtext;
    }

    public Date getLastupdatedts() {
        return lastupdatedts;
    }

    public void setLastupdatedts(Date lastupdatedts) {
        this.lastupdatedts = lastupdatedts;
    }

    @XmlTransient
    public List<Referencetext> getReferencetextList() {
        return referencetextList;
    }

    public void setReferencetextList(List<Referencetext> referencetextList) {
        this.referencetextList = referencetextList;
    }

    @XmlTransient
    public List<Words> getWordsList() {
        return wordsList;
    }

    public void setWordsList(List<Words> wordsList) {
        this.wordsList = wordsList;
    }

    public Parva getParva() {
        return parva;
    }

    public void setParva(Parva parva) {
        this.parva = parva;
    }

    public Ubacha getUbachaId() {
        return ubachaId;
    }

    public void setUbachaId(Ubacha ubachaId) {
        this.ubachaId = ubachaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (maintextPK != null ? maintextPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Maintext)) {
            return false;
        }
        Maintext other = (Maintext) object;
        if ((this.maintextPK == null && other.maintextPK != null) || (this.maintextPK != null && !this.maintextPK.equals(other.maintextPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dgrf.ksamancore.db.entities.Maintext[ maintextPK=" + maintextPK + " ]";
    }
    
}
