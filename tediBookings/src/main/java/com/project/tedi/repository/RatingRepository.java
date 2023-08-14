package com.project.tedi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.tedi.model.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long>{

	public List<Rating> findByAccomodationId(Long accId);
	
	@Query(value = " select r from Rating r where r.guest.id = :guestId")
	public List<Rating> findByGuestId(Long guestId);
	
	@Query(value = "select r from Rating r where r.accomodation.owner.id = :hostId ")
	public List<Rating> findByHostId(Long hostId);
}
