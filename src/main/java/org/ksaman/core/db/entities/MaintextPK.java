/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ksaman.core.db.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author dgrfiv
 */
@Embeddable
public class MaintextPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "parva_id")
    private int parvaId;
    @Basic(optional = false)
    @Column(name = "adhyayid")
    private int adhyayid;
    @Basic(optional = false)
    @Column(name = "shlokanum")
    private int shlokanum;
    @Basic(optional = false)
    @Column(name = "shlokaline")
    private int shlokaline;

    public MaintextPK() {
    }

    public MaintextPK(int parvaId, int adhyayid, int shlokanum, int shlokaline) {
        this.parvaId = parvaId;
        this.adhyayid = adhyayid;
        this.shlokanum = shlokanum;
        this.shlokaline = shlokaline;
    }

    public int getParvaId() {
        return parvaId;
    }

    public void setParvaId(int parvaId) {
        this.parvaId = parvaId;
    }

    public int getAdhyayid() {
        return adhyayid;
    }

    public void setAdhyayid(int adhyayid) {
        this.adhyayid = adhyayid;
    }

    public int getShlokanum() {
        return shlokanum;
    }

    public void setShlokanum(int shlokanum) {
        this.shlokanum = shlokanum;
    }

    public int getShlokaline() {
        return shlokaline;
    }

    public void setShlokaline(int shlokaline) {
        this.shlokaline = shlokaline;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) parvaId;
        hash += (int) adhyayid;
        hash += (int) shlokanum;
        hash += (int) shlokaline;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MaintextPK)) {
            return false;
        }
        MaintextPK other = (MaintextPK) object;
        if (this.parvaId != other.parvaId) {
            return false;
        }
        if (this.adhyayid != other.adhyayid) {
            return false;
        }
        if (this.shlokanum != other.shlokanum) {
            return false;
        }
        if (this.shlokaline != other.shlokaline) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dgrf.ksamancore.db.entities.MaintextPK[ parvaId=" + parvaId + ", adhyayid=" + adhyayid + ", shlokanum=" + shlokanum + ", shlokaline=" + shlokaline + " ]";
    }
    
}
