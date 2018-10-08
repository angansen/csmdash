package com.csm.dashboard.service;

import java.util.HashMap;
import java.util.List;

import com.csm.dashboard.model.PeriodicUsage;

public interface ContractTypeService {

	HashMap<String, Object> usageMap();

	HashMap<String, Object> getKPI(String contractType, String periodType, String periodValue);

}
