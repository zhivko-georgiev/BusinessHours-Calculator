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
    
    
    /**
     * Calculate the expected time for pickup.
     * 
     * @param timeInterval expected time in seconds for finishing the order
     * @param startingDateTime date and time at which the order was placed
     * @return date and time at which the order will be ready for pickup.
     */
    ZonedDateTime calculateDeadline(long timeInterval, String startingDateTime);
    
    /**
     * Prepares the actual working business hours for each order.
     * 
     * @return string containing the info about the business hours
     */
    String prepareBusinessHoursData();
}
