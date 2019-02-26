package com.carlgira.soa.managers.audit;

import com.carlgira.soa.managers.SOAManager;
import com.carlgira.soa.util.ServerConnection;
import com.oracle.schemas.bpel.audit_trail.audit_trail.AuditTrail;
import oracle.soa.management.facade.bpel.BPELInstance;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.ArrayList;
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

        if(bpelInstance.getAuditTrail() != null){
            JAXBContext jaxbContext = JAXBContext.newInstance(AuditTrail.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            StringReader reader = new StringReader(bpelInstance.getAuditTrail());

            AuditTrail auditTrail = (AuditTrail) jaxbUnmarshaller.unmarshal(reader);
        }

        return new ArrayList<>();
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("socksProxyHost", "localhost");
        System.setProperty("socksProxyPort", "8123");
        System.setProperty("java.net.socks.username", "t719400");
        System.setProperty("java.net.socks.password", "125WNUme");
        System.setProperty("java.awt.headless", "true");

        // BSS 30000
        // OSS 31000
        ServerConnection serverConnection = new ServerConnection("t3://ephol328.serv.dc.es.telefonica:30000/soa-infra/", "usarqtec", "temporal12", "");

        //BpelAuditTrailManager bpelAuditTrailManager = new BpelAuditTrailManager(serverConnection);
        //bpelAuditTrailManager.getAuditTrail("983040036");

        SOAManager soaManager = new SOAManager(serverConnection);
        BPELInstance bpelInstance = soaManager.getBpelByCikey("983040036");

        System.out.println(bpelInstance);
    }

}
