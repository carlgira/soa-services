package com.carlgira.soa.repo;

import com.carlgira.soa.model.soa.FlowInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.sql.Timestamp;
import java.util.List;

public interface FlowInfoRepository extends CrudRepository<FlowInfo, Long> {

    @Query(nativeQuery = true)
    List<FlowInfo> findByDates(Timestamp startDate, Timestamp endDate, String running, String faulted, Integer offset, Integer size);

    @Query(nativeQuery = true)
    List<FlowInfo> findByNameAndDates(String compositeName, String revision, Timestamp startDate, Timestamp endDate, String running, String faulted, Integer offset, Integer size);

    @Query(nativeQuery = true)
    List<FlowInfo> findByFlowIdAndDates(Long flowid, Timestamp startDate, Timestamp endDate, String running, String faulted, Integer offset, Integer size);

    @Query(nativeQuery = true)
    List<FlowInfo> findByFlowIdAndNameAndDates(Long flowid, String compositeName , String compositeRevision, Timestamp startDate, Timestamp endDate, String running, String faulted, Integer offset, Integer size);

    @Query(nativeQuery = true)
    List<FlowInfo> findByCikeyAndDates(Long cikey, Timestamp startDate, Timestamp endDate, String running, String faulted, Integer offset, Integer size);

    @Query(nativeQuery = true)
    List<FlowInfo> findByCikeyAndNameAndDates(Long cikey, String compositeName , String compositeRevision, Timestamp startDate, Timestamp endDate, String running, String faulted, Integer offset, Integer size);

    @Query(nativeQuery = true)
    List<FlowInfo> findByEcidAndDates(String ecid, Timestamp startDate, Timestamp endDate, String running, String faulted, Integer offset, Integer size);

    @Query(nativeQuery = true)
    List<FlowInfo> findByEcidAndNameAndDates(String ecid, String compositeName , String compositeRevision, Timestamp startDate, Timestamp endDate, String running, String faulted, Integer offset, Integer size);


    @Query(nativeQuery = true)
    List<FlowInfo> findBySensorAndDates(String sensorName, String sensorValue, Timestamp startDate, Timestamp endDate, String compositeName ,String running, String faulted, Integer offset, Integer size);

    @Query(nativeQuery = true)
    List<FlowInfo> findSensorByFlowIdAndDates(Long flowid, String sensorName, String compositeName, Timestamp startDate, Timestamp endDate,String running, String faulted, Integer offset, Integer size);

}