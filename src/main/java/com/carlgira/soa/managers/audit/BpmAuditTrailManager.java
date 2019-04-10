package com.carlgira.soa.managers.audit;

import com.carlgira.soa.util.ServerConnection;
import oracle.bpel.services.bpm.common.IBPMContext;
import oracle.bpel.services.workflow.client.IWorkflowServiceClientConstants;
import oracle.bpel.services.workflow.client.WorkflowServiceClientFactory;
import oracle.bpm.client.BPMServiceClientFactory;
import oracle.bpm.services.common.exception.BPMException;
import oracle.bpm.services.instancequery.IAuditInstance;
import oracle.bpm.services.instancequery.IInstanceQueryService;
import java.io.IOException;
import java.util.*;
import java.util.zip.GZIPInputStream;

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

    static enum IMAGE_TYPE {
        PROCESS,
        AUDIT,
    };

    public byte[] getBpmDiagram(String instanceId, String type) {
        BpmAuditTrailManager.IMAGE_TYPE imageType;
        if (type.equalsIgnoreCase("process")) {
            imageType = BpmAuditTrailManager.IMAGE_TYPE.PROCESS;
        } else if (type.equalsIgnoreCase("audit")) {
            imageType = BpmAuditTrailManager.IMAGE_TYPE.AUDIT;
        } else {
            return null;
        }

        String base64 = null;
        try {
            bpmContext = bpmServiceClientFactory.getBPMUserAuthenticationService().getBPMContextForAuthenticatedUser();
            IInstanceQueryService instanceQueryService =
                    bpmServiceClientFactory.getBPMServiceClient().getInstanceQueryService();
            if (imageType.equals(BpmAuditTrailManager.IMAGE_TYPE.PROCESS)) {
                base64 = instanceQueryService.getProcessDiagram(bpmContext, instanceId, Locale.US);
            } else if (imageType.equals(BpmAuditTrailManager.IMAGE_TYPE.AUDIT)) {
                base64 = instanceQueryService.getProcessAuditDiagram(bpmContext, instanceId, Locale.US);
            }
        } catch (BPMException e) {
            e.printStackTrace();
        }

        try {
            return Base64.getDecoder().decode(new String(base64).getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<IAuditInstance> getAuditTrail(String id) throws BPMException {
        List<IAuditInstance> auditInstances = this.bpmServiceClientFactory.getBPMServiceClient().getInstanceQueryService().queryAuditInstanceByProcessId(bpmContext, id);
        return auditInstances;
    }


}
