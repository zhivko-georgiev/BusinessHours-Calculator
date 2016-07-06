package com.example.calculator.domain.enumeration;

/**
 * The DayOfWeek enumeration.
 */
public enum DayOfWeek {
    MONDAY(0),TUESDAY(1),WEDNESDAY(2),THURSDAY(3),FRIDAY(4),SATURDAY(5),SUNDAY(6);
	
	private int dayOfWeekIntValue;
	
	private DayOfWeek(int dayOfWeekIntValue) {
		this.dayOfWeekIntValue = dayOfWeekIntValue;
	}
    
}
