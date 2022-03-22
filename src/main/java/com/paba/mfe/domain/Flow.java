package com.paba.mfe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.paba.mfe.domain.enumeration.Buildstate;
import com.paba.mfe.domain.enumeration.Flowusecase;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Flow.
 */
@Entity
@Table(name = "flow")
public class Flow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "file_ident", nullable = false)
    private String fileIdent;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "flowusecase", nullable = false)
    private Flowusecase flowusecase;

    @Column(name = "description")
    private String description;

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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "businessunit", "accountedfors" }, allowSetters = true)
    private Site origin;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "businessunit", "accountedfors" }, allowSetters = true)
    private Site destination;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Flow id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileIdent() {
        return this.fileIdent;
    }

    public Flow fileIdent(String fileIdent) {
        this.setFileIdent(fileIdent);
        return this;
    }

    public void setFileIdent(String fileIdent) {
        this.fileIdent = fileIdent;
    }

    public Flowusecase getFlowusecase() {
        return this.flowusecase;
    }

    public Flow flowusecase(Flowusecase flowusecase) {
        this.setFlowusecase(flowusecase);
        return this;
    }

    public void setFlowusecase(Flowusecase flowusecase) {
        this.flowusecase = flowusecase;
    }

    public String getDescription() {
        return this.description;
    }

    public Flow description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreationdate() {
        return this.creationdate;
    }

    public Flow creationdate(LocalDate creationdate) {
        this.setCreationdate(creationdate);
        return this;
    }

    public void setCreationdate(LocalDate creationdate) {
        this.creationdate = creationdate;
    }

    public LocalDate getLastupdated() {
        return this.lastupdated;
    }

    public Flow lastupdated(LocalDate lastupdated) {
        this.setLastupdated(lastupdated);
        return this;
    }

    public void setLastupdated(LocalDate lastupdated) {
        this.lastupdated = lastupdated;
    }

    public Buildstate getBuildstate() {
        return this.buildstate;
    }

    public Flow buildstate(Buildstate buildstate) {
        this.setBuildstate(buildstate);
        return this;
    }

    public void setBuildstate(Buildstate buildstate) {
        this.buildstate = buildstate;
    }

    public Integer getBuildcount() {
        return this.buildcount;
    }

    public Flow buildcount(Integer buildcount) {
        this.setBuildcount(buildcount);
        return this;
    }

    public void setBuildcount(Integer buildcount) {
        this.buildcount = buildcount;
    }

    public String getBuildcomment() {
        return this.buildcomment;
    }

    public Flow buildcomment(String buildcomment) {
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

    public Flow businessunit(Businessunit businessunit) {
        this.setBusinessunit(businessunit);
        return this;
    }

    public Site getOrigin() {
        return this.origin;
    }

    public void setOrigin(Site site) {
        this.origin = site;
    }

    public Flow origin(Site site) {
        this.setOrigin(site);
        return this;
    }

    public Site getDestination() {
        return this.destination;
    }

    public void setDestination(Site site) {
        this.destination = site;
    }

    public Flow destination(Site site) {
        this.setDestination(site);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Flow)) {
            return false;
        }
        return id != null && id.equals(((Flow) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Flow{" +
            "id=" + getId() +
            ", fileIdent='" + getFileIdent() + "'" +
            ", flowusecase='" + getFlowusecase() + "'" +
            ", description='" + getDescription() + "'" +
            ", creationdate='" + getCreationdate() + "'" +
            ", lastupdated='" + getLastupdated() + "'" +
            ", buildstate='" + getBuildstate() + "'" +
            ", buildcount=" + getBuildcount() +
            ", buildcomment='" + getBuildcomment() + "'" +
            "}";
    }
}
