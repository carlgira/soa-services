package com.carlgira.soa.managers;

import com.carlgira.soa.util.ServerConnection;
import oracle.bpel.services.bpm.common.IBPMContext;
import oracle.bpel.services.workflow.client.IWorkflowServiceClientConstants;
import oracle.bpel.services.workflow.client.WorkflowServiceClientFactory;
import oracle.bpm.client.BPMServiceClientFactory;
import oracle.bpm.services.common.exception.BPMException;
import oracle.bpm.services.instancequery.IAuditInstance;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cgiraldo on 04/05/2017.
 */
public class BPMManager{

    public List<IAuditInstance> auditInstances;

    private ServerConnection serverConnection;

    private IBPMContext bpmContext;
    private BPMServiceClientFactory bpmServiceClientFactory;

    public BPMManager(ServerConnection serverConnection) {
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
}
