package com.example.calculator.repository;

import com.example.calculator.domain.StoreClosedPerDayOfWeek;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StoreClosedPerDayOfWeek entity.
 */
public interface StoreClosedPerDayOfWeekRepository extends JpaRepository<StoreClosedPerDayOfWeek,Long> {

	List<StoreClosedPerDayOfWeek> findAllByOrderByDayOfWeekAsc();
}
