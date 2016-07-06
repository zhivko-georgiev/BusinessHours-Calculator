package com.example.calculator.service;

import com.example.calculator.domain.StoreClosedPerDayOfWeek;

import java.util.List;

/**
 * Service Interface for managing StoreClosedPerDayOfWeek.
 */
public interface StoreClosedPerDayOfWeekService {

    /**
     * Save a storeClosedPerDayOfWeek.
     * 
     * @param storeClosedPerDayOfWeek the entity to save
     * @return the persisted entity
     */
    StoreClosedPerDayOfWeek save(StoreClosedPerDayOfWeek storeClosedPerDayOfWeek);

    /**
     *  Get all the storeClosedPerDayOfWeeks.
     *  
     *  @return the list of entities
     */
    List<StoreClosedPerDayOfWeek> findAll();

    /**
     *  Get the "id" storeClosedPerDayOfWeek.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    StoreClosedPerDayOfWeek findOne(Long id);

    /**
     *  Delete the "id" storeClosedPerDayOfWeek.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
