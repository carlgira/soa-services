package com.carlgira.soa.model;

import java.util.List;

public class AbortRequest {

    private List<Long> flowids;

    public AbortRequest() {
    }

    public List<Long> getFlowids() {
        return flowids;
    }

    public void setFlowids(List<Long> flowids) {
        this.flowids = flowids;
    }
}
