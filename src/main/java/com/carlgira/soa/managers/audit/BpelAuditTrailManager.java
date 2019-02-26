package com.carlgira.soa.managers.audit;

import com.carlgira.soa.managers.SOAManager;
import com.carlgira.soa.util.ServerConnection;
import oracle.bpm.services.util.AuditTrail;
import oracle.soa.management.facade.bpel.BPELInstance;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by carlgira on 08/03/2016.
 * Class to parse the content of a BPEL AuditTrail. Add some functions to lookup all the events.
 */
public class BpelAuditTrailManager{

    private ServerConnection serverConnection;

    public BpelAuditTrailManager(ServerConnection serverConnection){
        this.serverConnection = serverConnection;
    }

    public List<CEvent> getAuditTrail(String id) throws Exception {
        SOAManager soaManager = new SOAManager(this.serverConnection);
        BPELInstance bpelInstance = soaManager.getBpelByCikey(id);

        JAXBContext jaxbContext = JAXBContext.newInstance(AuditTrail.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        StringReader reader = new StringReader(bpelInstance.getAuditTrail());

        AuditTrail auditTrail = (AuditTrail) jaxbUnmarshaller.unmarshal(reader);


        return new ArrayList<>();
    }

}
