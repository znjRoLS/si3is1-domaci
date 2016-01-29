/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domaci;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Rols
 */
@Embeddable
public class ReservedPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "iduser")
    private int iduser;
    @Basic(optional = false)
    @Column(name = "idart")
    private int idart;

    public ReservedPK() {
    }

    public ReservedPK(int iduser, int idart) {
        this.iduser = iduser;
        this.idart = idart;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public int getIdart() {
        return idart;
    }

    public void setIdart(int idart) {
        this.idart = idart;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) iduser;
        hash += (int) idart;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReservedPK)) {
            return false;
        }
        ReservedPK other = (ReservedPK) object;
        if (this.iduser != other.iduser) {
            return false;
        }
        if (this.idart != other.idart) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domaci.ReservedPK[ iduser=" + iduser + ", idart=" + idart + " ]";
    }
    
}
