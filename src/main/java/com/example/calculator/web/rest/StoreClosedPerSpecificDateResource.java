package com.example.calculator.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.example.calculator.domain.StoreClosedPerSpecificDate;
import com.example.calculator.service.StoreClosedPerSpecificDateService;
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
 * REST controller for managing StoreClosedPerSpecificDate.
 */
@RestController
@RequestMapping("/api")
public class StoreClosedPerSpecificDateResource {

    private final Logger log = LoggerFactory.getLogger(StoreClosedPerSpecificDateResource.class);
        
    @Inject
    private StoreClosedPerSpecificDateService storeClosedPerSpecificDateService;
    
    /**
     * POST  /store-closed-per-specific-dates : Create a new storeClosedPerSpecificDate.
     *
     * @param storeClosedPerSpecificDate the storeClosedPerSpecificDate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new storeClosedPerSpecificDate, or with status 400 (Bad Request) if the storeClosedPerSpecificDate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/store-closed-per-specific-dates",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StoreClosedPerSpecificDate> createStoreClosedPerSpecificDate(@Valid @RequestBody StoreClosedPerSpecificDate storeClosedPerSpecificDate) throws URISyntaxException {
        log.debug("REST request to save StoreClosedPerSpecificDate : {}", storeClosedPerSpecificDate);
        if (storeClosedPerSpecificDate.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("storeClosedPerSpecificDate", "idexists", "A new storeClosedPerSpecificDate cannot already have an ID")).body(null);
        }
        
        for (StoreClosedPerSpecificDate element : storeClosedPerSpecificDateService.findAll()) {
			if (element.getDate().equals(storeClosedPerSpecificDate.getDate())) {
				throw new CustomParameterizedException(storeClosedPerSpecificDate.getDate() + " have already been added as non-working day");		
			}
		}
        
        StoreClosedPerSpecificDate result = storeClosedPerSpecificDateService.save(storeClosedPerSpecificDate);
        return ResponseEntity.created(new URI("/api/store-closed-per-specific-dates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("storeClosedPerSpecificDate", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /store-closed-per-specific-dates : Updates an existing storeClosedPerSpecificDate.
     *
     * @param storeClosedPerSpecificDate the storeClosedPerSpecificDate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated storeClosedPerSpecificDate,
     * or with status 400 (Bad Request) if the storeClosedPerSpecificDate is not valid,
     * or with status 500 (Internal Server Error) if the storeClosedPerSpecificDate couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/store-closed-per-specific-dates",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StoreClosedPerSpecificDate> updateStoreClosedPerSpecificDate(@Valid @RequestBody StoreClosedPerSpecificDate storeClosedPerSpecificDate) throws URISyntaxException {
        log.debug("REST request to update StoreClosedPerSpecificDate : {}", storeClosedPerSpecificDate);
        if (storeClosedPerSpecificDate.getId() == null) {
            return createStoreClosedPerSpecificDate(storeClosedPerSpecificDate);
        }
        StoreClosedPerSpecificDate result = storeClosedPerSpecificDateService.save(storeClosedPerSpecificDate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("storeClosedPerSpecificDate", storeClosedPerSpecificDate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /store-closed-per-specific-dates : get all the storeClosedPerSpecificDates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of storeClosedPerSpecificDates in body
     */
    @RequestMapping(value = "/store-closed-per-specific-dates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<StoreClosedPerSpecificDate> getAllStoreClosedPerSpecificDates() {
        log.debug("REST request to get all StoreClosedPerSpecificDates");
        return storeClosedPerSpecificDateService.findAll();
    }

    /**
     * GET  /store-closed-per-specific-dates/:id : get the "id" storeClosedPerSpecificDate.
     *
     * @param id the id of the storeClosedPerSpecificDate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the storeClosedPerSpecificDate, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/store-closed-per-specific-dates/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StoreClosedPerSpecificDate> getStoreClosedPerSpecificDate(@PathVariable Long id) {
        log.debug("REST request to get StoreClosedPerSpecificDate : {}", id);
        StoreClosedPerSpecificDate storeClosedPerSpecificDate = storeClosedPerSpecificDateService.findOne(id);
        return Optional.ofNullable(storeClosedPerSpecificDate)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /store-closed-per-specific-dates/:id : delete the "id" storeClosedPerSpecificDate.
     *
     * @param id the id of the storeClosedPerSpecificDate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/store-closed-per-specific-dates/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStoreClosedPerSpecificDate(@PathVariable Long id) {
        log.debug("REST request to delete StoreClosedPerSpecificDate : {}", id);
        storeClosedPerSpecificDateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("storeClosedPerSpecificDate", id.toString())).build();
    }

}
