package com.carlgira.soa.repo;

import com.carlgira.soa.model.soa.TaskInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface TaskInfoRepository extends CrudRepository<TaskInfo, String> {

    @Query(nativeQuery = true)
    List<TaskInfo> findByFlowId(Long flowid);
}
