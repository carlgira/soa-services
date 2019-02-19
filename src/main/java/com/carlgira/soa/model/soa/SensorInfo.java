package com.carlgira.soa.model.soa;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class SensorInfo  implements Serializable {

    @EmbeddedId
    private SensorInfoId id;

    @Column(name="SENSOR_VALUE")
    private String value;

    public SensorInfo() {
    }

    public SensorInfoId getId() {
        return id;
    }

    public void setId(SensorInfoId id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SensorInfo{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
