package com.paba.mfe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.paba.mfe.domain.enumeration.Accounttype;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * More informations about users\nwithout changing nativ user \"entity
 */
@Schema(description = "More informations about users\nwithout changing nativ user \"entity")
@Entity
@Table(name = "useraccount")
public class Useraccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "accounttype", nullable = false)
    private Accounttype accounttype;

    @Column(name = "comment")
    private String comment;

    @NotNull
    @Column(name = "creationdate", nullable = false)
    private LocalDate creationdate;

    @NotNull
    @Column(name = "lastupdated", nullable = false)
    private LocalDate lastupdated;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToMany
    @JoinTable(
        name = "rel_useraccount__businessunit",
        joinColumns = @JoinColumn(name = "useraccount_id"),
        inverseJoinColumns = @JoinColumn(name = "businessunit_id")
    )
    @JsonIgnoreProperties(value = { "useraccounts" }, allowSetters = true)
    private Set<Businessunit> businessunits = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_useraccount__sites",
        joinColumns = @JoinColumn(name = "useraccount_id"),
        inverseJoinColumns = @JoinColumn(name = "sites_id")
    )
    @JsonIgnoreProperties(value = { "businessunit", "accountedfors" }, allowSetters = true)
    private Set<Site> sites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Useraccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Accounttype getAccounttype() {
        return this.accounttype;
    }

    public Useraccount accounttype(Accounttype accounttype) {
        this.setAccounttype(accounttype);
        return this;
    }

    public void setAccounttype(Accounttype accounttype) {
        this.accounttype = accounttype;
    }

    public String getComment() {
        return this.comment;
    }

    public Useraccount comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getCreationdate() {
        return this.creationdate;
    }

    public Useraccount creationdate(LocalDate creationdate) {
        this.setCreationdate(creationdate);
        return this;
    }

    public void setCreationdate(LocalDate creationdate) {
        this.creationdate = creationdate;
    }

    public LocalDate getLastupdated() {
        return this.lastupdated;
    }

    public Useraccount lastupdated(LocalDate lastupdated) {
        this.setLastupdated(lastupdated);
        return this;
    }

    public void setLastupdated(LocalDate lastupdated) {
        this.lastupdated = lastupdated;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Useraccount user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Businessunit> getBusinessunits() {
        return this.businessunits;
    }

    public void setBusinessunits(Set<Businessunit> businessunits) {
        this.businessunits = businessunits;
    }

    public Useraccount businessunits(Set<Businessunit> businessunits) {
        this.setBusinessunits(businessunits);
        return this;
    }

    public Useraccount addBusinessunit(Businessunit businessunit) {
        this.businessunits.add(businessunit);
        businessunit.getUseraccounts().add(this);
        return this;
    }

    public Useraccount removeBusinessunit(Businessunit businessunit) {
        this.businessunits.remove(businessunit);
        businessunit.getUseraccounts().remove(this);
        return this;
    }

    public Set<Site> getSites() {
        return this.sites;
    }

    public void setSites(Set<Site> sites) {
        this.sites = sites;
    }

    public Useraccount sites(Set<Site> sites) {
        this.setSites(sites);
        return this;
    }

    public Useraccount addSites(Site site) {
        this.sites.add(site);
        site.getAccountedfors().add(this);
        return this;
    }

    public Useraccount removeSites(Site site) {
        this.sites.remove(site);
        site.getAccountedfors().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Useraccount)) {
            return false;
        }
        return id != null && id.equals(((Useraccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Useraccount{" +
            "id=" + getId() +
            ", accounttype='" + getAccounttype() + "'" +
            ", comment='" + getComment() + "'" +
            ", creationdate='" + getCreationdate() + "'" +
            ", lastupdated='" + getLastupdated() + "'" +
            "}";
    }
}
