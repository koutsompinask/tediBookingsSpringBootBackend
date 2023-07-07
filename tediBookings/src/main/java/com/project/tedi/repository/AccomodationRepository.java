package com.project.tedi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.tedi.model.Accomodation;

@Repository
public interface AccomodationRepository extends JpaRepository<Accomodation, Long>{

}
