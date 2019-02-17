package com.carlgira.soa.model.soa;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;

public class SensorInfo {

    @Id
    @Column(name="CIKEY")
    private Long cikey;

    @Column(name="FLOW_ID")
    private BigDecimal flowid;

    @Column(name="COMPONENT_NAME")
    private String componentName;

    @Column(name="SENSOR_NAME")
    private String name;

    @Column(name="SENSOR_VALUE")
    private String value;

    public SensorInfo() {
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
