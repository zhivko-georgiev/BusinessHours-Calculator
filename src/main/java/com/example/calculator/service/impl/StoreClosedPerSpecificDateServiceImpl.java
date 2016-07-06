package com.example.calculator.service.impl;

import com.example.calculator.service.StoreClosedPerSpecificDateService;
import com.example.calculator.domain.StoreClosedPerSpecificDate;
import com.example.calculator.repository.StoreClosedPerSpecificDateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing StoreClosedPerSpecificDate.
 */
@Service
@Transactional
public class StoreClosedPerSpecificDateServiceImpl implements StoreClosedPerSpecificDateService{

    private final Logger log = LoggerFactory.getLogger(StoreClosedPerSpecificDateServiceImpl.class);
    
    @Inject
    private StoreClosedPerSpecificDateRepository storeClosedPerSpecificDateRepository;
    
    /**
     * Save a storeClosedPerSpecificDate.
     * 
     * @param storeClosedPerSpecificDate the entity to save
     * @return the persisted entity
     */
    public StoreClosedPerSpecificDate save(StoreClosedPerSpecificDate storeClosedPerSpecificDate) {
        log.debug("Request to save StoreClosedPerSpecificDate : {}", storeClosedPerSpecificDate);
        StoreClosedPerSpecificDate result = storeClosedPerSpecificDateRepository.save(storeClosedPerSpecificDate);
        return result;
    }

    /**
     *  Get all the storeClosedPerSpecificDates.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<StoreClosedPerSpecificDate> findAll() {
        log.debug("Request to get all StoreClosedPerSpecificDates");
        List<StoreClosedPerSpecificDate> result = storeClosedPerSpecificDateRepository.findAllByOrderByDateAsc();
        return result;
    }

    /**
     *  Get one storeClosedPerSpecificDate by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public StoreClosedPerSpecificDate findOne(Long id) {
        log.debug("Request to get StoreClosedPerSpecificDate : {}", id);
        StoreClosedPerSpecificDate storeClosedPerSpecificDate = storeClosedPerSpecificDateRepository.findOne(id);
        return storeClosedPerSpecificDate;
    }

    /**
     *  Delete the  storeClosedPerSpecificDate by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete StoreClosedPerSpecificDate : {}", id);
        storeClosedPerSpecificDateRepository.delete(id);
    }
}
