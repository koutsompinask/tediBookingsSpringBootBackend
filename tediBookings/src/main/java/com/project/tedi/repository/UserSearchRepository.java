package com.project.tedi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.tedi.model.UserSearch;

@Repository
public interface UserSearchRepository extends JpaRepository<UserSearch, Long>{

	@Query("select u from UserSearch u where u.user.id = :userId and u.accomodation.id=:accId")
	public List<UserSearch> findByUserAndAccomodationId(@Param("userId") long userId ,@Param("accId") long accId);

	@Query("select count(u) from UserSearch u where u.user.id = :userId and u.accomodation.id=:accId")
	public int countByUserAndAccomodationId(@Param("userId") long userId ,@Param("accId") long accId);
}
