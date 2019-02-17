package com.carlgira.soa.model;


import com.carlgira.soa.model.soa.DeployedComposite;

import java.util.List;

public class DeployedCompositeResponse {

    private List<DeployedComposite> deployedComposites;

    public DeployedCompositeResponse() {
    }

    public List<DeployedComposite> getDeployedComposites() {
        return deployedComposites;
    }

    public void setDeployedComposites(List<DeployedComposite> deployedComposites) {
        this.deployedComposites = deployedComposites;
    }
}


