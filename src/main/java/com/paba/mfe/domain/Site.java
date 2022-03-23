package com.paba.mfe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.paba.mfe.domain.enumeration.BuildState;
import com.paba.mfe.domain.enumeration.OsType;
import com.paba.mfe.domain.enumeration.SiteType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * sites are entry point whitch participate\nto file transfert exchanges (Mfe) and represent\napplications, physical transfert monitors,\ngroups of users, externals partners
 */
@Schema(
    description = "sites are entry point whitch participate\nto file transfert exchanges (Mfe) and represent\napplications, physical transfert monitors,\ngroups of users, externals partners"
)
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
    @Column(name = "site_type", nullable = false)
    private SiteType siteType;

    @Enumerated(EnumType.STRING)
    @Column(name = "os_type")
    private OsType osType;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @NotNull
    @Column(name = "last_updated", nullable = false)
    private LocalDate lastUpdated;

    @Enumerated(EnumType.STRING)
    @Column(name = "build_state")
    private BuildState buildState;

    @Column(name = "build_count")
    private Integer buildCount;

    @Column(name = "build_comment")
    private String buildComment;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "userAccounts" }, allowSetters = true)
    private BusinessUnit businessUnit;

    @ManyToMany(mappedBy = "sites")
    @JsonIgnoreProperties(value = { "user", "businessUnits", "sites" }, allowSetters = true)
    private Set<UserAccount> accountedFors = new HashSet<>();

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

    public SiteType getSiteType() {
        return this.siteType;
    }

    public Site siteType(SiteType siteType) {
        this.setSiteType(siteType);
        return this;
    }

    public void setSiteType(SiteType siteType) {
        this.siteType = siteType;
    }

    public OsType getOsType() {
        return this.osType;
    }

    public Site osType(OsType osType) {
        this.setOsType(osType);
        return this;
    }

    public void setOsType(OsType osType) {
        this.osType = osType;
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

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public Site creationDate(LocalDate creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getLastUpdated() {
        return this.lastUpdated;
    }

    public Site lastUpdated(LocalDate lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public BuildState getBuildState() {
        return this.buildState;
    }

    public Site buildState(BuildState buildState) {
        this.setBuildState(buildState);
        return this;
    }

    public void setBuildState(BuildState buildState) {
        this.buildState = buildState;
    }

    public Integer getBuildCount() {
        return this.buildCount;
    }

    public Site buildCount(Integer buildCount) {
        this.setBuildCount(buildCount);
        return this;
    }

    public void setBuildCount(Integer buildCount) {
        this.buildCount = buildCount;
    }

    public String getBuildComment() {
        return this.buildComment;
    }

    public Site buildComment(String buildComment) {
        this.setBuildComment(buildComment);
        return this;
    }

    public void setBuildComment(String buildComment) {
        this.buildComment = buildComment;
    }

    public BusinessUnit getBusinessUnit() {
        return this.businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    public Site businessUnit(BusinessUnit businessUnit) {
        this.setBusinessUnit(businessUnit);
        return this;
    }

    public Set<UserAccount> getAccountedFors() {
        return this.accountedFors;
    }

    public void setAccountedFors(Set<UserAccount> userAccounts) {
        if (this.accountedFors != null) {
            this.accountedFors.forEach(i -> i.removeSites(this));
        }
        if (userAccounts != null) {
            userAccounts.forEach(i -> i.addSites(this));
        }
        this.accountedFors = userAccounts;
    }

    public Site accountedFors(Set<UserAccount> userAccounts) {
        this.setAccountedFors(userAccounts);
        return this;
    }

    public Site addAccountedFor(UserAccount userAccount) {
        this.accountedFors.add(userAccount);
        userAccount.getSites().add(this);
        return this;
    }

    public Site removeAccountedFor(UserAccount userAccount) {
        this.accountedFors.remove(userAccount);
        userAccount.getSites().remove(this);
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
            ", siteType='" + getSiteType() + "'" +
            ", osType='" + getOsType() + "'" +
            ", description='" + getDescription() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", buildState='" + getBuildState() + "'" +
            ", buildCount=" + getBuildCount() +
            ", buildComment='" + getBuildComment() + "'" +
            "}";
    }
}
