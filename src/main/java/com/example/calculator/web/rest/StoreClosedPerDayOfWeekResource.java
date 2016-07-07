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
import com.example.calculator.domain.StoreClosedPerDayOfWeek;
import com.example.calculator.service.StoreClosedPerDayOfWeekService;
import com.example.calculator.web.rest.errors.CustomParameterizedException;
import com.example.calculator.web.rest.util.HeaderUtil;

/**
 * REST controller for managing StoreClosedPerDayOfWeek.
 */
@RestController
@RequestMapping("/api")
public class StoreClosedPerDayOfWeekResource {

    private final Logger log = LoggerFactory.getLogger(StoreClosedPerDayOfWeekResource.class);
        
    @Inject
    private StoreClosedPerDayOfWeekService storeClosedPerDayOfWeekService;
    
    /**
     * POST  /store-closed-per-day-of-weeks : Create a new storeClosedPerDayOfWeek.
     *
     * @param storeClosedPerDayOfWeek the storeClosedPerDayOfWeek to create
     * @return the ResponseEntity with status 201 (Created) and with body the new storeClosedPerDayOfWeek, or with status 400 (Bad Request) if the storeClosedPerDayOfWeek has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/store-closed-per-day-of-weeks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StoreClosedPerDayOfWeek> createStoreClosedPerDayOfWeek(@Valid @RequestBody StoreClosedPerDayOfWeek storeClosedPerDayOfWeek) throws URISyntaxException {
        log.debug("REST request to save StoreClosedPerDayOfWeek : {}", storeClosedPerDayOfWeek);
        if (storeClosedPerDayOfWeek.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("storeClosedPerDayOfWeek", "idexists", "A new storeClosedPerDayOfWeek cannot already have an ID")).body(null);
        }
        
        for (StoreClosedPerDayOfWeek element : storeClosedPerDayOfWeekService.findAll()) {
			if (element.getDayOfWeek().equals(storeClosedPerDayOfWeek.getDayOfWeek())) {
				throw new CustomParameterizedException(storeClosedPerDayOfWeek.getDayOfWeek() + " have already been added as non-working day");		
			}
		}
        
        StoreClosedPerDayOfWeek result = storeClosedPerDayOfWeekService.save(storeClosedPerDayOfWeek);
        return ResponseEntity.created(new URI("/api/store-closed-per-day-of-weeks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("storeClosedPerDayOfWeek", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /store-closed-per-day-of-weeks : Updates an existing storeClosedPerDayOfWeek.
     *
     * @param storeClosedPerDayOfWeek the storeClosedPerDayOfWeek to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated storeClosedPerDayOfWeek,
     * or with status 400 (Bad Request) if the storeClosedPerDayOfWeek is not valid,
     * or with status 500 (Internal Server Error) if the storeClosedPerDayOfWeek couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/store-closed-per-day-of-weeks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StoreClosedPerDayOfWeek> updateStoreClosedPerDayOfWeek(@Valid @RequestBody StoreClosedPerDayOfWeek storeClosedPerDayOfWeek) throws URISyntaxException {
        log.debug("REST request to update StoreClosedPerDayOfWeek : {}", storeClosedPerDayOfWeek);
        if (storeClosedPerDayOfWeek.getId() == null) {
            return createStoreClosedPerDayOfWeek(storeClosedPerDayOfWeek);
        }
        StoreClosedPerDayOfWeek result = storeClosedPerDayOfWeekService.save(storeClosedPerDayOfWeek);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("storeClosedPerDayOfWeek", storeClosedPerDayOfWeek.getId().toString()))
            .body(result);
    }

    /**
     * GET  /store-closed-per-day-of-weeks : get all the storeClosedPerDayOfWeeks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of storeClosedPerDayOfWeeks in body
     */
    @RequestMapping(value = "/store-closed-per-day-of-weeks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<StoreClosedPerDayOfWeek> getAllStoreClosedPerDayOfWeeks() {
        log.debug("REST request to get all StoreClosedPerDayOfWeeks");
        return storeClosedPerDayOfWeekService.findAll();
    }

    /**
     * GET  /store-closed-per-day-of-weeks/:id : get the "id" storeClosedPerDayOfWeek.
     *
     * @param id the id of the storeClosedPerDayOfWeek to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the storeClosedPerDayOfWeek, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/store-closed-per-day-of-weeks/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StoreClosedPerDayOfWeek> getStoreClosedPerDayOfWeek(@PathVariable Long id) {
        log.debug("REST request to get StoreClosedPerDayOfWeek : {}", id);
        StoreClosedPerDayOfWeek storeClosedPerDayOfWeek = storeClosedPerDayOfWeekService.findOne(id);
        return Optional.ofNullable(storeClosedPerDayOfWeek)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /store-closed-per-day-of-weeks/:id : delete the "id" storeClosedPerDayOfWeek.
     *
     * @param id the id of the storeClosedPerDayOfWeek to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/store-closed-per-day-of-weeks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStoreClosedPerDayOfWeek(@PathVariable Long id) {
        log.debug("REST request to delete StoreClosedPerDayOfWeek : {}", id);
        storeClosedPerDayOfWeekService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("storeClosedPerDayOfWeek", id.toString())).build();
    }

}
