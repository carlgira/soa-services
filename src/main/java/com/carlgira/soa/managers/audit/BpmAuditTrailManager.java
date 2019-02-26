package com.carlgira.soa.managers.audit;

import com.carlgira.soa.util.ServerConnection;
import oracle.bpel.services.bpm.common.IBPMContext;
import oracle.bpel.services.workflow.client.IWorkflowServiceClientConstants;
import oracle.bpel.services.workflow.client.WorkflowServiceClientFactory;
import oracle.bpm.client.BPMServiceClientFactory;
import oracle.bpm.services.common.exception.BPMException;
import oracle.bpm.services.instancequery.IAuditInstance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cgiraldo on 04/05/2017.
 */
public class BpmAuditTrailManager{

    private ServerConnection serverConnection;
    private BPMServiceClientFactory bpmServiceClientFactory;
    private IBPMContext bpmContext;

    public BpmAuditTrailManager(ServerConnection serverConnection){
        this.serverConnection = serverConnection;
    }

    public void init() throws BPMException {
        Map<IWorkflowServiceClientConstants.CONNECTION_PROPERTY, String> properties = new HashMap<IWorkflowServiceClientConstants.CONNECTION_PROPERTY, String>();
        properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.CLIENT_TYPE, WorkflowServiceClientFactory.REMOTE_CLIENT);
        properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_PROVIDER_URL, serverConnection.server);
        properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_SECURITY_PRINCIPAL,serverConnection.adminUser);
        properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_SECURITY_CREDENTIALS,serverConnection.adminPassword);
        properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");

        this.bpmServiceClientFactory = BPMServiceClientFactory.getInstance(properties, null, null);
        this.bpmContext = bpmServiceClientFactory.getBPMUserAuthenticationService().getBPMContextForAuthenticatedUser();
    }

    private List<CEvent> getAuditTrail(String id) throws BPMException {

        List<IAuditInstance> auditInstances = this.bpmServiceClientFactory.getBPMServiceClient().getInstanceQueryService().queryAuditInstanceByProcessId(bpmContext, id);

        return new ArrayList<>();
    }
}
