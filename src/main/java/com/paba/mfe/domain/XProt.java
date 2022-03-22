package com.paba.mfe.domain;

import com.paba.mfe.domain.enumeration.Buildstate;
import com.paba.mfe.domain.enumeration.XProttype;
import com.paba.mfe.domain.enumeration.Xrole;
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
    @Column(name = "xprottype", nullable = false)
    private XProttype xprottype;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "xrole", nullable = false)
    private Xrole xrole;

    @Column(name = "comment")
    private String comment;

    @Column(name = "access_address")
    private String accessAddress;

    @Column(name = "access_service_point")
    private Integer accessServicePoint;

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

    public XProttype getXprottype() {
        return this.xprottype;
    }

    public XProt xprottype(XProttype xprottype) {
        this.setXprottype(xprottype);
        return this;
    }

    public void setXprottype(XProttype xprottype) {
        this.xprottype = xprottype;
    }

    public Xrole getXrole() {
        return this.xrole;
    }

    public XProt xrole(Xrole xrole) {
        this.setXrole(xrole);
        return this;
    }

    public void setXrole(Xrole xrole) {
        this.xrole = xrole;
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

    public LocalDate getCreationdate() {
        return this.creationdate;
    }

    public XProt creationdate(LocalDate creationdate) {
        this.setCreationdate(creationdate);
        return this;
    }

    public void setCreationdate(LocalDate creationdate) {
        this.creationdate = creationdate;
    }

    public LocalDate getLastupdated() {
        return this.lastupdated;
    }

    public XProt lastupdated(LocalDate lastupdated) {
        this.setLastupdated(lastupdated);
        return this;
    }

    public void setLastupdated(LocalDate lastupdated) {
        this.lastupdated = lastupdated;
    }

    public Buildstate getBuildstate() {
        return this.buildstate;
    }

    public XProt buildstate(Buildstate buildstate) {
        this.setBuildstate(buildstate);
        return this;
    }

    public void setBuildstate(Buildstate buildstate) {
        this.buildstate = buildstate;
    }

    public Integer getBuildcount() {
        return this.buildcount;
    }

    public XProt buildcount(Integer buildcount) {
        this.setBuildcount(buildcount);
        return this;
    }

    public void setBuildcount(Integer buildcount) {
        this.buildcount = buildcount;
    }

    public String getBuildcomment() {
        return this.buildcomment;
    }

    public XProt buildcomment(String buildcomment) {
        this.setBuildcomment(buildcomment);
        return this;
    }

    public void setBuildcomment(String buildcomment) {
        this.buildcomment = buildcomment;
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
            ", xprottype='" + getXprottype() + "'" +
            ", xrole='" + getXrole() + "'" +
            ", comment='" + getComment() + "'" +
            ", accessAddress='" + getAccessAddress() + "'" +
            ", accessServicePoint=" + getAccessServicePoint() +
            ", creationdate='" + getCreationdate() + "'" +
            ", lastupdated='" + getLastupdated() + "'" +
            ", buildstate='" + getBuildstate() + "'" +
            ", buildcount=" + getBuildcount() +
            ", buildcomment='" + getBuildcomment() + "'" +
            "}";
    }
}
