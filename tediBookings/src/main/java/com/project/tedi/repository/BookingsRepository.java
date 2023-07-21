package com.project.tedi.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.tedi.model.Booking;

@Repository
public interface BookingsRepository extends JpaRepository<Booking, Long>{
	
	@Query(value = "SELECT * FROM booking WHERE user_id=:renter ",nativeQuery = true)
	List<Booking> findByUser(@Param("renter") Long renterId);

	@Query(value = "SELECT * FROM booking WHERE "
			+ "accomodation_id = :accId AND "
			+ "(:from BETWEEN from_date AND to_date "
			+ "OR :to BETWEEN from_date AND to_date)", nativeQuery = true)
	List<Booking> checkBooked(@Param("accId") Long accId, @Param("from") Date from, @Param("to") Date to);
	
}