package com.carlgira.soa.model.soa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class TaskInfo {

    @Id
    @Column(name="TASKID")
    private String taskid;

    @Column(name="INSTANCEID")
    private Long cikey;

    @Column(name="TASKNUMBER")
    private String tasknumber;

    @Column(name="UPDATEDDATE")
    private Timestamp updateDate;

    @Column(name="STATE")
    private String state;

    @Column(name="OUTCOME")
    private String outcome;

    public TaskInfo() {
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public Long getCikey() {
        return cikey;
    }

    public void setCikey(Long cikey) {
        this.cikey = cikey;
    }

    public String getTasknumber() {
        return tasknumber;
    }

    public void setTasknumber(String tasknumber) {
        this.tasknumber = tasknumber;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}
