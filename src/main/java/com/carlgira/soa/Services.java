package com.carlgira.soa;

import com.carlgira.soa.managers.SOAManager;
import com.carlgira.soa.model.*;
import com.carlgira.soa.model.soa.*;
import com.carlgira.soa.util.ServerConnection;
import oracle.soa.management.facade.flow.FlowInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import oracle.fabric.management.deployedcomposites.CompositeManager;
import javax.management.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@CacheConfig(cacheNames={"soaServicesCache"})
@RestController
public class Services {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private FlowInfoRepository compositeRepository;

    @Autowired
    private SensorInfoRepository sensorInfoRepository;

    @Autowired
    private ComponentInfoRepository componentInfoRepository;

    @Autowired
    private TaskInfoRepository taskInfoRepository;

    public Services(){
    }


    @CrossOrigin(origins = "*")
    @RequestMapping( path = "/abort",  method = RequestMethod.GET)
    public AbortResponse abortFlow(@RequestParam(value = "flowid", required = true) long flowid){

        ServerConnection serverConnection = this.getServerConnection();
        SOAManager compositeManager = new SOAManager(serverConnection);
        try {
            compositeManager.init();
            FlowInstance flowInstance = compositeManager.abortFlow(flowid);
            if(flowInstance == null){
                return new AbortResponse("error", "Message: No flowid found with value, " + flowid ) ;
            }
            compositeManager.close();
        } catch (Exception e) {
            return new AbortResponse("error", "Message: " +e.getMessage() + " -- " + e.getLocalizedMessage() + " -- Cause: " + e.getCause()) ;
        }

        return new AbortResponse("ok","" + flowid);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping( path = "/delete",  method = RequestMethod.GET)
    public AbortResponse deleteFlow(@RequestParam(value = "flowid", required = true) long flowid){

        ServerConnection serverConnection = this.getServerConnection();
        SOAManager compositeManager = new SOAManager(serverConnection);
        try {
            compositeManager.init();
            FlowInstance flowInstance = compositeManager.deleteFlow(flowid);
            if(flowInstance == null){
                return new AbortResponse("error", "Message: No flowid found with value, " + flowid ) ;
            }
            compositeManager.close();
        } catch (Exception e) {
            return new AbortResponse("error", "Message: " +e.getMessage() + " -- " + e.getLocalizedMessage() + " -- Cause: " + e.getCause()) ;
        }

        return new AbortResponse("ok","" + flowid);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping( path = "/abort", method = RequestMethod.POST)
    public AbortResponse abortFlows(@RequestBody AbortRequest abortRequest){

        ServerConnection serverConnection = this.getServerConnection();
        SOAManager compositeManager = new SOAManager(serverConnection);
        try {
            compositeManager.init();
            long[] flowsid = new long[abortRequest.getFlowids().size()];
            for(int i=0; i<abortRequest.getFlowids().size(); i++){
                flowsid[i] = abortRequest.getFlowids().get(i);
            }
            compositeManager.abortFlows(flowsid);
            compositeManager.close();
        } catch (Exception e) {
            return new AbortResponse("error", "Message: " +e.getMessage() + " -- " + e.getLocalizedMessage() + " -- Cause: " + e.getCause()) ;
        }

        return new AbortResponse("ok","");
    }

    @CrossOrigin(origins = "*")
    @RequestMapping( path = "/composites", method = RequestMethod.POST)
    public ListOfCompositesResponse listOfComposites(@RequestBody ListOfCompositesRequest r){

        Date endDate = r.getEndDate();
        Date startDate = r.getStartDate();

        List<FlowInfo> cubeInstances = null;

        if((r.getFlowids() == null || r.getFlowids().isEmpty()) &&
                (r.getEcids() == null || r.getEcids().isEmpty()) &&
                (r.getCikeys() == null || r.getCikeys().isEmpty()) &&
                (r.getSensorValues() == null || r.getSensorValues().isEmpty())
        ){
            if((r.getComposite() == null || r.getComposite().trim().isEmpty()) &&
                    (r.getRevision() == null || r.getRevision().trim().isEmpty())){

                cubeInstances = this.compositeRepository.findByDates(new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()), r.getState(), r.getFault(), r.getSize()*r.getPage(), r.getSize());
                System.out.println("this.compositeRepository.findByDates");
            }
            else {
                cubeInstances = this.compositeRepository.findByNameAndDates(r.getComposite(), r.getRevision(), new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()), r.getState(), r.getFault(), r.getSize()*r.getPage(), r.getSize());
                System.out.println("this.compositeRepository.findByNameAndDates");
            }
        }
        else if(r.getFlowids() != null && !r.getFlowids().isEmpty()){

            if(r.getComposite() != null && r.getComposite().trim().length() > 0 && r.getRevision() != null && r.getRevision().trim().length() > 0){
                cubeInstances = this.compositeRepository.findByFlowIdAndNameAndDates(r.getFlowids().get(0),  r.getComposite() , r.getRevision(), new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()), r.getState(), r.getFault(), r.getSize()*r.getPage(), r.getSize());
                System.out.println("this.compositeRepository.findByFlowIdAndNameAndDates");
            }
            else{
                cubeInstances = this.compositeRepository.findByFlowIdAndDates(r.getFlowids().get(0), new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()), r.getState(), r.getFault(), r.getSize()*r.getPage(), r.getSize());
                System.out.println("this.compositeRepository.findByFlowIdAndDates");
            }
        }
        else if(r.getCikeys() != null && !r.getCikeys().isEmpty()){

            if(r.getComposite() != null && r.getComposite().trim().length() > 0 && r.getRevision() != null && r.getRevision().trim().length() > 0){
                cubeInstances = this.compositeRepository.findByCikeyAndDates(r.getCikeys().get(0), new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()), r.getState(), r.getFault(), r.getSize()*r.getPage(), r.getSize());
                System.out.println("this.compositeRepository.findByCikeyAndDates");
            }
            else{
                cubeInstances = this.compositeRepository.findByCikeyAndNameAndDates(r.getCikeys().get(0), r.getComposite() , r.getRevision() , new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()), r.getState(), r.getFault(), r.getSize()*r.getPage(), r.getSize());
                System.out.println("this.compositeRepository.findByCikeyAndNameAndDates");
            }
        }
        else if(r.getEcids() != null && !r.getEcids().isEmpty()){

            if(r.getComposite() != null && r.getComposite().trim().length() > 0 && r.getRevision() != null && r.getRevision().trim().length() > 0){
                cubeInstances = this.compositeRepository.findByEcidAndNameAndDates(r.getEcids().get(0), r.getComposite() , r.getRevision(), new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()), r.getState(), r.getFault(), r.getSize()*r.getPage(), r.getSize());
                System.out.println("this.compositeRepository.findByEcidAndNameAndDates");
            }
            else{
                cubeInstances = this.compositeRepository.findByEcidAndDates(r.getEcids().get(0), new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()), r.getState(), r.getFault(), r.getSize()*r.getPage(), r.getSize());
                System.out.println("this.compositeRepository.findByEcidAndDates");
            }
        }
        else if(r.getSensorValues() != null && !r.getSensorValues().isEmpty()){
            cubeInstances = this.compositeRepository.findBySensorAndDates(r.getSensorName(), r.getSensorValues().get(0), new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()), r.getComposite(),  r.getState(), r.getFault(), r.getSize()*r.getPage(), r.getSize());
            System.out.println("this.compositeRepository.findBySensorAndDates");
        }

        ListOfCompositesResponse listOfCompositesResponse = new ListOfCompositesResponse();
        listOfCompositesResponse.setComposites(cubeInstances);

        return listOfCompositesResponse;
    }

    @CrossOrigin(origins = "*")
    @Cacheable("soaServicesCache")
    @RequestMapping( path = "/deployed-composites", method = RequestMethod.GET)
    public DeployedCompositeResponse deployedComposites() throws IOException, MalformedObjectNameException, AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException {

        DeployedCompositeResponse response = new DeployedCompositeResponse();
        List<DeployedComposite> composites = new ArrayList<>();

        ServerConnection serverConnection = this.getServerConnection();

        CompositeManager.initConnection(this.request.getLocalName(), String.valueOf(this.request.getLocalPort()), serverConnection.adminUser, serverConnection.adminPassword);
        String value = CompositeManager.listDeployedComposites(CompositeManager.getCompositeLifeCycleMBean());

        StringTokenizer tokenizer = new StringTokenizer(value, "\n");
        tokenizer.nextToken();

        while (tokenizer.hasMoreTokens()){
            String token = tokenizer.nextToken();
            DeployedComposite composite = new DeployedComposite(token);
            composites.add(composite);
        }

        response.setDeployedComposites(composites);

        return response;
    }

    @CrossOrigin(origins = "*")
    @Cacheable("soaServicesCache")
    @RequestMapping( path = "/sensor/names", method = RequestMethod.GET)
    public List<String> sensorNames() {

        List<String> responses = new ArrayList<>();
        for(SensorInfo e : sensorInfoRepository.findBySensorNames()){
            responses.add(e.getId().getName());
        }

        return responses;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping( path = "/sensor", method = RequestMethod.GET)
    public List<SensorInfo> sensorByCikey(@RequestParam Long cikey) {

        List<SensorInfo> s = sensorInfoRepository.findByCikey(cikey);

        return s;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping( path = "/flow", method = RequestMethod.GET)
    public List<ComponentInfo> componentInfo(@RequestParam(value = "flowid", required = true) long flowid) throws Exception {

        List<ComponentInfo> components = componentInfoRepository.findByFlowId(flowid);

        List<TaskInfo> tasks = taskInfoRepository.findByFlowId(flowid);

        Map<Long, ComponentInfo> coMap = components.stream().collect(Collectors.toMap(ComponentInfo::getCikey, Function.identity()));

        List<ComponentInfo> response = new ArrayList<>();

        Long currentCikey = -1L;
        for(TaskInfo t: tasks){
            if(!t.getCikey().equals(currentCikey)){
                currentCikey = t.getCikey();
            }
            coMap.get(currentCikey).getTasks().add(t);
        }

        int counter = 0;
        int size = components.size();
        while(!components.isEmpty() && counter < size){
            ComponentInfo currentComp = components.get(components.size()-1);
            if(currentComp.getParent() != null && coMap.containsKey(currentComp.getParent())){
                coMap.get(currentComp.getParent()).getComponents().add(currentComp);
                components.remove(components.size()-1);
            }
            counter++;
        }

        for(ComponentInfo c: coMap.values()){
            if(c.getParent() == null){
                response.add(c);
            }
        }

        return response;
    }

    private ServerConnection getServerConnection(){
        byte[] decodedBytes = Base64.getDecoder().decode(request.getHeader("Authorization").split(" ")[1]);
        String authorization = new String(decodedBytes);
        String user = authorization.split(":")[0];
        String password = authorization.split(":")[1];

        return new ServerConnection("t3://" + this.request.getLocalName() + ":" + this.request.getLocalPort() + "/soa-infra/", user, password,"");
    }

    @Scheduled(fixedDelay = 60000)
    public void clearCache(){
        cacheManager.getCache("soaServicesCache").clear();
    }
}
