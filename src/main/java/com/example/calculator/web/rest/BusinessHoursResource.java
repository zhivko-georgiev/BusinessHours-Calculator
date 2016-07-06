package com.example.calculator.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.example.calculator.domain.BusinessHours;
import com.example.calculator.service.BusinessHoursService;
import com.example.calculator.web.rest.util.HeaderUtil;

/**
 * REST controller for managing BusinessHours.
 */
@RestController
@RequestMapping("/api")
public class BusinessHoursResource {

    private final Logger log = LoggerFactory.getLogger(BusinessHoursResource.class);
        
    @Inject
    private BusinessHoursService businessHoursService;
    
    /**
     * POST  /business-hours : Create a new businessHours.
     *
     * @param businessHours the businessHours to create
     * @return the ResponseEntity with status 201 (Created) and with body the new businessHours, or with status 400 (Bad Request) if the businessHours has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/business-hours",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BusinessHours> createBusinessHours(@Valid @RequestBody BusinessHours businessHours) throws URISyntaxException {
        log.debug("REST request to save BusinessHours : {}", businessHours);
        if (businessHours.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("businessHours", "idexists", "A new businessHours cannot already have an ID")).body(null);
        }
        BusinessHours result = businessHoursService.save(businessHours);
        return ResponseEntity.created(new URI("/api/business-hours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("businessHours", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /business-hours : Updates an existing businessHours.
     *
     * @param businessHours the businessHours to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated businessHours,
     * or with status 400 (Bad Request) if the businessHours is not valid,
     * or with status 500 (Internal Server Error) if the businessHours couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/business-hours",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BusinessHours> updateBusinessHours(@Valid @RequestBody BusinessHours businessHours) throws URISyntaxException {
        log.debug("REST request to update BusinessHours : {}", businessHours);
        if (businessHours.getId() == null) {
            return createBusinessHours(businessHours);
        }
        BusinessHours result = businessHoursService.save(businessHours);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("businessHours", businessHours.getId().toString()))
            .body(result);
    }

    /**
     * GET  /business-hours : get all the businessHours.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of businessHours in body
     */
    @RequestMapping(value = "/business-hours",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BusinessHours> getAllBusinessHours() {
        log.debug("REST request to get all BusinessHours");
        return businessHoursService.findAll();
    }

    /**
     * GET  /business-hours/:id : get the "id" businessHours.
     *
     * @param id the id of the businessHours to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the businessHours, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/business-hours/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BusinessHours> getBusinessHours(@PathVariable Long id) {
        log.debug("REST request to get BusinessHours : {}", id);
        BusinessHours businessHours = businessHoursService.findOne(id);
        return Optional.ofNullable(businessHours)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /business-hours/:id : delete the "id" businessHours.
     *
     * @param id the id of the businessHours to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/business-hours/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBusinessHours(@PathVariable Long id) {
        log.debug("REST request to delete BusinessHours : {}", id);
        businessHoursService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("businessHours", id.toString())).build();
    }

}
