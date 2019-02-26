package com.carlgira.soa.model.soa;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class FlowInfo implements Serializable {

    @Id
    @Column(name="CIKEY")
    private Long cikey;

    @Column(name="FLOW_ID")
    private BigDecimal flowid;

    @Column(name="ECID")
    private String ecid;

    @Column(name="COMPOSITE_NAME")
    private String name;

    @Column(name="COMPOSITE_REVISION")
    private String revision;

    @Column(name="COMPONENTTYPE")
    private String type;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @Column(name="CREATION_DATE")
    private Timestamp creationDate;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @Column(name="MODIFY_DATE")
    private Timestamp modifyDate;

    @Column(name="ACTIVE_COMPONENT_INSTANCES")
    private BigDecimal activeComponents;

    @Column(name="UNHANDLED_FAULTS")
    private BigDecimal unhandledFaults;

    @Column(name="RECOVERABLE_FAULTS")
    private BigDecimal recoverableFaults;

    public FlowInfo() {
    }

    public Long getCikey() {
        return cikey;
    }

    public void setCikey(Long cikey) {
        this.cikey = cikey;
    }

    public String getEcid() {
        return ecid;
    }

    public void setEcid(String ecid) {
        this.ecid = ecid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public BigDecimal getFlowid() {
        return flowid;
    }

    public void setFlowid(BigDecimal flowid) {
        this.flowid = flowid;
    }

    public BigDecimal getActiveComponents() {
        return activeComponents;
    }

    public void setActiveComponents(BigDecimal activeComponents) {
        this.activeComponents = activeComponents;
    }

    public BigDecimal getUnhandledFaults() {
        return unhandledFaults;
    }

    public void setUnhandledFaults(BigDecimal unhandledFaults) {
        this.unhandledFaults = unhandledFaults;
    }

    public BigDecimal getRecoverableFaults() {
        return recoverableFaults;
    }

    public void setRecoverableFaults(BigDecimal recoverableFaults) {
        this.recoverableFaults = recoverableFaults;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
