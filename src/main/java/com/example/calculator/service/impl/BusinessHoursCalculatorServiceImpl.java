package com.example.calculator.service.impl;

import com.example.calculator.service.*;
import com.example.calculator.domain.*;
import com.example.calculator.repository.BusinessHoursCalculatorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Service Implementation for managing BusinessHoursCalculator.
 */
@Service
@Transactional
public class BusinessHoursCalculatorServiceImpl implements BusinessHoursCalculatorService{

    private final Logger log = LoggerFactory.getLogger(BusinessHoursCalculatorServiceImpl.class);
    
    @Inject
    private BusinessHoursCalculatorRepository businessHoursCalculatorRepository;
    
    @Inject
	private BusinessHoursService businessHoursService;

    @Inject
	private OpeningHoursPerDayOfWeekService РѕpeningHoursPerDayOfWeekService;

    @Inject
	private OpeningHoursPerSpecificDateService РѕpeningHoursPerSpecificDateService;

    @Inject
	private StoreClosedPerDayOfWeekService storeClosedPerDayOfWeekService;

    @Inject
	private StoreClosedPerSpecificDateService storeClosedPerSpecificDateService;

	private String defaultOpeningTime;
	private String defaultClosingTime;

	private Map<java.time.DayOfWeek, List<String>> openingHoursPerDayOfWeek = new HashMap<>();
	private Map<LocalDate, List<String>> openingHoursPerSpecificDate = new HashMap<>();
	private List<java.time.DayOfWeek> storeClosedPerDaysOfWeek = new ArrayList<>();
	private List<LocalDate> storeClosedPerSpecificDates = new ArrayList<>();
    
    /**
     * Save a businessHoursCalculator.
     * 
     * @param businessHoursCalculator the entity to save
     * @return the persisted entity
     */
    public BusinessHoursCalculator save(BusinessHoursCalculator businessHoursCalculator) {
        log.debug("Request to save BusinessHoursCalculator : {}", businessHoursCalculator);
        BusinessHoursCalculator result = businessHoursCalculatorRepository.save(businessHoursCalculator);
        return result;
    }

