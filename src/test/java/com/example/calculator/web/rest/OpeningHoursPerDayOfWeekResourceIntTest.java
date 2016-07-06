package com.example.calculator.web.rest;

import com.example.calculator.BusinessHoursCalculatorApp;
import com.example.calculator.domain.OpeningHoursPerDayOfWeek;
import com.example.calculator.repository.OpeningHoursPerDayOfWeekRepository;
import com.example.calculator.service.OpeningHoursPerDayOfWeekService;

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

import com.example.calculator.domain.enumeration.DayOfWeek;

/**
 * Test class for the OpeningHoursPerDayOfWeekResource REST controller.
 *
 * @see OpeningHoursPerDayOfWeekResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BusinessHoursCalculatorApp.class)
@WebAppConfiguration
@IntegrationTest
public class OpeningHoursPerDayOfWeekResourceIntTest {


    private static final DayOfWeek DEFAULT_DAY_OF_WEEK = DayOfWeek.MONDAY;
    private static final DayOfWeek UPDATED_DAY_OF_WEEK = DayOfWeek.TUESDAY;
    private static final String DEFAULT_OPENING_HOURS = "AAAAA";
    private static final String UPDATED_OPENING_HOURS = "BBBBB";
    private static final String DEFAULT_CLOSING_HOURS = "AAAAA";
    private static final String UPDATED_CLOSING_HOURS = "BBBBB";

    @Inject
    private OpeningHoursPerDayOfWeekRepository openingHoursPerDayOfWeekRepository;

    @Inject
    private OpeningHoursPerDayOfWeekService openingHoursPerDayOfWeekService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOpeningHoursPerDayOfWeekMockMvc;

    private OpeningHoursPerDayOfWeek openingHoursPerDayOfWeek;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OpeningHoursPerDayOfWeekResource openingHoursPerDayOfWeekResource = new OpeningHoursPerDayOfWeekResource();
        ReflectionTestUtils.setField(openingHoursPerDayOfWeekResource, "openingHoursPerDayOfWeekService", openingHoursPerDayOfWeekService);
        this.restOpeningHoursPerDayOfWeekMockMvc = MockMvcBuilders.standaloneSetup(openingHoursPerDayOfWeekResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        openingHoursPerDayOfWeek = new OpeningHoursPerDayOfWeek();
        openingHoursPerDayOfWeek.setDayOfWeek(DEFAULT_DAY_OF_WEEK);
        openingHoursPerDayOfWeek.setOpeningHours(DEFAULT_OPENING_HOURS);
        openingHoursPerDayOfWeek.setClosingHours(DEFAULT_CLOSING_HOURS);
    }

    @Test
    @Transactional
    public void createOpeningHoursPerDayOfWeek() throws Exception {
        int databaseSizeBeforeCreate = openingHoursPerDayOfWeekRepository.findAll().size();

        // Create the OpeningHoursPerDayOfWeek

        restOpeningHoursPerDayOfWeekMockMvc.perform(post("/api/opening-hours-per-day-of-weeks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(openingHoursPerDayOfWeek)))
                .andExpect(status().isCreated());

        // Validate the OpeningHoursPerDayOfWeek in the database
        List<OpeningHoursPerDayOfWeek> openingHoursPerDayOfWeeks = openingHoursPerDayOfWeekRepository.findAll();
        assertThat(openingHoursPerDayOfWeeks).hasSize(databaseSizeBeforeCreate + 1);
        OpeningHoursPerDayOfWeek testOpeningHoursPerDayOfWeek = openingHoursPerDayOfWeeks.get(openingHoursPerDayOfWeeks.size() - 1);
        assertThat(testOpeningHoursPerDayOfWeek.getDayOfWeek()).isEqualTo(DEFAULT_DAY_OF_WEEK);
        assertThat(testOpeningHoursPerDayOfWeek.getOpeningHours()).isEqualTo(DEFAULT_OPENING_HOURS);
        assertThat(testOpeningHoursPerDayOfWeek.getClosingHours()).isEqualTo(DEFAULT_CLOSING_HOURS);
    }

    @Test
    @Transactional
    public void checkDayOfWeekIsRequired() throws Exception {
        int databaseSizeBeforeTest = openingHoursPerDayOfWeekRepository.findAll().size();
        // set the field null
        openingHoursPerDayOfWeek.setDayOfWeek(null);

        // Create the OpeningHoursPerDayOfWeek, which fails.

        restOpeningHoursPerDayOfWeekMockMvc.perform(post("/api/opening-hours-per-day-of-weeks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(openingHoursPerDayOfWeek)))
                .andExpect(status().isBadRequest());

        List<OpeningHoursPerDayOfWeek> openingHoursPerDayOfWeeks = openingHoursPerDayOfWeekRepository.findAll();
        assertThat(openingHoursPerDayOfWeeks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOpeningHoursIsRequired() throws Exception {
        int databaseSizeBeforeTest = openingHoursPerDayOfWeekRepository.findAll().size();
        // set the field null
        openingHoursPerDayOfWeek.setOpeningHours(null);

        // Create the OpeningHoursPerDayOfWeek, which fails.

        restOpeningHoursPerDayOfWeekMockMvc.perform(post("/api/opening-hours-per-day-of-weeks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(openingHoursPerDayOfWeek)))
                .andExpect(status().isBadRequest());

        List<OpeningHoursPerDayOfWeek> openingHoursPerDayOfWeeks = openingHoursPerDayOfWeekRepository.findAll();
        assertThat(openingHoursPerDayOfWeeks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClosingHoursIsRequired() throws Exception {
        int databaseSizeBeforeTest = openingHoursPerDayOfWeekRepository.findAll().size();
        // set the field null
        openingHoursPerDayOfWeek.setClosingHours(null);

        // Create the OpeningHoursPerDayOfWeek, which fails.

        restOpeningHoursPerDayOfWeekMockMvc.perform(post("/api/opening-hours-per-day-of-weeks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(openingHoursPerDayOfWeek)))
                .andExpect(status().isBadRequest());

        List<OpeningHoursPerDayOfWeek> openingHoursPerDayOfWeeks = openingHoursPerDayOfWeekRepository.findAll();
        assertThat(openingHoursPerDayOfWeeks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOpeningHoursPerDayOfWeeks() throws Exception {
        // Initialize the database
        openingHoursPerDayOfWeekRepository.saveAndFlush(openingHoursPerDayOfWeek);

        // Get all the openingHoursPerDayOfWeeks
        restOpeningHoursPerDayOfWeekMockMvc.perform(get("/api/opening-hours-per-day-of-weeks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(openingHoursPerDayOfWeek.getId().intValue())))
                .andExpect(jsonPath("$.[*].dayOfWeek").value(hasItem(DEFAULT_DAY_OF_WEEK.toString())))
                .andExpect(jsonPath("$.[*].openingHours").value(hasItem(DEFAULT_OPENING_HOURS.toString())))
                .andExpect(jsonPath("$.[*].closingHours").value(hasItem(DEFAULT_CLOSING_HOURS.toString())));
    }

    @Test
    @Transactional
    public void getOpeningHoursPerDayOfWeek() throws Exception {
        // Initialize the database
        openingHoursPerDayOfWeekRepository.saveAndFlush(openingHoursPerDayOfWeek);

        // Get the openingHoursPerDayOfWeek
        restOpeningHoursPerDayOfWeekMockMvc.perform(get("/api/opening-hours-per-day-of-weeks/{id}", openingHoursPerDayOfWeek.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(openingHoursPerDayOfWeek.getId().intValue()))
            .andExpect(jsonPath("$.dayOfWeek").value(DEFAULT_DAY_OF_WEEK.toString()))
            .andExpect(jsonPath("$.openingHours").value(DEFAULT_OPENING_HOURS.toString()))
            .andExpect(jsonPath("$.closingHours").value(DEFAULT_CLOSING_HOURS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOpeningHoursPerDayOfWeek() throws Exception {
        // Get the openingHoursPerDayOfWeek
        restOpeningHoursPerDayOfWeekMockMvc.perform(get("/api/opening-hours-per-day-of-weeks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOpeningHoursPerDayOfWeek() throws Exception {
        // Initialize the database
        openingHoursPerDayOfWeekService.save(openingHoursPerDayOfWeek);

        int databaseSizeBeforeUpdate = openingHoursPerDayOfWeekRepository.findAll().size();

        // Update the openingHoursPerDayOfWeek
        OpeningHoursPerDayOfWeek updatedOpeningHoursPerDayOfWeek = new OpeningHoursPerDayOfWeek();
        updatedOpeningHoursPerDayOfWeek.setId(openingHoursPerDayOfWeek.getId());
        updatedOpeningHoursPerDayOfWeek.setDayOfWeek(UPDATED_DAY_OF_WEEK);
        updatedOpeningHoursPerDayOfWeek.setOpeningHours(UPDATED_OPENING_HOURS);
        updatedOpeningHoursPerDayOfWeek.setClosingHours(UPDATED_CLOSING_HOURS);

        restOpeningHoursPerDayOfWeekMockMvc.perform(put("/api/opening-hours-per-day-of-weeks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOpeningHoursPerDayOfWeek)))
                .andExpect(status().isOk());

        // Validate the OpeningHoursPerDayOfWeek in the database
        List<OpeningHoursPerDayOfWeek> openingHoursPerDayOfWeeks = openingHoursPerDayOfWeekRepository.findAll();
        assertThat(openingHoursPerDayOfWeeks).hasSize(databaseSizeBeforeUpdate);
        OpeningHoursPerDayOfWeek testOpeningHoursPerDayOfWeek = openingHoursPerDayOfWeeks.get(openingHoursPerDayOfWeeks.size() - 1);
        assertThat(testOpeningHoursPerDayOfWeek.getDayOfWeek()).isEqualTo(UPDATED_DAY_OF_WEEK);
        assertThat(testOpeningHoursPerDayOfWeek.getOpeningHours()).isEqualTo(UPDATED_OPENING_HOURS);
        assertThat(testOpeningHoursPerDayOfWeek.getClosingHours()).isEqualTo(UPDATED_CLOSING_HOURS);
    }

    @Test
    @Transactional
    public void deleteOpeningHoursPerDayOfWeek() throws Exception {
        // Initialize the database
        openingHoursPerDayOfWeekService.save(openingHoursPerDayOfWeek);

        int databaseSizeBeforeDelete = openingHoursPerDayOfWeekRepository.findAll().size();

        // Get the openingHoursPerDayOfWeek
        restOpeningHoursPerDayOfWeekMockMvc.perform(delete("/api/opening-hours-per-day-of-weeks/{id}", openingHoursPerDayOfWeek.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OpeningHoursPerDayOfWeek> openingHoursPerDayOfWeeks = openingHoursPerDayOfWeekRepository.findAll();
        assertThat(openingHoursPerDayOfWeeks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
