package com.csm.dashboard.model;

import java.util.List;

public class dataWrapper {

	private List<PeriodicUsage> data;
	
	public void setData(List<PeriodicUsage> data) {
		this.data = data;
	}
	
	public List<PeriodicUsage> getData() {
		return data;
	}
}
