package com.project.tedi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.tedi.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	@Query(value = "select u from User u where u.username = :username and u.password = :password ")
	public Optional<User>findUserByUserNameandPassWord(@Param("username") String username,@Param("password") String password);

}