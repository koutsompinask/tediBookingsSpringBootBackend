package com.project.tedi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.tedi.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByUsername(String username);

	@Query("select u from User u where u.role = 'HOST' or u.role = 'HOST_AND_RENTER'")
	List<User> findHosts();
	
	@Query("select u from User u where u.role = 'RENTER' or u.role = 'HOST_AND_RENTER'")
	List<User> findRenters();
}
