package com.csm.dashboard.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csm.dashboard.model.UserActivity;
@Repository
public interface UserActivityDAO extends JpaRepository<UserActivity, Long>{
	
}