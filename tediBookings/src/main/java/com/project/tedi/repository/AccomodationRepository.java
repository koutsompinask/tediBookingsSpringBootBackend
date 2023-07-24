package com.project.tedi.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.tedi.model.Accomodation;

@Repository
public interface AccomodationRepository extends JpaRepository<Accomodation, Long>{
	
	@Query(value = "SELECT * FROM accomodation a WHERE a.max_person >= :p "
			+ "AND a.location LIKE %:loc% "
			+ "AND a.available_from <= :from "
			+ "AND a.available_to >= :to "
			+ "AND NOT EXISTS ("
			+ "SELECT * FROM booking b "
			+ "WHERE (b.from_date BETWEEN :from AND :to "
			+ "OR b.to_date BETWEEN :from AND :to) "
			+ "AND b.accomodation_id = a.id) "
			+ "ORDER BY (a.price+a.extra_cost*:p) ASC",nativeQuery = true)
	public List<Accomodation> filteredAccomodations(@Param("p") int people ,@Param("loc") String location,@Param("from") Date from,@Param("to") Date to);
	
	@Query(value = "SELECT * FROM accomodation a WHERE a.user_id = :owner",nativeQuery = true)
	public List<Accomodation> findByOwnerId(@Param("owner") Long ownerId);
}
