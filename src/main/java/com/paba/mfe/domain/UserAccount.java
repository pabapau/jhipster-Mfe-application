package com.paba.mfe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.paba.mfe.domain.enumeration.AccountType;
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
@Table(name = "user_account")
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(name = "comment")
    private String comment;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @NotNull
    @Column(name = "last_updated", nullable = false)
    private LocalDate lastUpdated;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToMany
    @JoinTable(
        name = "rel_user_account__business_unit",
        joinColumns = @JoinColumn(name = "user_account_id"),
        inverseJoinColumns = @JoinColumn(name = "business_unit_id")
    )
    @JsonIgnoreProperties(value = { "userAccounts" }, allowSetters = true)
    private Set<BusinessUnit> businessUnits = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_user_account__sites",
        joinColumns = @JoinColumn(name = "user_account_id"),
        inverseJoinColumns = @JoinColumn(name = "sites_id")
    )
    @JsonIgnoreProperties(value = { "businessUnit", "accountedFors" }, allowSetters = true)
    private Set<Site> sites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountType getAccountType() {
        return this.accountType;
    }

    public UserAccount accountType(AccountType accountType) {
        this.setAccountType(accountType);
        return this;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getComment() {
        return this.comment;
    }

    public UserAccount comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public UserAccount creationDate(LocalDate creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getLastUpdated() {
        return this.lastUpdated;
    }

    public UserAccount lastUpdated(LocalDate lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserAccount user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<BusinessUnit> getBusinessUnits() {
        return this.businessUnits;
    }

    public void setBusinessUnits(Set<BusinessUnit> businessUnits) {
        this.businessUnits = businessUnits;
    }

    public UserAccount businessUnits(Set<BusinessUnit> businessUnits) {
        this.setBusinessUnits(businessUnits);
        return this;
    }

    public UserAccount addBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnits.add(businessUnit);
        businessUnit.getUserAccounts().add(this);
        return this;
    }

    public UserAccount removeBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnits.remove(businessUnit);
        businessUnit.getUserAccounts().remove(this);
        return this;
    }

    public Set<Site> getSites() {
        return this.sites;
    }

    public void setSites(Set<Site> sites) {
        this.sites = sites;
    }

    public UserAccount sites(Set<Site> sites) {
        this.setSites(sites);
        return this;
    }

    public UserAccount addSites(Site site) {
        this.sites.add(site);
        site.getAccountedFors().add(this);
        return this;
    }

    public UserAccount removeSites(Site site) {
        this.sites.remove(site);
        site.getAccountedFors().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAccount)) {
            return false;
        }
        return id != null && id.equals(((UserAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAccount{" +
            "id=" + getId() +
            ", accountType='" + getAccountType() + "'" +
            ", comment='" + getComment() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
