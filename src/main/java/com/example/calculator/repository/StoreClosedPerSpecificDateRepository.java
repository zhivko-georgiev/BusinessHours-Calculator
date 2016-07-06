package com.example.calculator.repository;

import com.example.calculator.domain.StoreClosedPerSpecificDate;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StoreClosedPerSpecificDate entity.
 */
public interface StoreClosedPerSpecificDateRepository extends JpaRepository<StoreClosedPerSpecificDate,Long> {
	
	List<StoreClosedPerSpecificDate> findAllByOrderByDateAsc();
}
