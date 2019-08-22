package com.csm.dashboard.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.csm.dashboard.model.Consumption;
@Repository
public interface DashboardRepository extends CrudRepository<Consumption, Integer>{
	public List<Consumption> findAll();

}
