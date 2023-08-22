package com.project.tedi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.tedi.model.UserViewAccomodation;

@Repository
public interface UserViewAccomodationRepository extends JpaRepository<UserViewAccomodation, Long>{
	
	@Query("select u from UserViewAccomodation u where u.user.id = :userId and u.accomodation.id=:accId")
	public List<UserViewAccomodation> findByUserAndAccomodationId(@Param("userId") long userId ,@Param("accId") long accId);
	
	@Query("select count(u) from UserViewAccomodation u where u.user.id = :userId and u.accomodation.id=:accId")
	public int countByUserAndAccomodationId(@Param("userId") long userId ,@Param("accId") long accId);
	
}
