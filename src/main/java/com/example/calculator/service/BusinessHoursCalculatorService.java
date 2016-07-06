package com.example.calculator.service;

import java.time.LocalDate;

public interface BusinessHoursCalculatorService {
	
	String calculateDeadline(long timeInterval, String startingDateTime);
}
