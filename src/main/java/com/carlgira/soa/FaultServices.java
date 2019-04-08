package com.carlgira.soa;

import com.carlgira.soa.managers.audit.BpelAuditTrailManager;
import com.carlgira.soa.managers.audit.BpmAuditTrailManager;
import com.carlgira.soa.model.soa.ComponentInfo;
import com.carlgira.soa.model.soa.TaskInfo;
import com.carlgira.soa.repo.ComponentInfoRepository;
import com.carlgira.soa.repo.TaskInfoRepository;
import com.carlgira.soa.util.ServerConnection;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@CacheConfig(cacheNames={"faultServicesCache"})
public class FaultServices {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ComponentInfoRepository componentInfoRepository;

    @RequestMapping( path = "/fault/transactionTimeout")
    public List<ComponentInfo> faultNonRecoverable() throws Exception {
        return componentInfoRepository.findByTransactionTimeoutException();
    }

    @Scheduled(fixedDelay = 1200000)
    public void clearCache(){
        cacheManager.getCache("faultServicesCache").clear();
    }
}
