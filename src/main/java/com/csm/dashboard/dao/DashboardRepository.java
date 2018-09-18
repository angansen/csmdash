package com.csm.dashboard.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csm.dashboard.model.UsageData;
@Repository
public interface DashboardRepository extends JpaRepository<UsageData, Integer>{
	public List<UsageData> findAll();

}
