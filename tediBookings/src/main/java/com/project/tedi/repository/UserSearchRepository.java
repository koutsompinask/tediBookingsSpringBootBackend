package com.project.tedi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.tedi.model.UserSearch;

@Repository
public interface UserSearchRepository extends JpaRepository<UserSearch, Long>{

}
