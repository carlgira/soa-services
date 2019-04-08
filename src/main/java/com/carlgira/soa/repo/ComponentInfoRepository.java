package com.carlgira.soa.repo;

import com.carlgira.soa.model.soa.ComponentInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ComponentInfoRepository  extends CrudRepository<ComponentInfo, Long> {

    @Query(nativeQuery = true)
    List<ComponentInfo> findByFlowId(Long flowid);

    @Query(nativeQuery = true)
    List<ComponentInfo> findByTransactionTimeoutException();
}
