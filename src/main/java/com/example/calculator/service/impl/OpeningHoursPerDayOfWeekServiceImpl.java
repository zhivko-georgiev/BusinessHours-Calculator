package com.example.calculator.service.impl;

import com.example.calculator.service.OpeningHoursPerDayOfWeekService;
import com.example.calculator.domain.OpeningHoursPerDayOfWeek;
import com.example.calculator.repository.OpeningHoursPerDayOfWeekRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing OpeningHoursPerDayOfWeek.
 */
@Service
@Transactional
public class OpeningHoursPerDayOfWeekServiceImpl implements OpeningHoursPerDayOfWeekService{

    private final Logger log = LoggerFactory.getLogger(OpeningHoursPerDayOfWeekServiceImpl.class);
    
    @Inject
    private OpeningHoursPerDayOfWeekRepository openingHoursPerDayOfWeekRepository;
    
    /**
     * Save a openingHoursPerDayOfWeek.
     * 
     * @param openingHoursPerDayOfWeek the entity to save
     * @return the persisted entity
     */
    public OpeningHoursPerDayOfWeek save(OpeningHoursPerDayOfWeek openingHoursPerDayOfWeek) {
        log.debug("Request to save OpeningHoursPerDayOfWeek : {}", openingHoursPerDayOfWeek);
        OpeningHoursPerDayOfWeek result = openingHoursPerDayOfWeekRepository.save(openingHoursPerDayOfWeek);
        return result;
    }

    /**
     *  Get all the openingHoursPerDayOfWeeks.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<OpeningHoursPerDayOfWeek> findAll() {
        log.debug("Request to get all OpeningHoursPerDayOfWeeks");
        List<OpeningHoursPerDayOfWeek> result = openingHoursPerDayOfWeekRepository.findAllByOrderByDayOfWeekAsc();
        return result;
    }

    /**
     *  Get one openingHoursPerDayOfWeek by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public OpeningHoursPerDayOfWeek findOne(Long id) {
        log.debug("Request to get OpeningHoursPerDayOfWeek : {}", id);
        OpeningHoursPerDayOfWeek openingHoursPerDayOfWeek = openingHoursPerDayOfWeekRepository.findOne(id);
        return openingHoursPerDayOfWeek;
    }

    /**
     *  Delete the  openingHoursPerDayOfWeek by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OpeningHoursPerDayOfWeek : {}", id);
        openingHoursPerDayOfWeekRepository.delete(id);
    }
}
