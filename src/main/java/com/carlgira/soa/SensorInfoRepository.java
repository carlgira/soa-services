package com.carlgira.soa;

import com.carlgira.soa.model.soa.SensorInfo;
import com.carlgira.soa.model.soa.SensorInfoId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface SensorInfoRepository  extends CrudRepository<SensorInfo, SensorInfoId> {

    @Query(nativeQuery = true)
    List<SensorInfo> findBySensorNames();

    @Query(nativeQuery = true)
    List<SensorInfo> findByCikey(Long cikey);
}
