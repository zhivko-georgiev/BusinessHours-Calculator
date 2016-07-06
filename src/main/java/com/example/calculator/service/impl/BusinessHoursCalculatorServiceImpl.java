package com.example.calculator.service.impl;

import com.example.calculator.domain.enumeration.DayOfWeek;
import com.example.calculator.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BusinessHoursCalculatorServiceImpl implements BusinessHoursCalculatorService {

    private final Logger log = LoggerFactory.getLogger(BusinessHoursCalculatorServiceImpl.class);
    
    @Autowired
    private BusinessHoursService businessHoursService;
    
    @Autowired
    private OpeningHoursPerDayOfWeekService оpeningHoursPerDayOfWeekService;
    
    @Autowired
    private OpeningHoursPerSpecificDateService оpeningHoursPerSpecificDateService;
    
    @Autowired
    private StoreClosedPerDayOfWeekService storeClosedPerDayOfWeekService;
    
    @Autowired
    private StoreClosedPerSpecificDateService storeClosedPerSpecificDateService;
    
	@Override
	public LocalDate calculateDeadline(long timeInterval, String startingTime) {
		// TODO Auto-generated method stub
		return null;
	}

}
