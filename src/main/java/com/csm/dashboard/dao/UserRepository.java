package com.csm.dashboard.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csm.dashboard.model.CSMUser;
@Repository
public interface UserRepository extends JpaRepository<CSMUser, Long>{
	public List<CSMUser> findAll();
	public Optional<CSMUser> findByUserId(String userid);
}