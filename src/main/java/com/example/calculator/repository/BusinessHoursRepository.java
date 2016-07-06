package com.example.calculator.repository;

import com.example.calculator.domain.BusinessHours;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BusinessHours entity.
 */
@SuppressWarnings("unused")
public interface BusinessHoursRepository extends JpaRepository<BusinessHours,Long> {

}
