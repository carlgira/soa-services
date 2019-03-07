package com.carlgira.soa.model.soa;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class ComponentInfo  implements Serializable {

    @Id
    @Column(name="CIKEY")
    private Long cikey;

    @Column(name="COMPOSITE_NAME")
    private String name;

    @Column(name="COMPOSITE_REVISION")
    private String revision;

    @Column(name="DOMAIN_NAME")
    private String partition;

    @Column(name="STATE")
    private String state;

    @Column(name="COMPONENTTYPE")
    private String type;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @Column(name="MODIFY_DATE")
    private Timestamp modifyDate;

    @JsonIgnore
    @Column(name="PARENT_ID")
    private Long parent;

    @JsonInclude()
    @Transient
    private List<ComponentInfo> components;

    @JsonInclude()
    @Transient
    private List<TaskInfo> tasks;

    public ComponentInfo() {
        this.tasks = new ArrayList<>();
        this.components = new ArrayList<>();
    }

    public Long getCikey() {
        return cikey;
    }

    public void setCikey(Long cikey) {
        this.cikey = cikey;
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

    public String getPartition() {
        return partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public List<ComponentInfo> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentInfo> components) {
        this.components = components;
    }

    public List<TaskInfo> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskInfo> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        try{
            ComponentInfo that = (ComponentInfo)o;
            return that.getCikey() == this.getCikey();
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCikey());
    }
}
