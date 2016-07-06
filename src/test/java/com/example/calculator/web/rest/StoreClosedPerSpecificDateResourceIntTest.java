package com.example.calculator.web.rest;

import com.example.calculator.BusinessHoursCalculatorApp;
import com.example.calculator.domain.StoreClosedPerSpecificDate;
import com.example.calculator.repository.StoreClosedPerSpecificDateRepository;
import com.example.calculator.service.StoreClosedPerSpecificDateService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the StoreClosedPerSpecificDateResource REST controller.
 *
 * @see StoreClosedPerSpecificDateResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BusinessHoursCalculatorApp.class)
@WebAppConfiguration
@IntegrationTest
public class StoreClosedPerSpecificDateResourceIntTest {


    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private StoreClosedPerSpecificDateRepository storeClosedPerSpecificDateRepository;

    @Inject
    private StoreClosedPerSpecificDateService storeClosedPerSpecificDateService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStoreClosedPerSpecificDateMockMvc;

    private StoreClosedPerSpecificDate storeClosedPerSpecificDate;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StoreClosedPerSpecificDateResource storeClosedPerSpecificDateResource = new StoreClosedPerSpecificDateResource();
        ReflectionTestUtils.setField(storeClosedPerSpecificDateResource, "storeClosedPerSpecificDateService", storeClosedPerSpecificDateService);
        this.restStoreClosedPerSpecificDateMockMvc = MockMvcBuilders.standaloneSetup(storeClosedPerSpecificDateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        storeClosedPerSpecificDate = new StoreClosedPerSpecificDate();
        storeClosedPerSpecificDate.setDate(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createStoreClosedPerSpecificDate() throws Exception {
        int databaseSizeBeforeCreate = storeClosedPerSpecificDateRepository.findAll().size();

        // Create the StoreClosedPerSpecificDate

        restStoreClosedPerSpecificDateMockMvc.perform(post("/api/store-closed-per-specific-dates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(storeClosedPerSpecificDate)))
                .andExpect(status().isCreated());

        // Validate the StoreClosedPerSpecificDate in the database
        List<StoreClosedPerSpecificDate> storeClosedPerSpecificDates = storeClosedPerSpecificDateRepository.findAll();
        assertThat(storeClosedPerSpecificDates).hasSize(databaseSizeBeforeCreate + 1);
        StoreClosedPerSpecificDate testStoreClosedPerSpecificDate = storeClosedPerSpecificDates.get(storeClosedPerSpecificDates.size() - 1);
        assertThat(testStoreClosedPerSpecificDate.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeClosedPerSpecificDateRepository.findAll().size();
        // set the field null
        storeClosedPerSpecificDate.setDate(null);

        // Create the StoreClosedPerSpecificDate, which fails.

        restStoreClosedPerSpecificDateMockMvc.perform(post("/api/store-closed-per-specific-dates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(storeClosedPerSpecificDate)))
                .andExpect(status().isBadRequest());

        List<StoreClosedPerSpecificDate> storeClosedPerSpecificDates = storeClosedPerSpecificDateRepository.findAll();
        assertThat(storeClosedPerSpecificDates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStoreClosedPerSpecificDates() throws Exception {
        // Initialize the database
        storeClosedPerSpecificDateRepository.saveAndFlush(storeClosedPerSpecificDate);

        // Get all the storeClosedPerSpecificDates
        restStoreClosedPerSpecificDateMockMvc.perform(get("/api/store-closed-per-specific-dates?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(storeClosedPerSpecificDate.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getStoreClosedPerSpecificDate() throws Exception {
        // Initialize the database
        storeClosedPerSpecificDateRepository.saveAndFlush(storeClosedPerSpecificDate);

        // Get the storeClosedPerSpecificDate
        restStoreClosedPerSpecificDateMockMvc.perform(get("/api/store-closed-per-specific-dates/{id}", storeClosedPerSpecificDate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(storeClosedPerSpecificDate.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStoreClosedPerSpecificDate() throws Exception {
        // Get the storeClosedPerSpecificDate
        restStoreClosedPerSpecificDateMockMvc.perform(get("/api/store-closed-per-specific-dates/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStoreClosedPerSpecificDate() throws Exception {
        // Initialize the database
        storeClosedPerSpecificDateService.save(storeClosedPerSpecificDate);

        int databaseSizeBeforeUpdate = storeClosedPerSpecificDateRepository.findAll().size();

        // Update the storeClosedPerSpecificDate
        StoreClosedPerSpecificDate updatedStoreClosedPerSpecificDate = new StoreClosedPerSpecificDate();
        updatedStoreClosedPerSpecificDate.setId(storeClosedPerSpecificDate.getId());
        updatedStoreClosedPerSpecificDate.setDate(UPDATED_DATE);

        restStoreClosedPerSpecificDateMockMvc.perform(put("/api/store-closed-per-specific-dates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedStoreClosedPerSpecificDate)))
                .andExpect(status().isOk());

        // Validate the StoreClosedPerSpecificDate in the database
        List<StoreClosedPerSpecificDate> storeClosedPerSpecificDates = storeClosedPerSpecificDateRepository.findAll();
        assertThat(storeClosedPerSpecificDates).hasSize(databaseSizeBeforeUpdate);
        StoreClosedPerSpecificDate testStoreClosedPerSpecificDate = storeClosedPerSpecificDates.get(storeClosedPerSpecificDates.size() - 1);
        assertThat(testStoreClosedPerSpecificDate.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void deleteStoreClosedPerSpecificDate() throws Exception {
        // Initialize the database
        storeClosedPerSpecificDateService.save(storeClosedPerSpecificDate);

        int databaseSizeBeforeDelete = storeClosedPerSpecificDateRepository.findAll().size();

        // Get the storeClosedPerSpecificDate
        restStoreClosedPerSpecificDateMockMvc.perform(delete("/api/store-closed-per-specific-dates/{id}", storeClosedPerSpecificDate.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<StoreClosedPerSpecificDate> storeClosedPerSpecificDates = storeClosedPerSpecificDateRepository.findAll();
        assertThat(storeClosedPerSpecificDates).hasSize(databaseSizeBeforeDelete - 1);
    }
}
