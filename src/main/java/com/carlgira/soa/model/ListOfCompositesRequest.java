package com.carlgira.soa.model;

import java.util.*;

public class ListOfCompositesRequest {

    private String composite;
    private String partition;
    private String revision;
    private String state;
    private String fault;
    private Date startDate;
    private Date endDate;
    private List<Long> flowids;
    private List<Long> cikeys;
    private List<String> ecids;
    private String sensorName;
    private List<String> sensorValues;
    private Integer page;
    private Integer size;

    public ListOfCompositesRequest() {
        this.state = "all";
        this.fault = "all";
        this.page = 0;
        this.size = 100;
        this.endDate = new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        this.startDate = c.getTime();
    }

    public String getComposite() {
        return composite;
    }

    public void setComposite(String composite) {
        this.composite = composite;
    }

    public String getPartition() {
        return partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Long> getFlowids() {
        return flowids;
    }

    public void setFlowids(List<Long> flowids) {
        this.flowids = flowids;
    }

    public List<Long> getCikeys() {
        return cikeys;
    }

    public void setCikeys(List<Long> cikeys) {
        this.cikeys = cikeys;
    }

    public List<String> getEcids() {
        return ecids;
    }

    public void setEcids(List<String> ecids) {
        this.ecids = ecids;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFault() {
        return fault;
    }

    public void setFault(String fault) {
        this.fault = fault;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public List<String> getSensorValues() {
        return sensorValues;
    }

    public void setSensorValues(List<String> sensorValues) {
        this.sensorValues = sensorValues;
    }

    @Override
    public String toString() {
        return "ListOfCompositesRequest{" +
                "composite='" + composite + '\'' +
                ", partition='" + partition + '\'' +
                ", revision='" + revision + '\'' +
                ", state='" + state + '\'' +
                ", fault='" + fault + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", flowids=" + flowids +
                ", cikeys=" + cikeys +
                ", ecids=" + ecids +
                ", page=" + page +
                ", size=" + size +
                '}';
    }
}
