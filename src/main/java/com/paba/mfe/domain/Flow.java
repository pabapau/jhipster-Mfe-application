package com.paba.mfe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.paba.mfe.domain.enumeration.BuildState;
import com.paba.mfe.domain.enumeration.FlowUseCase;
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
    @Column(name = "flow_use_case", nullable = false)
    private FlowUseCase flowUseCase;

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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "businessUnit", "accountedFors" }, allowSetters = true)
    private Site origin;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "businessUnit", "accountedFors" }, allowSetters = true)
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

    public FlowUseCase getFlowUseCase() {
        return this.flowUseCase;
    }

    public Flow flowUseCase(FlowUseCase flowUseCase) {
        this.setFlowUseCase(flowUseCase);
        return this;
    }

    public void setFlowUseCase(FlowUseCase flowUseCase) {
        this.flowUseCase = flowUseCase;
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

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public Flow creationDate(LocalDate creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getLastUpdated() {
        return this.lastUpdated;
    }

    public Flow lastUpdated(LocalDate lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public BuildState getBuildState() {
        return this.buildState;
    }

    public Flow buildState(BuildState buildState) {
        this.setBuildState(buildState);
        return this;
    }

    public void setBuildState(BuildState buildState) {
        this.buildState = buildState;
    }

    public Integer getBuildCount() {
        return this.buildCount;
    }

    public Flow buildCount(Integer buildCount) {
        this.setBuildCount(buildCount);
        return this;
    }

    public void setBuildCount(Integer buildCount) {
        this.buildCount = buildCount;
    }

    public String getBuildComment() {
        return this.buildComment;
    }

    public Flow buildComment(String buildComment) {
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

    public Flow businessUnit(BusinessUnit businessUnit) {
        this.setBusinessUnit(businessUnit);
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
            ", flowUseCase='" + getFlowUseCase() + "'" +
            ", description='" + getDescription() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", buildState='" + getBuildState() + "'" +
            ", buildCount=" + getBuildCount() +
            ", buildComment='" + getBuildComment() + "'" +
            "}";
    }
}
