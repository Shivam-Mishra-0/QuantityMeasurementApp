package com.quantitymeasurement.repository;

import java.util.List;

import com.quantitymeasurement.entity.QuantityMeasurementEntity;


public interface IQuantityMeasurementRepository {

    
    void save(QuantityMeasurementEntity entity);

    
    List<QuantityMeasurementEntity> getAllMeasurements();

    
    List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation);

    
    List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType);

    
    int getTotalCount();

    
    void deleteAll();

    
    default String getPoolStatistics() {
        return "Pool statistics not available for this repository type";
    }

    
    default void releaseResources() {
        /* Default implementation does nothing — override in resource-holding implementations */
    }
}
