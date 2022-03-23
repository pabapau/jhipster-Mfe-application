package com.paba.mfe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.paba.mfe.domain.enumeration.BuildState;
import com.paba.mfe.domain.enumeration.XProtType;
import com.paba.mfe.domain.enumeration.XRole;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A XProt.
 */
@Entity
@Table(name = "x_prot")
public class XProt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "xprot_type", nullable = false)
    private XProtType xprotType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "x_role", nullable = false)
    private XRole xRole;

    @Column(name = "comment")
    private String comment;

    @Column(name = "access_address")
    private String accessAddress;

    @Column(name = "access_service_point")
    private Integer accessServicePoint;

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

    @ManyToOne
    @JsonIgnoreProperties(value = { "businessUnit", "accountedFors" }, allowSetters = true)
    private Site onNode;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public XProt id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public XProtType getXprotType() {
        return this.xprotType;
    }

    public XProt xprotType(XProtType xprotType) {
        this.setXprotType(xprotType);
        return this;
    }

    public void setXprotType(XProtType xprotType) {
        this.xprotType = xprotType;
    }

    public XRole getxRole() {
        return this.xRole;
    }

    public XProt xRole(XRole xRole) {
        this.setxRole(xRole);
        return this;
    }

    public void setxRole(XRole xRole) {
        this.xRole = xRole;
    }

    public String getComment() {
        return this.comment;
    }

    public XProt comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAccessAddress() {
        return this.accessAddress;
    }

    public XProt accessAddress(String accessAddress) {
        this.setAccessAddress(accessAddress);
        return this;
    }

    public void setAccessAddress(String accessAddress) {
        this.accessAddress = accessAddress;
    }

    public Integer getAccessServicePoint() {
        return this.accessServicePoint;
    }

    public XProt accessServicePoint(Integer accessServicePoint) {
        this.setAccessServicePoint(accessServicePoint);
        return this;
    }

    public void setAccessServicePoint(Integer accessServicePoint) {
        this.accessServicePoint = accessServicePoint;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public XProt creationDate(LocalDate creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getLastUpdated() {
        return this.lastUpdated;
    }

    public XProt lastUpdated(LocalDate lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public BuildState getBuildState() {
        return this.buildState;
    }

    public XProt buildState(BuildState buildState) {
        this.setBuildState(buildState);
        return this;
    }

    public void setBuildState(BuildState buildState) {
        this.buildState = buildState;
    }

    public Integer getBuildCount() {
        return this.buildCount;
    }

    public XProt buildCount(Integer buildCount) {
        this.setBuildCount(buildCount);
        return this;
    }

    public void setBuildCount(Integer buildCount) {
        this.buildCount = buildCount;
    }

    public String getBuildComment() {
        return this.buildComment;
    }

    public XProt buildComment(String buildComment) {
        this.setBuildComment(buildComment);
        return this;
    }

    public void setBuildComment(String buildComment) {
        this.buildComment = buildComment;
    }

    public Site getOnNode() {
        return this.onNode;
    }

    public void setOnNode(Site site) {
        this.onNode = site;
    }

    public XProt onNode(Site site) {
        this.setOnNode(site);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof XProt)) {
            return false;
        }
        return id != null && id.equals(((XProt) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "XProt{" +
            "id=" + getId() +
            ", xprotType='" + getXprotType() + "'" +
            ", xRole='" + getxRole() + "'" +
            ", comment='" + getComment() + "'" +
            ", accessAddress='" + getAccessAddress() + "'" +
            ", accessServicePoint=" + getAccessServicePoint() +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", buildState='" + getBuildState() + "'" +
            ", buildCount=" + getBuildCount() +
            ", buildComment='" + getBuildComment() + "'" +
            "}";
    }
}
