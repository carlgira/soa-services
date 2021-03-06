package com.carlgira.soa;

import com.carlgira.soa.managers.audit.BpelAuditTrailManager;
import com.carlgira.soa.managers.audit.BpmAuditTrailManager;
import com.carlgira.soa.model.BPMAuditEvent;
import com.carlgira.soa.model.soa.ComponentInfo;
import com.carlgira.soa.model.soa.TaskInfo;
import com.carlgira.soa.repo.ComponentInfoRepository;
import com.carlgira.soa.repo.TaskInfoRepository;
import com.carlgira.soa.util.ServerConnection;
import com.carlgira.soa.util.Utils;
import com.oracle.schemas.bpel.audit_trail.audit_trail.AuditTrail;
import oracle.bpm.services.instancequery.IAuditInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@CacheConfig(cacheNames={"auditServicesCache"})
public class AuditServices {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ComponentInfoRepository componentInfoRepository;

    @Autowired
    private TaskInfoRepository taskInfoRepository;

    @CrossOrigin(origins = "*")
    @Cacheable("auditServicesCache")
    @RequestMapping( path = "/audit/flow", method = RequestMethod.GET)
    public List<ComponentInfo> componentInfo(@RequestParam(value = "flowid", required = true) long flowid) throws Exception {

        List<ComponentInfo> components = componentInfoRepository.findByFlowId(flowid);

        List<TaskInfo> tasks = taskInfoRepository.findByFlowId(flowid);

        Map<Long, ComponentInfo> coMap = components.stream().collect(Collectors.toMap(ComponentInfo::getCikey, Function.identity()));

        for(ComponentInfo c : coMap.values()){
            System.out.println(c.getName() + " " + c.getCikey() + " " + c.getParent());
        }

        List<ComponentInfo> response = new ArrayList<>();

        Long currentCikey = -1L;
        for(TaskInfo t: tasks){
            if(!t.getCikey().equals(currentCikey)){
                currentCikey = t.getCikey();
            }

            if(coMap.containsKey(currentCikey)){
                coMap.get(currentCikey).getTasks().add(t);
            }
        }

        while(!components.isEmpty()){
            ComponentInfo currentComp = components.get(components.size()-1);
            if(currentComp.getParent() == null){
                components.remove(components.size()-1);
                continue;
            }
            if(currentComp.getParent() != null && coMap.containsKey(currentComp.getParent())){
                coMap.get(currentComp.getParent()).getComponents().add(currentComp);
            }
            components.remove(components.size()-1);
        }

        for(ComponentInfo c: coMap.values()){
            if(c.getParent() == null){
                response.add(c);
            }
            else if(c.getParent() != null && !coMap.containsKey(c.getParent())) {
                response.add(c);
            }
        }
        return response;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping( path = "/audit/bpm/{type}/image.png")
    public ResponseEntity<byte[]> bpmDiagram(@PathVariable("type") String type,
                                                  @RequestParam(value = "cikey", required = true) String cikey) throws Exception {

        ServerConnection serverConnection = Utils.getServerConnection(request);
        BpmAuditTrailManager bpmAuditTrailManager = new BpmAuditTrailManager(serverConnection);
        bpmAuditTrailManager.init();

        byte[] r = bpmAuditTrailManager.getBpmDiagram(cikey, type );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(r, headers, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping( path = "/audit/bpm/text")
    public List<BPMAuditEvent> getBpmAuditTrail(@RequestParam(value = "cikey", required = true) String cikey) throws Exception {

        ServerConnection serverConnection = Utils.getServerConnection(request);
        List<BPMAuditEvent> bpmAuditEvents = new ArrayList<>();
        BpmAuditTrailManager bpmAuditTrailManager = new BpmAuditTrailManager(serverConnection);
        bpmAuditTrailManager.init();
        for(IAuditInstance audit : bpmAuditTrailManager.getAuditTrail(cikey)){
            BPMAuditEvent bpmAuditEvent = new BPMAuditEvent();
            bpmAuditEvent.setLabel(audit.getLabel());
            bpmAuditEvent.setActivityName(audit.getActivityName());
            bpmAuditEvent.setAuditInstanceType(audit.getAuditInstanceType());
            bpmAuditEvent.setAuditLog(audit.getAuditLog());
            bpmAuditEvent.setComponentType(audit.getComponentType());
            bpmAuditEvent.setCreateTime(audit.getCreateTime());
            bpmAuditEvent.setFlowElementType(audit.getFlowElementType());
            bpmAuditEvent.setOperation(audit.getOperation().getType(audit.getAuditInstanceType().equals("START")));
            bpmAuditEvents.add(bpmAuditEvent);
        }

        return bpmAuditEvents;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping( path = "/audit/bpel/text")
    public AuditTrail getBpelAuditTrail(@RequestParam(value = "cikey", required = true) String cikey) throws Exception {

        ServerConnection serverConnection = Utils.getServerConnection(request);
        BpelAuditTrailManager bpelAuditTrailManager = new BpelAuditTrailManager(serverConnection);

        AuditTrail auditTrail = bpelAuditTrailManager.getAuditTrail(cikey);

        return auditTrail;
    }

    @Scheduled(fixedDelay = 120000)
    public void clearCache(){
        cacheManager.getCache("auditServicesCache").clear();
    }
}
