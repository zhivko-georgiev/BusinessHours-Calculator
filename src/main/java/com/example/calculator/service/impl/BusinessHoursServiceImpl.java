package com.example.calculator.service.impl;

import com.example.calculator.service.BusinessHoursService;
import com.example.calculator.domain.BusinessHours;
import com.example.calculator.repository.BusinessHoursRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing BusinessHours.
 */
@Service
@Transactional
public class BusinessHoursServiceImpl implements BusinessHoursService{

    private final Logger log = LoggerFactory.getLogger(BusinessHoursServiceImpl.class);
    
    @Inject
    private BusinessHoursRepository businessHoursRepository;
    
    /**
     * Save a businessHours.
     * 
     * @param businessHours the entity to save
     * @return the persisted entity
     */
    public BusinessHours save(BusinessHours businessHours) {
        log.debug("Request to save BusinessHours : {}", businessHours);
        BusinessHours result = businessHoursRepository.save(businessHours);
        return result;
    }

    /**
     *  Get all the businessHours.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<BusinessHours> findAll() {
        log.debug("Request to get all BusinessHours");
        List<BusinessHours> result = businessHoursRepository.findAll();
        return result;
    }

    /**
     *  Get one businessHours by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BusinessHours findOne(Long id) {
        log.debug("Request to get BusinessHours : {}", id);
        BusinessHours businessHours = businessHoursRepository.findOne(id);
        return businessHours;
    }

    /**
     *  Delete the  businessHours by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BusinessHours : {}", id);
        businessHoursRepository.delete(id);
    }
}
