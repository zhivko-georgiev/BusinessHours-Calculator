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
import com.example.calculator.domain.OpeningHoursPerSpecificDate;
import com.example.calculator.service.OpeningHoursPerSpecificDateService;
import com.example.calculator.web.rest.errors.CustomParameterizedException;
import com.example.calculator.web.rest.util.HeaderUtil;

/**
 * REST controller for managing OpeningHoursPerSpecificDate.
 */
@RestController
@RequestMapping("/api")
public class OpeningHoursPerSpecificDateResource {

    private final Logger log = LoggerFactory.getLogger(OpeningHoursPerSpecificDateResource.class);
        
    @Inject
    private OpeningHoursPerSpecificDateService openingHoursPerSpecificDateService;
    
    /**
     * POST  /opening-hours-per-specific-dates : Create a new openingHoursPerSpecificDate.
     *
     * @param openingHoursPerSpecificDate the openingHoursPerSpecificDate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new openingHoursPerSpecificDate, or with status 400 (Bad Request) if the openingHoursPerSpecificDate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/opening-hours-per-specific-dates",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OpeningHoursPerSpecificDate> createOpeningHoursPerSpecificDate(@Valid @RequestBody OpeningHoursPerSpecificDate openingHoursPerSpecificDate) throws URISyntaxException {
        log.debug("REST request to save OpeningHoursPerSpecificDate : {}", openingHoursPerSpecificDate);
        if (openingHoursPerSpecificDate.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("openingHoursPerSpecificDate", "idexists", "A new openingHoursPerSpecificDate cannot already have an ID")).body(null);
        }
        
        for (OpeningHoursPerSpecificDate element : openingHoursPerSpecificDateService.findAll()) {
			if (element.getDate().equals(openingHoursPerSpecificDate.getDate())) {
				throw new CustomParameterizedException("Working hours for " + openingHoursPerSpecificDate.getDate() + " have already been added");		
			}
		}
        
        OpeningHoursPerSpecificDate result = openingHoursPerSpecificDateService.save(openingHoursPerSpecificDate);
        return ResponseEntity.created(new URI("/api/opening-hours-per-specific-dates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("openingHoursPerSpecificDate", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /opening-hours-per-specific-dates : Updates an existing openingHoursPerSpecificDate.
     *
     * @param openingHoursPerSpecificDate the openingHoursPerSpecificDate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated openingHoursPerSpecificDate,
     * or with status 400 (Bad Request) if the openingHoursPerSpecificDate is not valid,
     * or with status 500 (Internal Server Error) if the openingHoursPerSpecificDate couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/opening-hours-per-specific-dates",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OpeningHoursPerSpecificDate> updateOpeningHoursPerSpecificDate(@Valid @RequestBody OpeningHoursPerSpecificDate openingHoursPerSpecificDate) throws URISyntaxException {
        log.debug("REST request to update OpeningHoursPerSpecificDate : {}", openingHoursPerSpecificDate);
        if (openingHoursPerSpecificDate.getId() == null) {
            return createOpeningHoursPerSpecificDate(openingHoursPerSpecificDate);
        }
        OpeningHoursPerSpecificDate result = openingHoursPerSpecificDateService.save(openingHoursPerSpecificDate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("openingHoursPerSpecificDate", openingHoursPerSpecificDate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /opening-hours-per-specific-dates : get all the openingHoursPerSpecificDates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of openingHoursPerSpecificDates in body
     */
    @RequestMapping(value = "/opening-hours-per-specific-dates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<OpeningHoursPerSpecificDate> getAllOpeningHoursPerSpecificDates() {
        log.debug("REST request to get all OpeningHoursPerSpecificDates");
        return openingHoursPerSpecificDateService.findAll();
    }

    /**
     * GET  /opening-hours-per-specific-dates/:id : get the "id" openingHoursPerSpecificDate.
     *
     * @param id the id of the openingHoursPerSpecificDate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the openingHoursPerSpecificDate, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/opening-hours-per-specific-dates/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OpeningHoursPerSpecificDate> getOpeningHoursPerSpecificDate(@PathVariable Long id) {
        log.debug("REST request to get OpeningHoursPerSpecificDate : {}", id);
        OpeningHoursPerSpecificDate openingHoursPerSpecificDate = openingHoursPerSpecificDateService.findOne(id);
        return Optional.ofNullable(openingHoursPerSpecificDate)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /opening-hours-per-specific-dates/:id : delete the "id" openingHoursPerSpecificDate.
     *
     * @param id the id of the openingHoursPerSpecificDate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/opening-hours-per-specific-dates/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOpeningHoursPerSpecificDate(@PathVariable Long id) {
        log.debug("REST request to delete OpeningHoursPerSpecificDate : {}", id);
        openingHoursPerSpecificDateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("openingHoursPerSpecificDate", id.toString())).build();
    }

}
