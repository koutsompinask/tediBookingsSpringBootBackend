package com.project.tedi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.tedi.model.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long>{

	public List<Rating> findByAccomodationId(Long accId);
}
