package com.carlgira.soa.model;

import java.util.Calendar;

public class BPMAuditEvent {

    private String label;
    private Calendar createTime;
    private String operation;
    private String componentType;
    private String flowElementType;
    private byte[] auditLog;
    private String auditInstanceType;
    private String activityName;

    public BPMAuditEvent() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getFlowElementType() {
        return flowElementType;
    }

    public void setFlowElementType(String flowElementType) {
        this.flowElementType = flowElementType;
    }

    public byte[] getAuditLog() {
        return auditLog;
    }

    public void setAuditLog(byte[] auditLog) {
        this.auditLog = auditLog;
    }

    public String getAuditInstanceType() {
        return auditInstanceType;
    }

    public void setAuditInstanceType(String auditInstanceType) {
        this.auditInstanceType = auditInstanceType;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
