package com.paba.mfe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.paba.mfe.domain.enumeration.Buildstate;
import com.paba.mfe.domain.enumeration.Sitetype;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Site.
 */
@Entity
@Table(name = "site")
public class Site implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sitetype", nullable = false)
    private Sitetype sitetype;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "sitenode", nullable = false)
    private String sitenode;

    @NotNull
    @Column(name = "creationdate", nullable = false)
    private LocalDate creationdate;

    @NotNull
    @Column(name = "lastupdated", nullable = false)
    private LocalDate lastupdated;

    @Enumerated(EnumType.STRING)
    @Column(name = "buildstate")
    private Buildstate buildstate;

    @Column(name = "buildcount")
    private Integer buildcount;

    @Column(name = "buildcomment")
    private String buildcomment;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "useraccounts" }, allowSetters = true)
    private Businessunit businessunit;

    @ManyToMany(mappedBy = "sites")
    @JsonIgnoreProperties(value = { "user", "businessunits", "sites" }, allowSetters = true)
    private Set<Useraccount> accountedfors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Site id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Site name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sitetype getSitetype() {
        return this.sitetype;
    }

    public Site sitetype(Sitetype sitetype) {
        this.setSitetype(sitetype);
        return this;
    }

    public void setSitetype(Sitetype sitetype) {
        this.sitetype = sitetype;
    }

    public String getDescription() {
        return this.description;
    }

    public Site description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSitenode() {
        return this.sitenode;
    }

    public Site sitenode(String sitenode) {
        this.setSitenode(sitenode);
        return this;
    }

    public void setSitenode(String sitenode) {
        this.sitenode = sitenode;
    }

    public LocalDate getCreationdate() {
        return this.creationdate;
    }

    public Site creationdate(LocalDate creationdate) {
        this.setCreationdate(creationdate);
        return this;
    }

    public void setCreationdate(LocalDate creationdate) {
        this.creationdate = creationdate;
    }

    public LocalDate getLastupdated() {
        return this.lastupdated;
    }

    public Site lastupdated(LocalDate lastupdated) {
        this.setLastupdated(lastupdated);
        return this;
    }

    public void setLastupdated(LocalDate lastupdated) {
        this.lastupdated = lastupdated;
    }

    public Buildstate getBuildstate() {
        return this.buildstate;
    }

    public Site buildstate(Buildstate buildstate) {
        this.setBuildstate(buildstate);
        return this;
    }

    public void setBuildstate(Buildstate buildstate) {
        this.buildstate = buildstate;
    }

    public Integer getBuildcount() {
        return this.buildcount;
    }

    public Site buildcount(Integer buildcount) {
        this.setBuildcount(buildcount);
        return this;
    }

    public void setBuildcount(Integer buildcount) {
        this.buildcount = buildcount;
    }

    public String getBuildcomment() {
        return this.buildcomment;
    }

    public Site buildcomment(String buildcomment) {
        this.setBuildcomment(buildcomment);
        return this;
    }

    public void setBuildcomment(String buildcomment) {
        this.buildcomment = buildcomment;
    }

    public Businessunit getBusinessunit() {
        return this.businessunit;
    }

    public void setBusinessunit(Businessunit businessunit) {
        this.businessunit = businessunit;
    }

    public Site businessunit(Businessunit businessunit) {
        this.setBusinessunit(businessunit);
        return this;
    }

    public Set<Useraccount> getAccountedfors() {
        return this.accountedfors;
    }

    public void setAccountedfors(Set<Useraccount> useraccounts) {
        if (this.accountedfors != null) {
            this.accountedfors.forEach(i -> i.removeSites(this));
        }
        if (useraccounts != null) {
            useraccounts.forEach(i -> i.addSites(this));
        }
        this.accountedfors = useraccounts;
    }

    public Site accountedfors(Set<Useraccount> useraccounts) {
        this.setAccountedfors(useraccounts);
        return this;
    }

    public Site addAccountedfor(Useraccount useraccount) {
        this.accountedfors.add(useraccount);
        useraccount.getSites().add(this);
        return this;
    }

    public Site removeAccountedfor(Useraccount useraccount) {
        this.accountedfors.remove(useraccount);
        useraccount.getSites().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Site)) {
            return false;
        }
        return id != null && id.equals(((Site) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Site{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", sitetype='" + getSitetype() + "'" +
            ", description='" + getDescription() + "'" +
            ", sitenode='" + getSitenode() + "'" +
            ", creationdate='" + getCreationdate() + "'" +
            ", lastupdated='" + getLastupdated() + "'" +
            ", buildstate='" + getBuildstate() + "'" +
            ", buildcount=" + getBuildcount() +
            ", buildcomment='" + getBuildcomment() + "'" +
            "}";
    }
}
