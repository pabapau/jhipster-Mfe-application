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
@Table(name = "businessunit")
public class Businessunit implements Serializable {

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

    @ManyToMany(mappedBy = "businessunits")
    @JsonIgnoreProperties(value = { "user", "businessunits", "sites" }, allowSetters = true)
    private Set<Useraccount> useraccounts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Businessunit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Businessunit code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Businessunit name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Businessunit description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Useraccount> getUseraccounts() {
        return this.useraccounts;
    }

    public void setUseraccounts(Set<Useraccount> useraccounts) {
        if (this.useraccounts != null) {
            this.useraccounts.forEach(i -> i.removeBusinessunit(this));
        }
        if (useraccounts != null) {
            useraccounts.forEach(i -> i.addBusinessunit(this));
        }
        this.useraccounts = useraccounts;
    }

    public Businessunit useraccounts(Set<Useraccount> useraccounts) {
        this.setUseraccounts(useraccounts);
        return this;
    }

    public Businessunit addUseraccount(Useraccount useraccount) {
        this.useraccounts.add(useraccount);
        useraccount.getBusinessunits().add(this);
        return this;
    }

    public Businessunit removeUseraccount(Useraccount useraccount) {
        this.useraccounts.remove(useraccount);
        useraccount.getBusinessunits().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Businessunit)) {
            return false;
        }
        return id != null && id.equals(((Businessunit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Businessunit{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
