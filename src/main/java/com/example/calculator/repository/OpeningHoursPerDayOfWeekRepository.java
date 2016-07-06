package com.example.calculator.repository;

import com.example.calculator.domain.OpeningHoursPerDayOfWeek;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OpeningHoursPerDayOfWeek entity.
 */
public interface OpeningHoursPerDayOfWeekRepository extends JpaRepository<OpeningHoursPerDayOfWeek,Long> {

	List<OpeningHoursPerDayOfWeek> findAllByOrderByDayOfWeekAsc();
}
