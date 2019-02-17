package com.carlgira.soa;

import com.carlgira.soa.model.soa.SensorInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface SensorInfoRepository  extends CrudRepository<SensorInfo, Long> {

    @Query(nativeQuery = true)
    List<SensorInfo> findBySensorNames();

    @Query(nativeQuery = true)
    List<SensorInfo> findByCikey(Long cikey);
}
