package com.example.calculator.web.rest;

import com.example.calculator.BusinessHoursCalculatorApp;
import com.example.calculator.domain.BusinessHoursCalculator;
import com.example.calculator.repository.BusinessHoursCalculatorRepository;
import com.example.calculator.service.BusinessHoursCalculatorService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the BusinessHoursCalculatorResource REST controller.
 *
 * @see BusinessHoursCalculatorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BusinessHoursCalculatorApp.class)
@WebAppConfiguration
@IntegrationTest
public class BusinessHoursCalculatorResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_TIME_INTERVAL = 1L;
    private static final Long UPDATED_TIME_INTERVAL = 2L;
    private static final String DEFAULT_STARTING_DATE_TIME = "AAAAA";
    private static final String UPDATED_STARTING_DATE_TIME = "BBBBB";

    private static final ZonedDateTime DEFAULT_EXPECTED_PICKUP_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_EXPECTED_PICKUP_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_EXPECTED_PICKUP_TIME_STR = dateTimeFormatter.format(DEFAULT_EXPECTED_PICKUP_TIME);

    @Inject
    private BusinessHoursCalculatorRepository businessHoursCalculatorRepository;

    @Inject
    private BusinessHoursCalculatorService businessHoursCalculatorService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBusinessHoursCalculatorMockMvc;

    private BusinessHoursCalculator businessHoursCalculator;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BusinessHoursCalculatorResource businessHoursCalculatorResource = new BusinessHoursCalculatorResource();
        ReflectionTestUtils.setField(businessHoursCalculatorResource, "businessHoursCalculatorService", businessHoursCalculatorService);
        this.restBusinessHoursCalculatorMockMvc = MockMvcBuilders.standaloneSetup(businessHoursCalculatorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        businessHoursCalculator = new BusinessHoursCalculator();
        businessHoursCalculator.setTimeInterval(DEFAULT_TIME_INTERVAL);
        businessHoursCalculator.setStartingDateTime(DEFAULT_STARTING_DATE_TIME);
        businessHoursCalculator.setExpectedPickupTime(DEFAULT_EXPECTED_PICKUP_TIME);
    }

    @Test
    @Transactional
    public void createBusinessHoursCalculator() throws Exception {
        int databaseSizeBeforeCreate = businessHoursCalculatorRepository.findAll().size();

        // Create the BusinessHoursCalculator

        restBusinessHoursCalculatorMockMvc.perform(post("/api/business-hours-calculators")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(businessHoursCalculator)))
                .andExpect(status().isCreated());

        // Validate the BusinessHoursCalculator in the database
        List<BusinessHoursCalculator> businessHoursCalculators = businessHoursCalculatorRepository.findAll();
        assertThat(businessHoursCalculators).hasSize(databaseSizeBeforeCreate + 1);
        BusinessHoursCalculator testBusinessHoursCalculator = businessHoursCalculators.get(businessHoursCalculators.size() - 1);
        assertThat(testBusinessHoursCalculator.getTimeInterval()).isEqualTo(DEFAULT_TIME_INTERVAL);
        assertThat(testBusinessHoursCalculator.getStartingDateTime()).isEqualTo(DEFAULT_STARTING_DATE_TIME);
        assertThat(testBusinessHoursCalculator.getExpectedPickupTime()).isEqualTo(DEFAULT_EXPECTED_PICKUP_TIME);
    }

    @Test
    @Transactional
    public void checkTimeIntervalIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessHoursCalculatorRepository.findAll().size();
        // set the field null
        businessHoursCalculator.setTimeInterval(null);

        // Create the BusinessHoursCalculator, which fails.

        restBusinessHoursCalculatorMockMvc.perform(post("/api/business-hours-calculators")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(businessHoursCalculator)))
                .andExpect(status().isBadRequest());

        List<BusinessHoursCalculator> businessHoursCalculators = businessHoursCalculatorRepository.findAll();
        assertThat(businessHoursCalculators).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartingDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessHoursCalculatorRepository.findAll().size();
        // set the field null
        businessHoursCalculator.setStartingDateTime(null);

        // Create the BusinessHoursCalculator, which fails.

        restBusinessHoursCalculatorMockMvc.perform(post("/api/business-hours-calculators")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(businessHoursCalculator)))
                .andExpect(status().isBadRequest());

        List<BusinessHoursCalculator> businessHoursCalculators = businessHoursCalculatorRepository.findAll();
        assertThat(businessHoursCalculators).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBusinessHoursCalculators() throws Exception {
        // Initialize the database
        businessHoursCalculatorRepository.saveAndFlush(businessHoursCalculator);

        // Get all the businessHoursCalculators
        restBusinessHoursCalculatorMockMvc.perform(get("/api/business-hours-calculators?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(businessHoursCalculator.getId().intValue())))
                .andExpect(jsonPath("$.[*].timeInterval").value(hasItem(DEFAULT_TIME_INTERVAL.intValue())))
                .andExpect(jsonPath("$.[*].startingDateTime").value(hasItem(DEFAULT_STARTING_DATE_TIME.toString())))
                .andExpect(jsonPath("$.[*].expectedPickupTime").value(hasItem(DEFAULT_EXPECTED_PICKUP_TIME_STR)));
    }

    @Test
    @Transactional
    public void getBusinessHoursCalculator() throws Exception {
        // Initialize the database
        businessHoursCalculatorRepository.saveAndFlush(businessHoursCalculator);

        // Get the businessHoursCalculator
        restBusinessHoursCalculatorMockMvc.perform(get("/api/business-hours-calculators/{id}", businessHoursCalculator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(businessHoursCalculator.getId().intValue()))
            .andExpect(jsonPath("$.timeInterval").value(DEFAULT_TIME_INTERVAL.intValue()))
            .andExpect(jsonPath("$.startingDateTime").value(DEFAULT_STARTING_DATE_TIME.toString()))
            .andExpect(jsonPath("$.expectedPickupTime").value(DEFAULT_EXPECTED_PICKUP_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingBusinessHoursCalculator() throws Exception {
        // Get the businessHoursCalculator
        restBusinessHoursCalculatorMockMvc.perform(get("/api/business-hours-calculators/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBusinessHoursCalculator() throws Exception {
        // Initialize the database
        businessHoursCalculatorService.save(businessHoursCalculator);

        int databaseSizeBeforeUpdate = businessHoursCalculatorRepository.findAll().size();

        // Update the businessHoursCalculator
        BusinessHoursCalculator updatedBusinessHoursCalculator = new BusinessHoursCalculator();
        updatedBusinessHoursCalculator.setId(businessHoursCalculator.getId());
        updatedBusinessHoursCalculator.setTimeInterval(UPDATED_TIME_INTERVAL);
        updatedBusinessHoursCalculator.setStartingDateTime(UPDATED_STARTING_DATE_TIME);
        updatedBusinessHoursCalculator.setExpectedPickupTime(UPDATED_EXPECTED_PICKUP_TIME);

        restBusinessHoursCalculatorMockMvc.perform(put("/api/business-hours-calculators")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBusinessHoursCalculator)))
                .andExpect(status().isOk());

        // Validate the BusinessHoursCalculator in the database
        List<BusinessHoursCalculator> businessHoursCalculators = businessHoursCalculatorRepository.findAll();
        assertThat(businessHoursCalculators).hasSize(databaseSizeBeforeUpdate);
        BusinessHoursCalculator testBusinessHoursCalculator = businessHoursCalculators.get(businessHoursCalculators.size() - 1);
        assertThat(testBusinessHoursCalculator.getTimeInterval()).isEqualTo(UPDATED_TIME_INTERVAL);
        assertThat(testBusinessHoursCalculator.getStartingDateTime()).isEqualTo(UPDATED_STARTING_DATE_TIME);
        assertThat(testBusinessHoursCalculator.getExpectedPickupTime()).isEqualTo(UPDATED_EXPECTED_PICKUP_TIME);
    }

    @Test
    @Transactional
    public void deleteBusinessHoursCalculator() throws Exception {
        // Initialize the database
        businessHoursCalculatorService.save(businessHoursCalculator);

        int databaseSizeBeforeDelete = businessHoursCalculatorRepository.findAll().size();

        // Get the businessHoursCalculator
        restBusinessHoursCalculatorMockMvc.perform(delete("/api/business-hours-calculators/{id}", businessHoursCalculator.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BusinessHoursCalculator> businessHoursCalculators = businessHoursCalculatorRepository.findAll();
        assertThat(businessHoursCalculators).hasSize(databaseSizeBeforeDelete - 1);
    }
}
