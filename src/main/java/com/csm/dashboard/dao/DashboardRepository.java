package com.csm.dashboard.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csm.dashboard.model.Consumption;
@Repository
public interface DashboardRepository extends JpaRepository<Consumption, Long>{
	public List<Consumption> findAll();

}
