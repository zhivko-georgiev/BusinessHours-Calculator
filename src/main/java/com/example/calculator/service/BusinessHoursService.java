package com.example.calculator.service;

import com.example.calculator.domain.BusinessHours;

import java.util.List;

/**
 * Service Interface for managing BusinessHours.
 */
public interface BusinessHoursService {

    /**
     * Save a businessHours.
     * 
     * @param businessHours the entity to save
     * @return the persisted entity
     */
    BusinessHours save(BusinessHours businessHours);

    /**
     *  Get all the businessHours.
     *  
     *  @return the list of entities
     */
    List<BusinessHours> findAll();

    /**
     *  Get the "id" businessHours.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    BusinessHours findOne(Long id);

    /**
     *  Delete the "id" businessHours.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
