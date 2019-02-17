package com.carlgira.soa.model.soa;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeployedComposite{

    private String name;
    private String revision;
    private String partition;
    private String mode;
    private String state;
    private String scope;
    private Boolean isDefault;
    private String deployedTime;

    public DeployedComposite(){
    }

    public DeployedComposite(String raw){
        String regex = "(.*) (.*)\\[(.*)], partition=(.*), scope=(.*), mode=(.*), state=(.*), isDefault=(.*), deployedTime=(.*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(raw);

        if(matcher.find()){
            this.name = matcher.group(2);
            this.revision = matcher.group(3);
            this.partition = matcher.group(4);
            this.scope = matcher.group(5);
            this.mode = matcher.group(6);
            this.state = matcher.group(7);
            this.isDefault = Boolean.parseBoolean(matcher.group(8));
            this.deployedTime = matcher.group(9);
        }
        else {
            regex = "(.*) (.*)\\[(.*)], partition=(.*), mode=(.*), state=(.*), isDefault=(.*), deployedTime=(.*)";
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(raw);

            if(matcher.find()){
                this.name = matcher.group(2);
                this.revision = matcher.group(3);
                this.partition = matcher.group(4);
                this.mode = matcher.group(5);
                this.state = matcher.group(6);
                this.isDefault = Boolean.parseBoolean(matcher.group(7));
                this.deployedTime = matcher.group(8);
            }
        }
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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public String getDeployedTime() {
        return deployedTime;
    }

    public void setDeployedTime(String deployedTime) {
        this.deployedTime = deployedTime;
    }
}

