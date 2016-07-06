package com.example.calculator.web.rest;

import com.example.calculator.BusinessHoursCalculatorApp;
import com.example.calculator.domain.StoreClosedPerDayOfWeek;
import com.example.calculator.repository.StoreClosedPerDayOfWeekRepository;
import com.example.calculator.service.StoreClosedPerDayOfWeekService;

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
 * Test class for the StoreClosedPerDayOfWeekResource REST controller.
 *
 * @see StoreClosedPerDayOfWeekResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BusinessHoursCalculatorApp.class)
@WebAppConfiguration
@IntegrationTest
public class StoreClosedPerDayOfWeekResourceIntTest {


    private static final DayOfWeek DEFAULT_DAY_OF_WEEK = DayOfWeek.MONDAY;
    private static final DayOfWeek UPDATED_DAY_OF_WEEK = DayOfWeek.TUESDAY;

    @Inject
    private StoreClosedPerDayOfWeekRepository storeClosedPerDayOfWeekRepository;

    @Inject
    private StoreClosedPerDayOfWeekService storeClosedPerDayOfWeekService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStoreClosedPerDayOfWeekMockMvc;

    private StoreClosedPerDayOfWeek storeClosedPerDayOfWeek;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StoreClosedPerDayOfWeekResource storeClosedPerDayOfWeekResource = new StoreClosedPerDayOfWeekResource();
        ReflectionTestUtils.setField(storeClosedPerDayOfWeekResource, "storeClosedPerDayOfWeekService", storeClosedPerDayOfWeekService);
        this.restStoreClosedPerDayOfWeekMockMvc = MockMvcBuilders.standaloneSetup(storeClosedPerDayOfWeekResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        storeClosedPerDayOfWeek = new StoreClosedPerDayOfWeek();
        storeClosedPerDayOfWeek.setDayOfWeek(DEFAULT_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    public void createStoreClosedPerDayOfWeek() throws Exception {
        int databaseSizeBeforeCreate = storeClosedPerDayOfWeekRepository.findAll().size();

        // Create the StoreClosedPerDayOfWeek

        restStoreClosedPerDayOfWeekMockMvc.perform(post("/api/store-closed-per-day-of-weeks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(storeClosedPerDayOfWeek)))
                .andExpect(status().isCreated());

        // Validate the StoreClosedPerDayOfWeek in the database
        List<StoreClosedPerDayOfWeek> storeClosedPerDayOfWeeks = storeClosedPerDayOfWeekRepository.findAll();
        assertThat(storeClosedPerDayOfWeeks).hasSize(databaseSizeBeforeCreate + 1);
        StoreClosedPerDayOfWeek testStoreClosedPerDayOfWeek = storeClosedPerDayOfWeeks.get(storeClosedPerDayOfWeeks.size() - 1);
        assertThat(testStoreClosedPerDayOfWeek.getDayOfWeek()).isEqualTo(DEFAULT_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    public void checkDayOfWeekIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeClosedPerDayOfWeekRepository.findAll().size();
        // set the field null
        storeClosedPerDayOfWeek.setDayOfWeek(null);

        // Create the StoreClosedPerDayOfWeek, which fails.

        restStoreClosedPerDayOfWeekMockMvc.perform(post("/api/store-closed-per-day-of-weeks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(storeClosedPerDayOfWeek)))
                .andExpect(status().isBadRequest());

        List<StoreClosedPerDayOfWeek> storeClosedPerDayOfWeeks = storeClosedPerDayOfWeekRepository.findAll();
        assertThat(storeClosedPerDayOfWeeks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStoreClosedPerDayOfWeeks() throws Exception {
        // Initialize the database
        storeClosedPerDayOfWeekRepository.saveAndFlush(storeClosedPerDayOfWeek);

        // Get all the storeClosedPerDayOfWeeks
        restStoreClosedPerDayOfWeekMockMvc.perform(get("/api/store-closed-per-day-of-weeks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(storeClosedPerDayOfWeek.getId().intValue())))
                .andExpect(jsonPath("$.[*].dayOfWeek").value(hasItem(DEFAULT_DAY_OF_WEEK.toString())));
    }

    @Test
    @Transactional
    public void getStoreClosedPerDayOfWeek() throws Exception {
        // Initialize the database
        storeClosedPerDayOfWeekRepository.saveAndFlush(storeClosedPerDayOfWeek);

        // Get the storeClosedPerDayOfWeek
        restStoreClosedPerDayOfWeekMockMvc.perform(get("/api/store-closed-per-day-of-weeks/{id}", storeClosedPerDayOfWeek.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(storeClosedPerDayOfWeek.getId().intValue()))
            .andExpect(jsonPath("$.dayOfWeek").value(DEFAULT_DAY_OF_WEEK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStoreClosedPerDayOfWeek() throws Exception {
        // Get the storeClosedPerDayOfWeek
        restStoreClosedPerDayOfWeekMockMvc.perform(get("/api/store-closed-per-day-of-weeks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStoreClosedPerDayOfWeek() throws Exception {
        // Initialize the database
        storeClosedPerDayOfWeekService.save(storeClosedPerDayOfWeek);

        int databaseSizeBeforeUpdate = storeClosedPerDayOfWeekRepository.findAll().size();

        // Update the storeClosedPerDayOfWeek
        StoreClosedPerDayOfWeek updatedStoreClosedPerDayOfWeek = new StoreClosedPerDayOfWeek();
        updatedStoreClosedPerDayOfWeek.setId(storeClosedPerDayOfWeek.getId());
        updatedStoreClosedPerDayOfWeek.setDayOfWeek(UPDATED_DAY_OF_WEEK);

        restStoreClosedPerDayOfWeekMockMvc.perform(put("/api/store-closed-per-day-of-weeks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedStoreClosedPerDayOfWeek)))
                .andExpect(status().isOk());

        // Validate the StoreClosedPerDayOfWeek in the database
        List<StoreClosedPerDayOfWeek> storeClosedPerDayOfWeeks = storeClosedPerDayOfWeekRepository.findAll();
        assertThat(storeClosedPerDayOfWeeks).hasSize(databaseSizeBeforeUpdate);
        StoreClosedPerDayOfWeek testStoreClosedPerDayOfWeek = storeClosedPerDayOfWeeks.get(storeClosedPerDayOfWeeks.size() - 1);
        assertThat(testStoreClosedPerDayOfWeek.getDayOfWeek()).isEqualTo(UPDATED_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    public void deleteStoreClosedPerDayOfWeek() throws Exception {
        // Initialize the database
        storeClosedPerDayOfWeekService.save(storeClosedPerDayOfWeek);

        int databaseSizeBeforeDelete = storeClosedPerDayOfWeekRepository.findAll().size();

        // Get the storeClosedPerDayOfWeek
        restStoreClosedPerDayOfWeekMockMvc.perform(delete("/api/store-closed-per-day-of-weeks/{id}", storeClosedPerDayOfWeek.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<StoreClosedPerDayOfWeek> storeClosedPerDayOfWeeks = storeClosedPerDayOfWeekRepository.findAll();
        assertThat(storeClosedPerDayOfWeeks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
