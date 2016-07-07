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
public class BusinessHoursCalculatorServiceImpl implements BusinessHoursCalculatorService {

	private final Logger log = LoggerFactory.getLogger(BusinessHoursCalculatorServiceImpl.class);

	private static final String DEFAULT_OPENING_HOURS_IF_NOT_PRESENT = "09:00";
	private static final String DEFAULT_CLOSING_HOURS_IF_NOT_PRESENT = "18:00";

	@Inject
	private BusinessHoursCalculatorRepository businessHoursCalculatorRepository;

	@Inject
	private BusinessHoursService businessHoursService;

	@Inject
	private OpeningHoursPerDayOfWeekService оpeningHoursPerDayOfWeekService;

	@Inject
	private OpeningHoursPerSpecificDateService оpeningHoursPerSpecificDateService;

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
	 * Get all the businessHoursCalculators.
	 * 
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public List<BusinessHoursCalculator> findAll() {
		log.debug("Request to get all BusinessHoursCalculators");
		List<BusinessHoursCalculator> result = businessHoursCalculatorRepository.findAll();
		return result;
	}

	/**
	 * Get one businessHoursCalculator by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public BusinessHoursCalculator findOne(Long id) {
		log.debug("Request to get BusinessHoursCalculator : {}", id);
		BusinessHoursCalculator businessHoursCalculator = businessHoursCalculatorRepository.findOne(id);
		return businessHoursCalculator;
	}

	/**
	 * Delete the businessHoursCalculator by id.
	 * 
	 * @param id the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete BusinessHoursCalculator : {}", id);
		businessHoursCalculatorRepository.delete(id);
	}

    /**
     * Calculate the expected time for pickup.
     * 
     * @param timeInterval expected time in seconds for finishing the order
     * @param startingDateTime date and time at which the order was placed
     * @return date and time at which the order will be ready for pickup.
     */
	@Transactional(readOnly = true)
	public ZonedDateTime calculateDeadline(long timeInterval, String startingDateTime) {
		intializeDefaultOpeningHours(businessHoursService);
		openingHoursPerDayOfWeek = initializeOpeningHoursPerDayOfWeek(оpeningHoursPerDayOfWeekService);
		openingHoursPerSpecificDate = initializeOpeningHoursPerSpecificDate(оpeningHoursPerSpecificDateService);
		storeClosedPerDaysOfWeek = initializeStoreClosedPerDaysOfWeek(storeClosedPerDayOfWeekService);
		storeClosedPerSpecificDates = initializeStoreClosedPerSpecificDates(storeClosedPerSpecificDateService);

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

    /**
     * Prepares the actual working business hours for each order.
     * 
     * @return string containing the info about the business hours
     */
	@Transactional(readOnly = true)
	public String prepareBusinessHoursData() {
		StringBuilder sb = new StringBuilder();

		// Appending Default Opening Hours
		sb.append("Opening Hours:" + defaultOpeningTime + "_");
		sb.append("Closing Hours:" + defaultClosingTime + "_");

		// Appending Opening Hours per Day Of Week
		if (!openingHoursPerDayOfWeek.isEmpty()) {
			sb.append("Opening Hours Per Day/s Of Week:_");
			for (DayOfWeek dayOfWeek : openingHoursPerDayOfWeek.keySet()) {
				List<String> openingHours = openingHoursPerDayOfWeek.get(dayOfWeek);

				sb.append(dayOfWeek + ": from " + openingHours.get(0) + " to " + openingHours.get(1) + "_");
			}
		}

		// Appending Opening Hours per Specific Date
		if (!openingHoursPerSpecificDate.isEmpty()) {
			sb.append("Opening Hours Per Specific Date/s:_");
			for (LocalDate date : openingHoursPerSpecificDate.keySet()) {
				List<String> openingHours = openingHoursPerSpecificDate.get(date);

				sb.append(date + ": from " + openingHours.get(0) + " to " + openingHours.get(1) + "_");
			}
		}

		// Appending Store Closed Per Day Of Week
		if (storeClosedPerDaysOfWeek.size() != 0) {
			sb.append("Store Closed Per Day/s Of Week:_");
			for (DayOfWeek dayOfWeek : storeClosedPerDaysOfWeek) {
				sb.append(dayOfWeek + "_");
			}
		}

		// Appending Store Closed Per Specific Date
		if (storeClosedPerSpecificDates.size() != 0) {
			sb.append("Store Closed Per Specific Date/s:_");
			for (LocalDate date : storeClosedPerSpecificDates) {
				sb.append(date + "_");
			}
		}

		return sb.toString();
	}
	
	/**
	 * Helper method used for adjusting the starting date and time
	 * in case it is before/after the opening/closing hours.
	 * 
	 * @param parsedStartingDateTime the starting date and time of the order
	 * @param openingHours working hours as list of strings
	 * @return working hours parsed into list of integers
	 */
	private LocalDateTime adjustStartingDateTime(LocalDateTime parsedStartingDateTime, List<String> openingHours) {
		int startingHour = parsedStartingDateTime.getHour();
		int startingMinutes = parsedStartingDateTime.getMinute();

		List<Integer> parsedOpeningAndClosingTimes = parseOpeningAndClosingTimes(openingHours);

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

	/**
	 * Helper method used for adjusting the starting date and time
	 * in case the order will not be ready for pickup on the same day.
	 * 
	 * @param parsedStartingDateTime the starting date and time of the order
	 * @return adjusted date and time
	 */
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

	/**
	 * Helper method used for parsing the opening hours.
	 * 
	 * @param openingHours working hours as string
	 * @return opening hours parsed into list of integers
	 */
	private List<Integer> parseOpeningTime(String openingHours) {
		String[] splittedOpeningHours = openingHours.split(":");
		List<Integer> parsedOpeningTimes = new ArrayList<>();
		parsedOpeningTimes.add(Integer.parseInt(splittedOpeningHours[0]));
		parsedOpeningTimes.add(Integer.parseInt(splittedOpeningHours[1]));

		return parsedOpeningTimes;
	}

	/**
	 * Helper method used for calculating the business minutes for the given date and time
	 * 
	 * @param parsedStartingDateTime the starting date and time of the order
	 * @return the working minutes for the given date and time
	 */
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
			businessMinutesBeforeClosing = calculateBusinessMinutesTillClosing(parsedStartingDateTime,
					defaultClosingTime);
		}

		return businessMinutesBeforeClosing;
	}

