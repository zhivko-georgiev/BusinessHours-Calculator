package com.example.calculator.web.rest;

import com.example.calculator.BusinessHoursCalculatorApp;
import com.example.calculator.domain.BusinessHours;
import com.example.calculator.repository.BusinessHoursRepository;
import com.example.calculator.service.BusinessHoursService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the BusinessHoursResource REST controller.
 *
 * @see BusinessHoursResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BusinessHoursCalculatorApp.class)
@WebAppConfiguration
@IntegrationTest
public class BusinessHoursResourceIntTest {

    private static final String DEFAULT_DEFAULT_OPENING_HOURS = "AAAAA";
    private static final String UPDATED_DEFAULT_OPENING_HOURS = "BBBBB";
    private static final String DEFAULT_DEFAULT_CLOSING_HOURS = "AAAAA";
    private static final String UPDATED_DEFAULT_CLOSING_HOURS = "BBBBB";

    @Inject
    private BusinessHoursRepository businessHoursRepository;

    @Inject
    private BusinessHoursService businessHoursService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBusinessHoursMockMvc;

    private BusinessHours businessHours;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BusinessHoursResource businessHoursResource = new BusinessHoursResource();
        ReflectionTestUtils.setField(businessHoursResource, "businessHoursService", businessHoursService);
        this.restBusinessHoursMockMvc = MockMvcBuilders.standaloneSetup(businessHoursResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        businessHours = new BusinessHours();
        businessHours.setDefaultOpeningHours(DEFAULT_DEFAULT_OPENING_HOURS);
        businessHours.setDefaultClosingHours(DEFAULT_DEFAULT_CLOSING_HOURS);
    }

    @Test
    @Transactional
    public void createBusinessHours() throws Exception {
        int databaseSizeBeforeCreate = businessHoursRepository.findAll().size();

        // Create the BusinessHours

        restBusinessHoursMockMvc.perform(post("/api/business-hours")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(businessHours)))
                .andExpect(status().isCreated());

        // Validate the BusinessHours in the database
        List<BusinessHours> businessHours = businessHoursRepository.findAll();
        assertThat(businessHours).hasSize(databaseSizeBeforeCreate + 1);
        BusinessHours testBusinessHours = businessHours.get(businessHours.size() - 1);
        assertThat(testBusinessHours.getDefaultOpeningHours()).isEqualTo(DEFAULT_DEFAULT_OPENING_HOURS);
        assertThat(testBusinessHours.getDefaultClosingHours()).isEqualTo(DEFAULT_DEFAULT_CLOSING_HOURS);
    }

    @Test
    @Transactional
    public void checkDefaultOpeningHoursIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessHoursRepository.findAll().size();
        // set the field null
        businessHours.setDefaultOpeningHours(null);

        // Create the BusinessHours, which fails.

        restBusinessHoursMockMvc.perform(post("/api/business-hours")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(businessHours)))
                .andExpect(status().isBadRequest());

        List<BusinessHours> businessHours = businessHoursRepository.findAll();
        assertThat(businessHours).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDefaultClosingHoursIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessHoursRepository.findAll().size();
        // set the field null
        businessHours.setDefaultClosingHours(null);

        // Create the BusinessHours, which fails.

        restBusinessHoursMockMvc.perform(post("/api/business-hours")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(businessHours)))
                .andExpect(status().isBadRequest());

        List<BusinessHours> businessHours = businessHoursRepository.findAll();
        assertThat(businessHours).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBusinessHours() throws Exception {
        // Initialize the database
        businessHoursRepository.saveAndFlush(businessHours);

        // Get all the businessHours
        restBusinessHoursMockMvc.perform(get("/api/business-hours?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(businessHours.getId().intValue())))
                .andExpect(jsonPath("$.[*].defaultOpeningHours").value(hasItem(DEFAULT_DEFAULT_OPENING_HOURS.toString())))
                .andExpect(jsonPath("$.[*].defaultClosingHours").value(hasItem(DEFAULT_DEFAULT_CLOSING_HOURS.toString())));
    }

    @Test
    @Transactional
    public void getBusinessHours() throws Exception {
        // Initialize the database
        businessHoursRepository.saveAndFlush(businessHours);

        // Get the businessHours
        restBusinessHoursMockMvc.perform(get("/api/business-hours/{id}", businessHours.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(businessHours.getId().intValue()))
            .andExpect(jsonPath("$.defaultOpeningHours").value(DEFAULT_DEFAULT_OPENING_HOURS.toString()))
            .andExpect(jsonPath("$.defaultClosingHours").value(DEFAULT_DEFAULT_CLOSING_HOURS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBusinessHours() throws Exception {
        // Get the businessHours
        restBusinessHoursMockMvc.perform(get("/api/business-hours/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBusinessHours() throws Exception {
        // Initialize the database
        businessHoursService.save(businessHours);

        int databaseSizeBeforeUpdate = businessHoursRepository.findAll().size();

        // Update the businessHours
        BusinessHours updatedBusinessHours = new BusinessHours();
        updatedBusinessHours.setId(businessHours.getId());
        updatedBusinessHours.setDefaultOpeningHours(UPDATED_DEFAULT_OPENING_HOURS);
        updatedBusinessHours.setDefaultClosingHours(UPDATED_DEFAULT_CLOSING_HOURS);

        restBusinessHoursMockMvc.perform(put("/api/business-hours")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBusinessHours)))
                .andExpect(status().isOk());

        // Validate the BusinessHours in the database
        List<BusinessHours> businessHours = businessHoursRepository.findAll();
        assertThat(businessHours).hasSize(databaseSizeBeforeUpdate);
        BusinessHours testBusinessHours = businessHours.get(businessHours.size() - 1);
        assertThat(testBusinessHours.getDefaultOpeningHours()).isEqualTo(UPDATED_DEFAULT_OPENING_HOURS);
        assertThat(testBusinessHours.getDefaultClosingHours()).isEqualTo(UPDATED_DEFAULT_CLOSING_HOURS);
    }

    @Test
    @Transactional
    public void deleteBusinessHours() throws Exception {
        // Initialize the database
        businessHoursService.save(businessHours);

        int databaseSizeBeforeDelete = businessHoursRepository.findAll().size();

        // Get the businessHours
        restBusinessHoursMockMvc.perform(delete("/api/business-hours/{id}", businessHours.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BusinessHours> businessHours = businessHoursRepository.findAll();
        assertThat(businessHours).hasSize(databaseSizeBeforeDelete - 1);
    }
}
