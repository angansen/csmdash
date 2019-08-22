package com.csm.dashboard.service;

import java.util.HashMap;

public interface DashboardService {
	public HashMap<String, Object> getOverallUsageData();
	public HashMap<String, Object> getLOBAndProducts();
	public HashMap<String, Object> getPeriodicUsage();
}
