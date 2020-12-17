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
@Table(name = "words")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Words.findAll", query = "SELECT w FROM Words w")
})
public class Words implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected WordsPK wordsPK;
    @Basic(optional = false)
    @Column(name = "wordtext")
    private String wordtext;
    @Basic(optional = false)
    @Column(name = "firstchar")
    private String firstchar;
    @JoinColumns({
        @JoinColumn(name = "maintext_parva_id", referencedColumnName = "parva_id", insertable = false, updatable = false),
        @JoinColumn(name = "maintext_adhyayid", referencedColumnName = "adhyayid", insertable = false, updatable = false),
        @JoinColumn(name = "maintext_shlokanum", referencedColumnName = "shlokanum", insertable = false, updatable = false),
        @JoinColumn(name = "maintext_shlokaline", referencedColumnName = "shlokaline", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Maintext maintext;

    public Words() {
    }

    public Words(WordsPK wordsPK) {
        this.wordsPK = wordsPK;
    }

    public Words(WordsPK wordsPK, String wordtext, String firstchar) {
        this.wordsPK = wordsPK;
        this.wordtext = wordtext;
        this.firstchar = firstchar;
    }

    public Words(int maintextParvaId, int maintextAdhyayid, int maintextShlokanum, int maintextShlokaline, int wordnum) {
        this.wordsPK = new WordsPK(maintextParvaId, maintextAdhyayid, maintextShlokanum, maintextShlokaline, wordnum);
    }

    public WordsPK getWordsPK() {
        return wordsPK;
    }

    public void setWordsPK(WordsPK wordsPK) {
        this.wordsPK = wordsPK;
    }

    public String getWordtext() {
        return wordtext;
    }

    public void setWordtext(String wordtext) {
        this.wordtext = wordtext;
    }

    public String getFirstchar() {
        return firstchar;
    }

    public void setFirstchar(String firstchar) {
        this.firstchar = firstchar;
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
        hash += (wordsPK != null ? wordsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Words)) {
            return false;
        }
        Words other = (Words) object;
        if ((this.wordsPK == null && other.wordsPK != null) || (this.wordsPK != null && !this.wordsPK.equals(other.wordsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dgrf.ksamancore.db.entities.Words[ wordsPK=" + wordsPK + " ]";
    }
    
}
