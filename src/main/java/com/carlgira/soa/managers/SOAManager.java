package com.carlgira.soa.managers;

import com.carlgira.soa.util.ServerConnection;
import oracle.soa.management.facade.*;
import oracle.soa.management.facade.flow.FlowInstance;

import oracle.soa.management.util.flow.FlowInstanceFilter;

import javax.naming.Context;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by carlgira on 03/03/2016.
 * Class to control the connection to the SOA server to lookup for BPEL or FlowInfo instances.
 */
public class SOAManager {

    public Locator locator;
    private ServerConnection serverConnection;

    public SOAManager(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    /**
     * Create server connection
     * @throws Exception
     */
    public void init() throws Exception {

        Hashtable jndiProps = new Hashtable();
        jndiProps.put(Context.PROVIDER_URL, this.serverConnection.server);
        jndiProps.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
        jndiProps.put(Context.SECURITY_PRINCIPAL, this.serverConnection.adminUser);
        jndiProps.put(Context.SECURITY_CREDENTIALS, this.serverConnection.adminPassword);
        jndiProps.put("dedicated.connection","true");

        this.locator = LocatorFactory.createLocator(jndiProps);
    }

    public void close(){
        this.locator.close();
    }

    /**
     * Return a FlowInstance using a Flowid
     * @param flowId
     * @return
     * @throws Exception
     */
    public FlowInstance getFlowInstanceByFlowId(Long flowId) throws Exception {
        FlowInstanceFilter flowInstanceFilter = new FlowInstanceFilter();
        flowInstanceFilter.setFlowId(flowId);
        List<FlowInstance> flowInstances = locator.getFlowInstances(flowInstanceFilter);

        if(flowInstances == null || flowInstances.size() != 1){
            return null;
        }

        return flowInstances.get(0);
    }

    public FlowInstance abortFlow(long flowid) throws Exception {
        FlowInstance flowInstance = this.getFlowInstanceByFlowId(flowid);
        if(flowInstance != null){
            flowInstance.abortFlow();
        }
        return flowInstance;
    }

    public FlowInstance deleteFlow(long flowid) throws Exception {
        FlowInstance flowInstance = this.getFlowInstanceByFlowId(flowid);
        if(flowInstance != null){
            flowInstance.deleteFlow();
        }
        return flowInstance;
    }

    public void abortFlows(long[] flowsid) throws Exception {
        this.locator.abortFlows(flowsid);
    }
}
