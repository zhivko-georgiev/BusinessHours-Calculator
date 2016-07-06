package com.example.calculator.service;

import com.example.calculator.domain.BusinessHoursCalculator;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Service Interface for managing BusinessHoursCalculator.
 */
public interface BusinessHoursCalculatorService {

    /**
     * Save a businessHoursCalculator.
     * 
     * @param businessHoursCalculator the entity to save
     * @return the persisted entity
     */
    BusinessHoursCalculator save(BusinessHoursCalculator businessHoursCalculator);

    /**
     *  Get all the businessHoursCalculators.
     *  
     *  @return the list of entities
     */
    List<BusinessHoursCalculator> findAll();

    /**
     *  Get the "id" businessHoursCalculator.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    BusinessHoursCalculator findOne(Long id);

    /**
     *  Delete the "id" businessHoursCalculator.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
    
    
    ZonedDateTime calculateDeadline(long timeInterval, String startingDateTime);
}
