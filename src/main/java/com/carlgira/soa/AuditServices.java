package com.carlgira.soa;

import com.carlgira.soa.managers.audit.BpelAuditTrailManager;
import com.carlgira.soa.managers.audit.BpmAuditTrailManager;
import com.carlgira.soa.util.ServerConnection;
import com.oracle.schemas.bpel.audit_trail.audit_trail.AuditTrail;
import oracle.bpm.services.instancequery.IAuditInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.List;

@RestController
public class AuditServices {

    @Autowired
    private HttpServletRequest request;

    @RequestMapping( path = "/audit/bpm/{type}/image.png")
    public ResponseEntity<byte[]> bpmDiagram(@PathVariable("type") String type,
                                                  @RequestParam(value = "cikey", required = true) String cikey) throws Exception {

        ServerConnection serverConnection = this.getServerConnection();
        BpmAuditTrailManager bpmAuditTrailManager = new BpmAuditTrailManager(serverConnection);
        bpmAuditTrailManager.init();

        byte[] r = bpmAuditTrailManager.getBpmDiagram(cikey, type );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(r, headers, HttpStatus.OK);
    }

    @RequestMapping( path = "/audit/bpm/text")
    public List<IAuditInstance> getBpmAuditTrail(@RequestParam(value = "cikey", required = true) String cikey) throws Exception {

        ServerConnection serverConnection = this.getServerConnection();
        BpmAuditTrailManager bpmAuditTrailManager = new BpmAuditTrailManager(serverConnection);
        bpmAuditTrailManager.init();

        return bpmAuditTrailManager.getAuditTrail(cikey);
    }

    @RequestMapping( path = "/audit/bpel/text")
    public AuditTrail getBpelAuditTrail(@RequestParam(value = "cikey", required = true) String cikey) throws Exception {

        ServerConnection serverConnection = this.getServerConnection();
        BpelAuditTrailManager bpelAuditTrailManager = new BpelAuditTrailManager(serverConnection);
        return bpelAuditTrailManager.getAuditTrail(cikey);
    }

    private ServerConnection getServerConnection(){
        byte[] decodedBytes = Base64.getDecoder().decode(request.getHeader("Authorization").split(" ")[1]);
        String authorization = new String(decodedBytes);
        String user = authorization.split(":")[0];
        String password = authorization.split(":")[1];

        return new ServerConnection("t3://" + this.request.getLocalName() + ":" + this.request.getLocalPort() + "/soa-infra/", user, password,"");
    }
}
