package com.example.calculator.service;

import com.example.calculator.domain.OpeningHoursPerDayOfWeek;

import java.util.List;

/**
 * Service Interface for managing OpeningHoursPerDayOfWeek.
 */
public interface OpeningHoursPerDayOfWeekService {

    /**
     * Save a openingHoursPerDayOfWeek.
     * 
     * @param openingHoursPerDayOfWeek the entity to save
     * @return the persisted entity
     */
    OpeningHoursPerDayOfWeek save(OpeningHoursPerDayOfWeek openingHoursPerDayOfWeek);

    /**
     *  Get all the openingHoursPerDayOfWeeks.
     *  
     *  @return the list of entities
     */
    List<OpeningHoursPerDayOfWeek> findAll();

    /**
     *  Get the "id" openingHoursPerDayOfWeek.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    OpeningHoursPerDayOfWeek findOne(Long id);

    /**
     *  Delete the "id" openingHoursPerDayOfWeek.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
