package com.example.calculator.web.rest;

import com.example.calculator.BusinessHoursCalculatorApp;
import com.example.calculator.domain.OpeningHoursPerSpecificDate;
import com.example.calculator.repository.OpeningHoursPerSpecificDateRepository;
import com.example.calculator.service.OpeningHoursPerSpecificDateService;

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
 * Test class for the OpeningHoursPerSpecificDateResource REST controller.
 *
 * @see OpeningHoursPerSpecificDateResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BusinessHoursCalculatorApp.class)
@WebAppConfiguration
@IntegrationTest
public class OpeningHoursPerSpecificDateResourceIntTest {


    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_OPENING_HOURS = "09:30";
    private static final String UPDATED_OPENING_HOURS = "10:30";
    private static final String DEFAULT_CLOSING_HOURS = "17:30";
    private static final String UPDATED_CLOSING_HOURS = "18:30";

    @Inject
    private OpeningHoursPerSpecificDateRepository openingHoursPerSpecificDateRepository;

    @Inject
    private OpeningHoursPerSpecificDateService openingHoursPerSpecificDateService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOpeningHoursPerSpecificDateMockMvc;

    private OpeningHoursPerSpecificDate openingHoursPerSpecificDate;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OpeningHoursPerSpecificDateResource openingHoursPerSpecificDateResource = new OpeningHoursPerSpecificDateResource();
        ReflectionTestUtils.setField(openingHoursPerSpecificDateResource, "openingHoursPerSpecificDateService", openingHoursPerSpecificDateService);
        this.restOpeningHoursPerSpecificDateMockMvc = MockMvcBuilders.standaloneSetup(openingHoursPerSpecificDateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        openingHoursPerSpecificDate = new OpeningHoursPerSpecificDate();
        openingHoursPerSpecificDate.setDate(DEFAULT_DATE);
        openingHoursPerSpecificDate.setOpeningHours(DEFAULT_OPENING_HOURS);
        openingHoursPerSpecificDate.setClosingHours(DEFAULT_CLOSING_HOURS);
    }

