package com.project.tedi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.tedi.model.Bookings;

@Repository
public interface BookingsRepository extends JpaRepository<Bookings, Long>{

}
