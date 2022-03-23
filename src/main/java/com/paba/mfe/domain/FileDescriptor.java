package com.paba.mfe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.paba.mfe.domain.enumeration.BuildState;
import com.paba.mfe.domain.enumeration.FlowUseCase;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A FileDescriptor.
 */
@Entity
@Table(name = "file_descriptor")
public class FileDescriptor implements Serializable {

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

    @JsonIgnoreProperties(
        value = { "originFileDescriptor", "destFileDescriptor", "businessUnit", "origin", "destination", "fileDescriptor" },
        allowSetters = true
    )
    @OneToOne
    @JoinColumn(unique = true)
    private Flow flow;

    @JsonIgnoreProperties(
        value = { "originFileDescriptor", "destFileDescriptor", "businessUnit", "origin", "destination", "fileDescriptor" },
        allowSetters = true
    )
    @OneToOne(mappedBy = "originFileDescriptor")
    private Flow isSourceFor;

    @JsonIgnoreProperties(
        value = { "originFileDescriptor", "destFileDescriptor", "businessUnit", "origin", "destination", "fileDescriptor" },
        allowSetters = true
    )
    @OneToOne(mappedBy = "destFileDescriptor")
    private Flow isDestFor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FileDescriptor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileIdent() {
        return this.fileIdent;
    }

    public FileDescriptor fileIdent(String fileIdent) {
        this.setFileIdent(fileIdent);
        return this;
    }

    public void setFileIdent(String fileIdent) {
        this.fileIdent = fileIdent;
    }

    public FlowUseCase getFlowUseCase() {
        return this.flowUseCase;
    }

    public FileDescriptor flowUseCase(FlowUseCase flowUseCase) {
        this.setFlowUseCase(flowUseCase);
        return this;
    }

    public void setFlowUseCase(FlowUseCase flowUseCase) {
        this.flowUseCase = flowUseCase;
    }

    public String getDescription() {
        return this.description;
    }

    public FileDescriptor description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public FileDescriptor creationDate(LocalDate creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getLastUpdated() {
        return this.lastUpdated;
    }

    public FileDescriptor lastUpdated(LocalDate lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public BuildState getBuildState() {
        return this.buildState;
    }

    public FileDescriptor buildState(BuildState buildState) {
        this.setBuildState(buildState);
        return this;
    }

    public void setBuildState(BuildState buildState) {
        this.buildState = buildState;
    }

    public Integer getBuildCount() {
        return this.buildCount;
    }

    public FileDescriptor buildCount(Integer buildCount) {
        this.setBuildCount(buildCount);
        return this;
    }

    public void setBuildCount(Integer buildCount) {
        this.buildCount = buildCount;
    }

    public String getBuildComment() {
        return this.buildComment;
    }

    public FileDescriptor buildComment(String buildComment) {
        this.setBuildComment(buildComment);
        return this;
    }

    public void setBuildComment(String buildComment) {
        this.buildComment = buildComment;
    }

    public Flow getFlow() {
        return this.flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    public FileDescriptor flow(Flow flow) {
        this.setFlow(flow);
        return this;
    }

    public Flow getIsSourceFor() {
        return this.isSourceFor;
    }

    public void setIsSourceFor(Flow flow) {
        if (this.isSourceFor != null) {
            this.isSourceFor.setOriginFileDescriptor(null);
        }
        if (flow != null) {
            flow.setOriginFileDescriptor(this);
        }
        this.isSourceFor = flow;
    }

    public FileDescriptor isSourceFor(Flow flow) {
        this.setIsSourceFor(flow);
        return this;
    }

    public Flow getIsDestFor() {
        return this.isDestFor;
    }

    public void setIsDestFor(Flow flow) {
        if (this.isDestFor != null) {
            this.isDestFor.setDestFileDescriptor(null);
        }
        if (flow != null) {
            flow.setDestFileDescriptor(this);
        }
        this.isDestFor = flow;
    }

    public FileDescriptor isDestFor(Flow flow) {
        this.setIsDestFor(flow);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileDescriptor)) {
            return false;
        }
        return id != null && id.equals(((FileDescriptor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileDescriptor{" +
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
