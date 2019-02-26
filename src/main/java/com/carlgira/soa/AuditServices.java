package com.carlgira.soa;

import com.carlgira.soa.util.ServerConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

@RestController
public class AuditServices {

    @Autowired
    private HttpServletRequest request;

    @RequestMapping( path = "/composites/bpm/diagram.png")
    public ResponseEntity<byte[]> flowChartBpmImg(@RequestParam(value = "type", required = true) String type,
                                                  @RequestParam(value = "bpmId", required = true) String bpmId) throws Exception {

        ServerConnection serverConnection = this.getServerConnection();
        AuditProcessImage auditProcessImage = new AuditProcessImage(serverConnection);

        byte[] r = auditProcessImage.getBpmDiagrama(bpmId, type );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(r, headers, HttpStatus.OK);
    }

    private ServerConnection getServerConnection(){
        byte[] decodedBytes = Base64.getDecoder().decode(request.getHeader("Authorization").split(" ")[1]);
        String authorization = new String(decodedBytes);
        String user = authorization.split(":")[0];
        String password = authorization.split(":")[1];

        return new ServerConnection("t3://" + this.request.getLocalName() + ":" + this.request.getLocalPort() + "/soa-infra/", user, password,"");
    }
}
