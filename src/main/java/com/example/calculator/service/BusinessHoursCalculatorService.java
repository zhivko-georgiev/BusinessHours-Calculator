package com.example.calculator.service;

import java.time.LocalDate;

public interface BusinessHoursCalculatorService {
	
	LocalDate calculateDeadline(long timeInterval, String startingTime);
}