    /**
     *  Get all the businessHoursCalculators.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<BusinessHoursCalculator> findAll() {
        log.debug("Request to get all BusinessHoursCalculators");
        List<BusinessHoursCalculator> result = businessHoursCalculatorRepository.findAll();
        return result;
    }

    /**
     *  Get one businessHoursCalculator by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BusinessHoursCalculator findOne(Long id) {
        log.debug("Request to get BusinessHoursCalculator : {}", id);
        BusinessHoursCalculator businessHoursCalculator = businessHoursCalculatorRepository.findOne(id);
        return businessHoursCalculator;
    }

    /**
     *  Delete the  businessHoursCalculator by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BusinessHoursCalculator : {}", id);
        businessHoursCalculatorRepository.delete(id);
    }

	@Override
	public ZonedDateTime calculateDeadline(long timeInterval, String startingDateTime) {
		intializeDefaultOpeningHours(businessHoursService);
		initializeOpeningHoursPerDayOfWeek(openingHoursPerDayOfWeek, РѕpeningHoursPerDayOfWeekService);
		initializeOpeningHoursPerSpecificDate(openingHoursPerSpecificDate, РѕpeningHoursPerSpecificDateService);
		initializeStoreClosedPerDaysOfWeek(storeClosedPerDaysOfWeek, storeClosedPerDayOfWeekService);
		initializeStoreClosedPerSpecificDates(storeClosedPerSpecificDates, storeClosedPerSpecificDateService);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm", Locale.US);

		LocalDateTime parsedStartingDateTime = LocalDateTime.parse(startingDateTime, formatter);
		DayOfWeek startingDayOfWeek = parsedStartingDateTime.getDayOfWeek();

		LocalDate startingParsedDate = LocalDate.of(parsedStartingDateTime.getYear(),
				parsedStartingDateTime.getMonthValue(), parsedStartingDateTime.getDayOfMonth());

		if (openingHoursPerDayOfWeek.containsKey(startingParsedDate)) {
			parsedStartingDateTime = adjustStartingDateTime(parsedStartingDateTime,
					openingHoursPerDayOfWeek.get(startingDayOfWeek));
		} else if (openingHoursPerSpecificDate.containsKey(startingParsedDate)) {
			parsedStartingDateTime = adjustStartingDateTime(parsedStartingDateTime,
					openingHoursPerSpecificDate.get(startingParsedDate));
		}

		long workingMinutesForAdjustedDateTime = 0;
		do {
			workingMinutesForAdjustedDateTime = calculateBusinessMinutesForSpecificDateTime(parsedStartingDateTime);

			if (workingMinutesForAdjustedDateTime == 0) {
				parsedStartingDateTime = parsedStartingDateTime.plusDays(1);
				continue;
			} else {
				if (workingMinutesForAdjustedDateTime * 60 >= timeInterval) {
					parsedStartingDateTime = parsedStartingDateTime.plus(timeInterval, ChronoUnit.SECONDS);
					timeInterval = 0;
				} else {
					timeInterval -= workingMinutesForAdjustedDateTime * 60;
					parsedStartingDateTime = parsedStartingDateTime.plusDays(1);
					parsedStartingDateTime = adjustStaringHoursForTheGivenDate(parsedStartingDateTime);
				}
			}
		} while (timeInterval != 0);


		return ZonedDateTime.of(parsedStartingDateTime, ZoneId.systemDefault());
	}
	
	private LocalDateTime adjustStaringHoursForTheGivenDate(LocalDateTime parsedStartingDateTime) {
		int startingHour = 0;
		int startingMinutes = 0;

		LocalDate startingDate = LocalDate.of(parsedStartingDateTime.getYear(), parsedStartingDateTime.getMonthValue(),
				parsedStartingDateTime.getDayOfMonth());
		DayOfWeek startingDayOfWeek = parsedStartingDateTime.getDayOfWeek();

		if (openingHoursPerSpecificDate.containsKey(startingDate)) {
			String openingHours = openingHoursPerSpecificDate.get(startingDate).get(0);
			List<Integer> parsedOpeningHours = parseOpeningTime(openingHours);
			startingHour = parsedOpeningHours.get(0);
			startingMinutes = parsedOpeningHours.get(1);
		} else if (openingHoursPerDayOfWeek.containsKey(startingDayOfWeek)) {
			String openingHours = openingHoursPerDayOfWeek.get(startingDayOfWeek).get(0);
			List<Integer> parsedOpeningHours = parseOpeningTime(openingHours);
			startingHour = parsedOpeningHours.get(0);
			startingMinutes = parsedOpeningHours.get(1);
		} else {
			List<Integer> parsedOpeningHours = parseOpeningTime(defaultOpeningTime);
			startingHour = parsedOpeningHours.get(0);
			startingMinutes = parsedOpeningHours.get(1);
		}

		return LocalDateTime.of(startingDate, LocalTime.of(startingHour, startingMinutes));
	}
	
	private List<Integer> parseOpeningTime(String openingHours) {
		String[] splittedOpeningHours = openingHours.split(":");
		List<Integer> parsedOpeningTimes = new ArrayList<>();
		parsedOpeningTimes.add(Integer.parseInt(splittedOpeningHours[0]));
		parsedOpeningTimes.add(Integer.parseInt(splittedOpeningHours[1]));
		
		return parsedOpeningTimes;
	}
	
	private long calculateBusinessMinutesForSpecificDateTime(LocalDateTime parsedStartingDateTime) {
		long businessMinutesBeforeClosing = 0;

		LocalDate startingDate = LocalDate.of(parsedStartingDateTime.getYear(), parsedStartingDateTime.getMonthValue(),
				parsedStartingDateTime.getDayOfMonth());
		DayOfWeek startingDayOfWeek = parsedStartingDateTime.getDayOfWeek();

		if (openingHoursPerSpecificDate.containsKey(startingDate)) {
			String closingHours = openingHoursPerSpecificDate.get(startingDate).get(1);
			businessMinutesBeforeClosing = calculateBusinessMinutesTillClosing(parsedStartingDateTime, closingHours);
		} else if (openingHoursPerDayOfWeek.containsKey(startingDayOfWeek)) {
			String closingHours = openingHoursPerDayOfWeek.get(startingDayOfWeek).get(1);
			businessMinutesBeforeClosing = calculateBusinessMinutesTillClosing(parsedStartingDateTime, closingHours);
		} else if (storeClosedPerSpecificDates.contains(startingDate)
				|| storeClosedPerDaysOfWeek.contains(startingDayOfWeek)) {
			businessMinutesBeforeClosing = 0;
		} else {
			businessMinutesBeforeClosing = calculateBusinessMinutesTillClosing(parsedStartingDateTime, defaultClosingTime);
		}

		return businessMinutesBeforeClosing;
	}
	
	private long calculateBusinessMinutesTillClosing(LocalDateTime parsedStartingDateTime, String closingHours) {
		long businessMinutesBeforeClosing = 0;

		String[] splittedClosingHours = closingHours.split(":");
		int closingHour = Integer.parseInt(splittedClosingHours[0]);
		int closingMinutes = Integer.parseInt(splittedClosingHours[1]);

		LocalTime closingTime = LocalTime.of(closingHour, closingMinutes);
		LocalTime startingTimeOfOrder = LocalTime.of(parsedStartingDateTime.getHour(),
				parsedStartingDateTime.getMinute());

		businessMinutesBeforeClosing = ChronoUnit.MINUTES.between(startingTimeOfOrder, closingTime);

		return businessMinutesBeforeClosing;
	}
	
	private LocalDateTime adjustStartingDateTime(LocalDateTime parsedStartingDateTime, List<String> list) {
		int startingHour = parsedStartingDateTime.getHour();
		int startingMinutes = parsedStartingDateTime.getMinute();

		List<Integer> parsedOpeningAndClosingTimes = parseOpeningAndClosingTimes(list);

		int parsedOpeningHour = parsedOpeningAndClosingTimes.get(0);
		int parsedOpeningMinutes = parsedOpeningAndClosingTimes.get(1);

		int parsedClosingHour = parsedOpeningAndClosingTimes.get(2);
		int parsedClosingMinutes = parsedOpeningAndClosingTimes.get(3);

		if (startingHour < parsedOpeningHour) {
			startingHour = parsedOpeningHour;
			startingMinutes = parsedOpeningMinutes;
		} else if (startingHour == parsedOpeningHour) {
			if (startingMinutes < parsedOpeningMinutes) {
				startingMinutes = parsedOpeningMinutes;
			}
		} else if (startingHour > parsedClosingHour) {
			parsedStartingDateTime = parsedStartingDateTime.plusDays(1);

			return LocalDateTime.of(parsedStartingDateTime.getYear(), parsedStartingDateTime.getMonthValue(),
					parsedStartingDateTime.getDayOfMonth(), parsedOpeningHour, parsedOpeningMinutes);
		} else if (startingHour == parsedClosingHour) {
			if (startingMinutes > parsedClosingMinutes) {
				parsedStartingDateTime = parsedStartingDateTime.plusDays(1);

				return LocalDateTime.of(parsedStartingDateTime.getYear(), parsedStartingDateTime.getMonthValue(),
						parsedStartingDateTime.getDayOfMonth(), parsedOpeningHour, parsedOpeningMinutes);
			}
		}

		return LocalDateTime.of(parsedStartingDateTime.getYear(), parsedStartingDateTime.getMonthValue(),
				parsedStartingDateTime.getDayOfMonth(), startingHour, startingMinutes);
	}
	
	private List<Integer> parseOpeningAndClosingTimes(List<String> list) {
		List<Integer> parsedOpeningAndClosingTimes = new ArrayList<>();

		String openingHours = list.get(0);
		String closingHours = list.get(1);

		String[] splittedOpeningHours = openingHours.split(":");
		Integer parsedOpeningHours = Integer.parseInt(splittedOpeningHours[0]);
		Integer parsedOpeningMinutes = Integer.parseInt(splittedOpeningHours[1]);
		parsedOpeningAndClosingTimes.add(parsedOpeningHours);
		parsedOpeningAndClosingTimes.add(parsedOpeningMinutes);

		String[] splittedClosingHours = closingHours.split(":");
		Integer parsedClosingHours = Integer.parseInt(splittedClosingHours[0]);
		Integer parsedClosingMinutes = Integer.parseInt(splittedClosingHours[1]);
		parsedOpeningAndClosingTimes.add(parsedClosingHours);
		parsedOpeningAndClosingTimes.add(parsedClosingMinutes);

		return parsedOpeningAndClosingTimes;
	}

	private void initializeStoreClosedPerSpecificDates(List<LocalDate> storeClosedPerSpecificDates,
			StoreClosedPerSpecificDateService storeClosedPerSpecificDateService) {

		List<StoreClosedPerSpecificDate> storeClosedPerDates = storeClosedPerSpecificDateService.findAll();

		if (storeClosedPerDates != null) {
			for (StoreClosedPerSpecificDate storeClosedPerSpecificDate : storeClosedPerDates) {
				storeClosedPerSpecificDates.add(storeClosedPerSpecificDate.getDate());
			}
		}
	}

	private void initializeStoreClosedPerDaysOfWeek(List<DayOfWeek> storeClosedPerDaysOfWeek,
			StoreClosedPerDayOfWeekService storeClosedPerDayOfWeekService) {

		List<StoreClosedPerDayOfWeek> storeClosedPerDayOfWeeks = storeClosedPerDayOfWeekService.findAll();

		if (storeClosedPerDayOfWeeks != null) {
			for (StoreClosedPerDayOfWeek element : storeClosedPerDayOfWeeks) {
				storeClosedPerDaysOfWeek.add(DayOfWeek.valueOf(element.getDayOfWeek().toString()));
			}
		}
	}

	private void initializeOpeningHoursPerSpecificDate(Map<LocalDate, List<String>> openingHoursPerSpecificDate,
			OpeningHoursPerSpecificDateService РѕpeningHoursPerSpecificDateService) {

		List<OpeningHoursPerSpecificDate> openingHoursPerSpecificDates = РѕpeningHoursPerSpecificDateService.findAll();

		if (openingHoursPerSpecificDate != null) {
			for (OpeningHoursPerSpecificDate element : openingHoursPerSpecificDates) {
				openingHoursPerSpecificDate.put(element.getDate(),
						Arrays.asList(element.getOpeningHours(), element.getClosingHours()));
			}
		}
	}

	private void initializeOpeningHoursPerDayOfWeek(Map<DayOfWeek, List<String>> openingHoursPerDayOfWeek,
			OpeningHoursPerDayOfWeekService РѕpeningHoursPerDayOfWeekService) {

		List<OpeningHoursPerDayOfWeek> openingHoursPerDayOfWeeks = РѕpeningHoursPerDayOfWeekService.findAll();

		if (openingHoursPerDayOfWeeks != null) {
			for (OpeningHoursPerDayOfWeek element : openingHoursPerDayOfWeeks) {
				openingHoursPerDayOfWeek.put(DayOfWeek.valueOf(element.getDayOfWeek().toString()),
						Arrays.asList(element.getOpeningHours(), element.getClosingHours()));
			}
		}
	}

	private void intializeDefaultOpeningHours(BusinessHoursService businessHoursService) {
		List<BusinessHours> businessHours = businessHoursService.findAll();

		if (businessHours != null) {
			for (BusinessHours element : businessHours) {
				defaultOpeningTime = element.getDefaultOpeningHours();
				defaultClosingTime = element.getDefaultClosingHours();
			}
		}
	}
}
