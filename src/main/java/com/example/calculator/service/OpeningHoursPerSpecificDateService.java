package com.example.calculator.service;

import com.example.calculator.domain.OpeningHoursPerSpecificDate;

import java.util.List;

/**
 * Service Interface for managing OpeningHoursPerSpecificDate.
 */
public interface OpeningHoursPerSpecificDateService {

    /**
     * Save a openingHoursPerSpecificDate.
     * 
     * @param openingHoursPerSpecificDate the entity to save
     * @return the persisted entity
     */
    OpeningHoursPerSpecificDate save(OpeningHoursPerSpecificDate openingHoursPerSpecificDate);

    /**
     *  Get all the openingHoursPerSpecificDates.
     *  
     *  @return the list of entities
     */
    List<OpeningHoursPerSpecificDate> findAll();

    /**
     *  Get the "id" openingHoursPerSpecificDate.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    OpeningHoursPerSpecificDate findOne(Long id);

    /**
     *  Delete the "id" openingHoursPerSpecificDate.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
