package com.example.calculator.service.impl;

import com.example.calculator.service.StoreClosedPerDayOfWeekService;
import com.example.calculator.domain.StoreClosedPerDayOfWeek;
import com.example.calculator.repository.StoreClosedPerDayOfWeekRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing StoreClosedPerDayOfWeek.
 */
@Service
@Transactional
public class StoreClosedPerDayOfWeekServiceImpl implements StoreClosedPerDayOfWeekService{

    private final Logger log = LoggerFactory.getLogger(StoreClosedPerDayOfWeekServiceImpl.class);
    
    @Inject
    private StoreClosedPerDayOfWeekRepository storeClosedPerDayOfWeekRepository;
    
    /**
     * Save a storeClosedPerDayOfWeek.
     * 
     * @param storeClosedPerDayOfWeek the entity to save
     * @return the persisted entity
     */
    public StoreClosedPerDayOfWeek save(StoreClosedPerDayOfWeek storeClosedPerDayOfWeek) {
        log.debug("Request to save StoreClosedPerDayOfWeek : {}", storeClosedPerDayOfWeek);
        StoreClosedPerDayOfWeek result = storeClosedPerDayOfWeekRepository.save(storeClosedPerDayOfWeek);
        return result;
    }

    /**
     *  Get all the storeClosedPerDayOfWeeks.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<StoreClosedPerDayOfWeek> findAll() {
        log.debug("Request to get all StoreClosedPerDayOfWeeks");
        List<StoreClosedPerDayOfWeek> result = storeClosedPerDayOfWeekRepository.findAllByOrderByDayOfWeekAsc();
        return result;
    }

    /**
     *  Get one storeClosedPerDayOfWeek by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public StoreClosedPerDayOfWeek findOne(Long id) {
        log.debug("Request to get StoreClosedPerDayOfWeek : {}", id);
        StoreClosedPerDayOfWeek storeClosedPerDayOfWeek = storeClosedPerDayOfWeekRepository.findOne(id);
        return storeClosedPerDayOfWeek;
    }

    /**
     *  Delete the  storeClosedPerDayOfWeek by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete StoreClosedPerDayOfWeek : {}", id);
        storeClosedPerDayOfWeekRepository.delete(id);
    }
}
