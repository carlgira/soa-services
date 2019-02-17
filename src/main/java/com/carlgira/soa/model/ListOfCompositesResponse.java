package com.carlgira.soa.model;


import com.carlgira.soa.model.soa.FlowInfo;

import java.util.List;

public class ListOfCompositesResponse {

    private List<FlowInfo> composites;

    public ListOfCompositesResponse() {
    }

    public List<FlowInfo> getComposites() {
        return composites;
    }

    public void setComposites(List<FlowInfo> composites) {
        this.composites = composites;
    }
}
