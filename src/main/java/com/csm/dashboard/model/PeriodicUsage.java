package com.csm.dashboard.model;

public class PeriodicUsage {
	private String name;
	private String value;
	
	public PeriodicUsage(String name,String value) {
		this.name=name;
		this.value=value;
	}
	public PeriodicUsage() {
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return ((PeriodicUsage)obj).getName().equals(this.getName());
	}

}