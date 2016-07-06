package com.example.calculator.service;

import com.example.calculator.domain.StoreClosedPerSpecificDate;

import java.util.List;

/**
 * Service Interface for managing StoreClosedPerSpecificDate.
 */
public interface StoreClosedPerSpecificDateService {

    /**
     * Save a storeClosedPerSpecificDate.
     * 
     * @param storeClosedPerSpecificDate the entity to save
     * @return the persisted entity
     */
    StoreClosedPerSpecificDate save(StoreClosedPerSpecificDate storeClosedPerSpecificDate);

    /**
     *  Get all the storeClosedPerSpecificDates.
     *  
     *  @return the list of entities
     */
    List<StoreClosedPerSpecificDate> findAll();

    /**
     *  Get the "id" storeClosedPerSpecificDate.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    StoreClosedPerSpecificDate findOne(Long id);

    /**
     *  Delete the "id" storeClosedPerSpecificDate.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
