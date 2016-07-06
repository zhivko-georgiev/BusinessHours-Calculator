package com.example.calculator.repository;

import com.example.calculator.domain.BusinessHoursCalculator;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BusinessHoursCalculator entity.
 */
@SuppressWarnings("unused")
public interface BusinessHoursCalculatorRepository extends JpaRepository<BusinessHoursCalculator,Long> {

}
