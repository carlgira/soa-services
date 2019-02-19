package com.carlgira.soa.model.soa;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;

@Embeddable
public class SensorInfoId implements Serializable {

    @Column(name="CIKEY")
    private Long cikey;

    @Column(name="FLOW_ID")
    private BigDecimal flowid;

    @Column(name="COMPONENT_NAME")
    private String componentName;

    @Column(name="SENSOR_NAME")
    private String name;

    public SensorInfoId() {
    }

    public Long getCikey() {
        return cikey;
    }

    public void setCikey(Long cikey) {
        this.cikey = cikey;
    }

    public BigDecimal getFlowid() {
        return flowid;
    }

    public void setFlowid(BigDecimal flowid) {
        this.flowid = flowid;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SensorInfoId{" +
                "cikey=" + cikey +
                ", flowid=" + flowid +
                ", componentName='" + componentName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
