package com.csm.dashboard.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csm.dashboard.model.Consumption;

public interface ConsumptionDao extends JpaRepository<Consumption, Long> {

}

