package com.project.tedi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.tedi.model.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long>{
	
	@Query(value = "SELECT * FROM photo WHERE accomodation_id=:accId",nativeQuery = true)
	public List<Photo> findByAccomodationId(@Param("accId") Long accId);

}