	/**
	 * Helper method used for calculating the remaining business minutes 
	 * until the store closes for the given day
	 * 
	 * @param parsedStartingDateTime the starting date and time of the order
	 * @param closingHours the hours at which the store closes for this day
	 * @return the working minutes before the store closes
	 */
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

	/**
	 * Helper method used for parsing the opening and closing hours.
	 * 
	 * @param workingHours working hours as list of strings
	 * @return working hours parsed into list of integers
	 */
	private List<Integer> parseOpeningAndClosingTimes(List<String> workingHours) {
		List<Integer> parsedOpeningAndClosingTimes = new ArrayList<>();

		String openingHours = workingHours.get(0);
		String closingHours = workingHours.get(1);

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
	
	/**
	 * Populate information about the opening and closing hours
	 * Sets defaults values if no business hours have been entered
	 * 
	 * @param businessHoursService service which extracts user's defined working hours
	 */
	private void intializeDefaultOpeningHours(BusinessHoursService businessHoursService) {
		List<BusinessHours> businessHours = businessHoursService.findAll();

		if (businessHours.size() != 0) {
			for (BusinessHours element : businessHours) {
				defaultOpeningTime = element.getDefaultOpeningHours();
				defaultClosingTime = element.getDefaultClosingHours();
			}
		} else {
			defaultOpeningTime = DEFAULT_OPENING_HOURS_IF_NOT_PRESENT;
			defaultClosingTime = DEFAULT_CLOSING_HOURS_IF_NOT_PRESENT;
		}
	}
	
	/**
	 * Populate information about the opening hours per days of week.
	 * 
	 * @param openingHoursPerDayOfWeekService service which extracts the opening hours per days of week
	 * @return map containing the opening hours for given day of week
	 */
	private Map<DayOfWeek, List<String>> initializeOpeningHoursPerDayOfWeek(OpeningHoursPerDayOfWeekService openingHoursPerDayOfWeekService) {
		Map<DayOfWeek, List<String>> openingHoursPerDayOfWeek = new HashMap<>();
		List<OpeningHoursPerDayOfWeek> openingHoursPerDayOfWeeks = openingHoursPerDayOfWeekService.findAll();

		for (OpeningHoursPerDayOfWeek element : openingHoursPerDayOfWeeks) {
			openingHoursPerDayOfWeek.put(DayOfWeek.valueOf(element.getDayOfWeek().toString()),
					Arrays.asList(element.getOpeningHours(), element.getClosingHours()));
		}

		return openingHoursPerDayOfWeek;
	}

	/**
	 * Populate information about the opening hours per specific days.
	 * 
	 * @param openingHoursPerSpecificDateService service which extracts the opening hours per specific dates
	 * @return map containing the opening hours for given date
	 */
	private Map<LocalDate, List<String>> initializeOpeningHoursPerSpecificDate(OpeningHoursPerSpecificDateService openingHoursPerSpecificDateService) {
		Map<LocalDate, List<String>> openingHoursPerSpecificDate = new HashMap<>();
		List<OpeningHoursPerSpecificDate> openingHoursPerSpecificDates = openingHoursPerSpecificDateService.findAll();

		for (OpeningHoursPerSpecificDate element : openingHoursPerSpecificDates) {
			openingHoursPerSpecificDate.put(element.getDate(),
					Arrays.asList(element.getOpeningHours(), element.getClosingHours()));
		}

		return openingHoursPerSpecificDate;
	}


	/**
	 * Populate information about the store's non-working days of week.
	 * 
	 * @param storeClosedPerDayOfWeekService service which extracts the non-working days of week
	 * @return list containing information about the non-working days of week
	 */
	private List<DayOfWeek> initializeStoreClosedPerDaysOfWeek(StoreClosedPerDayOfWeekService storeClosedPerDayOfWeekService) {
		List<DayOfWeek> daysOfWeek = new ArrayList<>();
		List<StoreClosedPerDayOfWeek> storeClosedPerDayOfWeeks = storeClosedPerDayOfWeekService.findAll();

		for (StoreClosedPerDayOfWeek element : storeClosedPerDayOfWeeks) {
			daysOfWeek.add(DayOfWeek.valueOf(element.getDayOfWeek().toString()));
		}

		return daysOfWeek;
	}

	/**
	 * Populate information about the store's non-working dates.
	 * 
	 * @param storeClosedPerSpecificDateService service which extracts the non-working dates
	 * @return list containing information about the non-working dates
	 */
	private List<LocalDate> initializeStoreClosedPerSpecificDates(StoreClosedPerSpecificDateService storeClosedPerSpecificDateService) {
		List<LocalDate> storeClosedPerSpecificDates = new ArrayList<>();
		List<StoreClosedPerSpecificDate> storeClosedPerDates = storeClosedPerSpecificDateService.findAll();

		for (StoreClosedPerSpecificDate storeClosedPerSpecificDate : storeClosedPerDates) {
			storeClosedPerSpecificDates.add(storeClosedPerSpecificDate.getDate());
		}

		return storeClosedPerSpecificDates;
	}
}