    @Test
    @Transactional
    public void createOpeningHoursPerSpecificDate() throws Exception {
        int databaseSizeBeforeCreate = openingHoursPerSpecificDateRepository.findAll().size();

        // Create the OpeningHoursPerSpecificDate

        restOpeningHoursPerSpecificDateMockMvc.perform(post("/api/opening-hours-per-specific-dates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(openingHoursPerSpecificDate)))
                .andExpect(status().isCreated());

        // Validate the OpeningHoursPerSpecificDate in the database
        List<OpeningHoursPerSpecificDate> openingHoursPerSpecificDates = openingHoursPerSpecificDateRepository.findAll();
        assertThat(openingHoursPerSpecificDates).hasSize(databaseSizeBeforeCreate + 1);
        OpeningHoursPerSpecificDate testOpeningHoursPerSpecificDate = openingHoursPerSpecificDates.get(openingHoursPerSpecificDates.size() - 1);
        assertThat(testOpeningHoursPerSpecificDate.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testOpeningHoursPerSpecificDate.getOpeningHours()).isEqualTo(DEFAULT_OPENING_HOURS);
        assertThat(testOpeningHoursPerSpecificDate.getClosingHours()).isEqualTo(DEFAULT_CLOSING_HOURS);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = openingHoursPerSpecificDateRepository.findAll().size();
        // set the field null
        openingHoursPerSpecificDate.setDate(null);

        // Create the OpeningHoursPerSpecificDate, which fails.

        restOpeningHoursPerSpecificDateMockMvc.perform(post("/api/opening-hours-per-specific-dates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(openingHoursPerSpecificDate)))
                .andExpect(status().isBadRequest());

        List<OpeningHoursPerSpecificDate> openingHoursPerSpecificDates = openingHoursPerSpecificDateRepository.findAll();
        assertThat(openingHoursPerSpecificDates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOpeningHoursIsRequired() throws Exception {
        int databaseSizeBeforeTest = openingHoursPerSpecificDateRepository.findAll().size();
        // set the field null
        openingHoursPerSpecificDate.setOpeningHours(null);

        // Create the OpeningHoursPerSpecificDate, which fails.

        restOpeningHoursPerSpecificDateMockMvc.perform(post("/api/opening-hours-per-specific-dates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(openingHoursPerSpecificDate)))
                .andExpect(status().isBadRequest());

        List<OpeningHoursPerSpecificDate> openingHoursPerSpecificDates = openingHoursPerSpecificDateRepository.findAll();
        assertThat(openingHoursPerSpecificDates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClosingHoursIsRequired() throws Exception {
        int databaseSizeBeforeTest = openingHoursPerSpecificDateRepository.findAll().size();
        // set the field null
        openingHoursPerSpecificDate.setClosingHours(null);

        // Create the OpeningHoursPerSpecificDate, which fails.

        restOpeningHoursPerSpecificDateMockMvc.perform(post("/api/opening-hours-per-specific-dates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(openingHoursPerSpecificDate)))
                .andExpect(status().isBadRequest());

        List<OpeningHoursPerSpecificDate> openingHoursPerSpecificDates = openingHoursPerSpecificDateRepository.findAll();
        assertThat(openingHoursPerSpecificDates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOpeningHoursPerSpecificDates() throws Exception {
        // Initialize the database
        openingHoursPerSpecificDateRepository.saveAndFlush(openingHoursPerSpecificDate);

        // Get all the openingHoursPerSpecificDates
        restOpeningHoursPerSpecificDateMockMvc.perform(get("/api/opening-hours-per-specific-dates?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(openingHoursPerSpecificDate.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].openingHours").value(hasItem(DEFAULT_OPENING_HOURS.toString())))
                .andExpect(jsonPath("$.[*].closingHours").value(hasItem(DEFAULT_CLOSING_HOURS.toString())));
    }

    @Test
    @Transactional
    public void getOpeningHoursPerSpecificDate() throws Exception {
        // Initialize the database
        openingHoursPerSpecificDateRepository.saveAndFlush(openingHoursPerSpecificDate);

        // Get the openingHoursPerSpecificDate
        restOpeningHoursPerSpecificDateMockMvc.perform(get("/api/opening-hours-per-specific-dates/{id}", openingHoursPerSpecificDate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(openingHoursPerSpecificDate.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.openingHours").value(DEFAULT_OPENING_HOURS.toString()))
            .andExpect(jsonPath("$.closingHours").value(DEFAULT_CLOSING_HOURS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOpeningHoursPerSpecificDate() throws Exception {
        // Get the openingHoursPerSpecificDate
        restOpeningHoursPerSpecificDateMockMvc.perform(get("/api/opening-hours-per-specific-dates/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOpeningHoursPerSpecificDate() throws Exception {
        // Initialize the database
        openingHoursPerSpecificDateService.save(openingHoursPerSpecificDate);

        int databaseSizeBeforeUpdate = openingHoursPerSpecificDateRepository.findAll().size();

        // Update the openingHoursPerSpecificDate
        OpeningHoursPerSpecificDate updatedOpeningHoursPerSpecificDate = new OpeningHoursPerSpecificDate();
        updatedOpeningHoursPerSpecificDate.setId(openingHoursPerSpecificDate.getId());
        updatedOpeningHoursPerSpecificDate.setDate(UPDATED_DATE);
        updatedOpeningHoursPerSpecificDate.setOpeningHours(UPDATED_OPENING_HOURS);
        updatedOpeningHoursPerSpecificDate.setClosingHours(UPDATED_CLOSING_HOURS);

        restOpeningHoursPerSpecificDateMockMvc.perform(put("/api/opening-hours-per-specific-dates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOpeningHoursPerSpecificDate)))
                .andExpect(status().isOk());

        // Validate the OpeningHoursPerSpecificDate in the database
        List<OpeningHoursPerSpecificDate> openingHoursPerSpecificDates = openingHoursPerSpecificDateRepository.findAll();
        assertThat(openingHoursPerSpecificDates).hasSize(databaseSizeBeforeUpdate);
        OpeningHoursPerSpecificDate testOpeningHoursPerSpecificDate = openingHoursPerSpecificDates.get(openingHoursPerSpecificDates.size() - 1);
        assertThat(testOpeningHoursPerSpecificDate.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOpeningHoursPerSpecificDate.getOpeningHours()).isEqualTo(UPDATED_OPENING_HOURS);
        assertThat(testOpeningHoursPerSpecificDate.getClosingHours()).isEqualTo(UPDATED_CLOSING_HOURS);
    }

    @Test
    @Transactional
    public void deleteOpeningHoursPerSpecificDate() throws Exception {
        // Initialize the database
        openingHoursPerSpecificDateService.save(openingHoursPerSpecificDate);

        int databaseSizeBeforeDelete = openingHoursPerSpecificDateRepository.findAll().size();

        // Get the openingHoursPerSpecificDate
        restOpeningHoursPerSpecificDateMockMvc.perform(delete("/api/opening-hours-per-specific-dates/{id}", openingHoursPerSpecificDate.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OpeningHoursPerSpecificDate> openingHoursPerSpecificDates = openingHoursPerSpecificDateRepository.findAll();
        assertThat(openingHoursPerSpecificDates).hasSize(databaseSizeBeforeDelete - 1);
    }
}
