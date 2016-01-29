/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domaci;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Rols
 */
@Entity
@Table(name = "company")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Company.findAll", query = "SELECT c FROM Company c"),
    @NamedQuery(name = "Company.findById", query = "SELECT c FROM Company c WHERE c.id = :id"),
    @NamedQuery(name = "Company.findByName", query = "SELECT c FROM Company c WHERE c.name = :name"),
    @NamedQuery(name = "Company.findByPassword", query = "SELECT c FROM Company c WHERE c.password = :password"),
    @NamedQuery(name = "Company.findByEmail", query = "SELECT c FROM Company c WHERE c.email = :email"),
    @NamedQuery(name = "Company.findByTelephone", query = "SELECT c FROM Company c WHERE c.telephone = :telephone"),
    @NamedQuery(name = "Company.findByAddress", query = "SELECT c FROM Company c WHERE c.address = :address"),
    @NamedQuery(name = "Company.findByCity", query = "SELECT c FROM Company c WHERE c.city = :city"),
    @NamedQuery(name = "Company.findByCountry", query = "SELECT c FROM Company c WHERE c.country = :country"),
    @NamedQuery(name = "Company.findByFullcompanyname", query = "SELECT c FROM Company c WHERE c.fullcompanyname = :fullcompanyname"),
    @NamedQuery(name = "Company.findByPib", query = "SELECT c FROM Company c WHERE c.pib = :pib"),
    @NamedQuery(name = "Company.findByJbk", query = "SELECT c FROM Company c WHERE c.jbk = :jbk"),
    @NamedQuery(name = "Company.findByBrlk", query = "SELECT c FROM Company c WHERE c.brlk = :brlk"),
@NamedQuery(name = "Company.findByNamePassword", query = "SELECT u FROM Company u WHERE u.name = :name AND u.password = :password")
})
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "address")
    private String address;
    @Column(name = "city")
    private String city;
    @Column(name = "country")
    private String country;
    @Column(name = "fullcompanyname")
    private String fullcompanyname;
    @Column(name = "PIB")
    private String pib;
    @Column(name = "JBK")
    private String jbk;
    @Column(name = "BRLK")
    private String brlk;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idcompany")
    private Collection<Articles> articlesCollection;

    public Company() {
    }

    public Company(Integer id) {
        this.id = id;
    }

    public Company(Integer id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFullcompanyname() {
        return fullcompanyname;
    }

    public void setFullcompanyname(String fullcompanyname) {
        this.fullcompanyname = fullcompanyname;
    }

    public String getPib() {
        return pib;
    }

    public void setPib(String pib) {
        this.pib = pib;
    }

    public String getJbk() {
        return jbk;
    }

    public void setJbk(String jbk) {
        this.jbk = jbk;
    }

    public String getBrlk() {
        return brlk;
    }

    public void setBrlk(String brlk) {
        this.brlk = brlk;
    }

    @XmlTransient
    public Collection<Articles> getArticlesCollection() {
        return articlesCollection;
    }

    public void setArticlesCollection(Collection<Articles> articlesCollection) {
        this.articlesCollection = articlesCollection;
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
        if (!(object instanceof Company)) {
            return false;
        }
        Company other = (Company) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domaci.Company[ id=" + id + " ]";
    }
    
}
