/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domaci;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Rols
 */
@Entity
@Table(name = "reserved")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reserved.findAll", query = "SELECT r FROM Reserved r"),
    @NamedQuery(name = "Reserved.findByIduser", query = "SELECT r FROM Reserved r WHERE r.reservedPK.iduser = :iduser"),
    @NamedQuery(name = "Reserved.findByIdart", query = "SELECT r FROM Reserved r WHERE r.reservedPK.idart = :idart"),
    @NamedQuery(name = "Reserved.findByNumber", query = "SELECT r FROM Reserved r WHERE r.number = :number"),
    @NamedQuery(name = "Reserved.findByVaziDo", query = "SELECT r FROM Reserved r WHERE r.vaziDo = :vaziDo"),
@NamedQuery(name = "Reserved.findByIdartIduser", query = "SELECT r FROM Reserved r WHERE r.reservedPK.idart = :idart AND r.reservedPK.iduser = :iduser")
})
public class Reserved implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ReservedPK reservedPK;
    @Basic(optional = false)
    @Column(name = "number")
    private int number;
    @Basic(optional = false)
    @Column(name = "vaziDo")
    @Temporal(TemporalType.DATE)
    private Date vaziDo;
    @JoinColumn(name = "idart", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Articles articles;
    @JoinColumn(name = "iduser", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    public Reserved() {
    }

    public Reserved(ReservedPK reservedPK) {
        this.reservedPK = reservedPK;
    }

    public Reserved(ReservedPK reservedPK, int number, Date vaziDo) {
        this.reservedPK = reservedPK;
        this.number = number;
        this.vaziDo = vaziDo;
    }

    public Reserved(int iduser, int idart) {
        this.reservedPK = new ReservedPK(iduser, idart);
    }

    public ReservedPK getReservedPK() {
        return reservedPK;
    }

    public void setReservedPK(ReservedPK reservedPK) {
        this.reservedPK = reservedPK;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Date getVaziDo() {
        return vaziDo;
    }

    public void setVaziDo(Date vaziDo) {
        this.vaziDo = vaziDo;
    }

    public Articles getArticles() {
        return articles;
    }

    public void setArticles(Articles articles) {
        this.articles = articles;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservedPK != null ? reservedPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reserved)) {
            return false;
        }
        Reserved other = (Reserved) object;
        if ((this.reservedPK == null && other.reservedPK != null) || (this.reservedPK != null && !this.reservedPK.equals(other.reservedPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domaci.Reserved[ reservedPK=" + reservedPK + " ]";
    }
    
}
