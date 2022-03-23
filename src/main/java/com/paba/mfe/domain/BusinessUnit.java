package com.paba.mfe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * list off the business entitys\nmanaged by this mfe application
 */
@Schema(description = "list off the business entitys\nmanaged by this mfe application")
@Entity
@Table(name = "business_unit")
public class BusinessUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "businessUnits")
    @JsonIgnoreProperties(value = { "user", "businessUnits", "sites" }, allowSetters = true)
    private Set<UserAccount> userAccounts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BusinessUnit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public BusinessUnit code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public BusinessUnit name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public BusinessUnit description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<UserAccount> getUserAccounts() {
        return this.userAccounts;
    }

    public void setUserAccounts(Set<UserAccount> userAccounts) {
        if (this.userAccounts != null) {
            this.userAccounts.forEach(i -> i.removeBusinessUnit(this));
        }
        if (userAccounts != null) {
            userAccounts.forEach(i -> i.addBusinessUnit(this));
        }
        this.userAccounts = userAccounts;
    }

    public BusinessUnit userAccounts(Set<UserAccount> userAccounts) {
        this.setUserAccounts(userAccounts);
        return this;
    }

    public BusinessUnit addUserAccount(UserAccount userAccount) {
        this.userAccounts.add(userAccount);
        userAccount.getBusinessUnits().add(this);
        return this;
    }

    public BusinessUnit removeUserAccount(UserAccount userAccount) {
        this.userAccounts.remove(userAccount);
        userAccount.getBusinessUnits().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessUnit)) {
            return false;
        }
        return id != null && id.equals(((BusinessUnit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessUnit{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
