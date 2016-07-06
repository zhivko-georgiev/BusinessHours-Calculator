package com.example.calculator.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.example.calculator.domain.OpeningHoursPerDayOfWeek;
import com.example.calculator.service.OpeningHoursPerDayOfWeekService;
import com.example.calculator.web.rest.errors.CustomParameterizedException;
import com.example.calculator.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing OpeningHoursPerDayOfWeek.
 */
@RestController
@RequestMapping("/api")
public class OpeningHoursPerDayOfWeekResource {

    private final Logger log = LoggerFactory.getLogger(OpeningHoursPerDayOfWeekResource.class);
        
    @Inject
    private OpeningHoursPerDayOfWeekService openingHoursPerDayOfWeekService;
    
    /**
     * POST  /opening-hours-per-day-of-weeks : Create a new openingHoursPerDayOfWeek.
     *
     * @param openingHoursPerDayOfWeek the openingHoursPerDayOfWeek to create
     * @return the ResponseEntity with status 201 (Created) and with body the new openingHoursPerDayOfWeek, or with status 400 (Bad Request) if the openingHoursPerDayOfWeek has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/opening-hours-per-day-of-weeks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OpeningHoursPerDayOfWeek> createOpeningHoursPerDayOfWeek(@Valid @RequestBody OpeningHoursPerDayOfWeek openingHoursPerDayOfWeek) throws URISyntaxException {
        log.debug("REST request to save OpeningHoursPerDayOfWeek : {}", openingHoursPerDayOfWeek);
        if (openingHoursPerDayOfWeek.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("openingHoursPerDayOfWeek", "idexists", "A new openingHoursPerDayOfWeek cannot already have an ID")).body(null);
        }
        
        for (OpeningHoursPerDayOfWeek element : openingHoursPerDayOfWeekService.findAll()) {
			if (element.getDayOfWeek().equals(openingHoursPerDayOfWeek.getDayOfWeek())) {
				throw new CustomParameterizedException("Working hours for " + openingHoursPerDayOfWeek.getDayOfWeek() + " have already been added");		
			}
		}
        
        OpeningHoursPerDayOfWeek result = openingHoursPerDayOfWeekService.save(openingHoursPerDayOfWeek);
        return ResponseEntity.created(new URI("/api/opening-hours-per-day-of-weeks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("openingHoursPerDayOfWeek", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /opening-hours-per-day-of-weeks : Updates an existing openingHoursPerDayOfWeek.
     *
     * @param openingHoursPerDayOfWeek the openingHoursPerDayOfWeek to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated openingHoursPerDayOfWeek,
     * or with status 400 (Bad Request) if the openingHoursPerDayOfWeek is not valid,
     * or with status 500 (Internal Server Error) if the openingHoursPerDayOfWeek couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/opening-hours-per-day-of-weeks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OpeningHoursPerDayOfWeek> updateOpeningHoursPerDayOfWeek(@Valid @RequestBody OpeningHoursPerDayOfWeek openingHoursPerDayOfWeek) throws URISyntaxException {
        log.debug("REST request to update OpeningHoursPerDayOfWeek : {}", openingHoursPerDayOfWeek);
        if (openingHoursPerDayOfWeek.getId() == null) {
            return createOpeningHoursPerDayOfWeek(openingHoursPerDayOfWeek);
        }
        OpeningHoursPerDayOfWeek result = openingHoursPerDayOfWeekService.save(openingHoursPerDayOfWeek);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("openingHoursPerDayOfWeek", openingHoursPerDayOfWeek.getId().toString()))
            .body(result);
    }

    /**
     * GET  /opening-hours-per-day-of-weeks : get all the openingHoursPerDayOfWeeks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of openingHoursPerDayOfWeeks in body
     */
    @RequestMapping(value = "/opening-hours-per-day-of-weeks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<OpeningHoursPerDayOfWeek> getAllOpeningHoursPerDayOfWeeks() {
        log.debug("REST request to get all OpeningHoursPerDayOfWeeks");
        return openingHoursPerDayOfWeekService.findAll();
    }

    /**
     * GET  /opening-hours-per-day-of-weeks/:id : get the "id" openingHoursPerDayOfWeek.
     *
     * @param id the id of the openingHoursPerDayOfWeek to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the openingHoursPerDayOfWeek, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/opening-hours-per-day-of-weeks/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OpeningHoursPerDayOfWeek> getOpeningHoursPerDayOfWeek(@PathVariable Long id) {
        log.debug("REST request to get OpeningHoursPerDayOfWeek : {}", id);
        OpeningHoursPerDayOfWeek openingHoursPerDayOfWeek = openingHoursPerDayOfWeekService.findOne(id);
        return Optional.ofNullable(openingHoursPerDayOfWeek)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /opening-hours-per-day-of-weeks/:id : delete the "id" openingHoursPerDayOfWeek.
     *
     * @param id the id of the openingHoursPerDayOfWeek to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/opening-hours-per-day-of-weeks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOpeningHoursPerDayOfWeek(@PathVariable Long id) {
        log.debug("REST request to delete OpeningHoursPerDayOfWeek : {}", id);
        openingHoursPerDayOfWeekService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("openingHoursPerDayOfWeek", id.toString())).build();
    }

}
