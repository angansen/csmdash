package com.csm.dashboard.service;

import java.util.HashMap;
import java.util.List;

import com.csm.dashboard.model.UsageBean;

public interface DashboardService {
	public HashMap<String, Object> getOverallUsageData();
	public HashMap<String, Object> getLOBAndProducts();
	public HashMap<String, Object> getPeriodicUsage();
}
