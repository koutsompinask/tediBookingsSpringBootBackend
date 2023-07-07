package com.project.tedi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.tedi.model.Accomodation;

@Repository
public interface AccomodationRepository extends JpaRepository<Accomodation, Long>{

	@Query(value = "select a from Accomodation a where a.maxPerson >= :p and upper(a.location) = upper(:loc)")
	public List<Accomodation> filteredAccomodationsByPeopleAndLocation(@Param("p") int people,@Param("loc") String loc);
	
	@Query(value = "select a from Accomodation a where a.maxPerson >= :p ")
	public List<Accomodation> filteredAccomodationsByPeople(@Param("p") int people);
	
	@Query(value = "select a from Accomodation a where upper(a.location) = upper(:loc)")
	public List<Accomodation> filteredAccomodationsByLocation(@Param("loc") String loc);
	
}
