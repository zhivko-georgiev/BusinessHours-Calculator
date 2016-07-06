package com.example.calculator.repository;

import com.example.calculator.domain.OpeningHoursPerSpecificDate;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OpeningHoursPerSpecificDate entity.
 */
public interface OpeningHoursPerSpecificDateRepository extends JpaRepository<OpeningHoursPerSpecificDate,Long> {
	
	List<OpeningHoursPerSpecificDate> findAllByOrderByDateAsc();
}
