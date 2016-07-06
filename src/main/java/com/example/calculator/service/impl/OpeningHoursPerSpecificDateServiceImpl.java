package com.example.calculator.service.impl;

import com.example.calculator.service.OpeningHoursPerSpecificDateService;
import com.example.calculator.domain.OpeningHoursPerSpecificDate;
import com.example.calculator.repository.OpeningHoursPerSpecificDateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing OpeningHoursPerSpecificDate.
 */
@Service
@Transactional
public class OpeningHoursPerSpecificDateServiceImpl implements OpeningHoursPerSpecificDateService{

    private final Logger log = LoggerFactory.getLogger(OpeningHoursPerSpecificDateServiceImpl.class);
    
    @Inject
    private OpeningHoursPerSpecificDateRepository openingHoursPerSpecificDateRepository;
    
    /**
     * Save a openingHoursPerSpecificDate.
     * 
     * @param openingHoursPerSpecificDate the entity to save
     * @return the persisted entity
     */
    public OpeningHoursPerSpecificDate save(OpeningHoursPerSpecificDate openingHoursPerSpecificDate) {
        log.debug("Request to save OpeningHoursPerSpecificDate : {}", openingHoursPerSpecificDate);
        OpeningHoursPerSpecificDate result = openingHoursPerSpecificDateRepository.save(openingHoursPerSpecificDate);
        return result;
    }

    /**
     *  Get all the openingHoursPerSpecificDates.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<OpeningHoursPerSpecificDate> findAll() {
        log.debug("Request to get all OpeningHoursPerSpecificDates");
        List<OpeningHoursPerSpecificDate> result = openingHoursPerSpecificDateRepository.findAllByOrderByDateAsc();
        return result;
    }

    /**
     *  Get one openingHoursPerSpecificDate by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public OpeningHoursPerSpecificDate findOne(Long id) {
        log.debug("Request to get OpeningHoursPerSpecificDate : {}", id);
        OpeningHoursPerSpecificDate openingHoursPerSpecificDate = openingHoursPerSpecificDateRepository.findOne(id);
        return openingHoursPerSpecificDate;
    }

    /**
     *  Delete the  openingHoursPerSpecificDate by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OpeningHoursPerSpecificDate : {}", id);
        openingHoursPerSpecificDateRepository.delete(id);
    }
}
