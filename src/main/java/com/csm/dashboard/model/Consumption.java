package com.csm.dashboard.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="USAGE_DATA")
public class Consumption {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name = "ID")
	private int ID;

	private String FISCAL_QUATER;

	private int FISCAL_WEEK_IN_QUATER;

	private String FISCAL_MONTH_IN_QUATER;

	private String CONTRACT_TYPE;

	private String METEREDLOB;

	private String CHILD_TIER;

	private double TRUE_USAGE;

	private double CONTRACT_TYPE_TARGET;

	private double OVERALL_USAGE_TARGET;

	private double LOBTARGET;
	
	private String UPDATED_DATE;
	
public void setUPDATED_DATE(String uPDATED_DATE) {
	UPDATED_DATE = uPDATED_DATE;
}
public String getUPDATED_DATE() {
	return UPDATED_DATE;
}
	public void setID(int iD) {
		ID = iD;
	}

	public int getID() {
		return ID;
	}

	public String getFISCAL_QUATER() {
		return FISCAL_QUATER;
	}

	public void setFISCAL_QUATER(String fISCAL_QUATER) {
		FISCAL_QUATER = fISCAL_QUATER;
	}

	public int getFISCAL_WEEK_IN_QUATER() {
		return FISCAL_WEEK_IN_QUATER;
	}

	public void setFISCAL_WEEK_IN_QUATER(int fISCAL_WEEK_IN_QUATER) {
		FISCAL_WEEK_IN_QUATER = fISCAL_WEEK_IN_QUATER;
	}

	public String getFISCAL_MONTH_IN_QUATER() {
		return FISCAL_MONTH_IN_QUATER;
	}

	public void setFISCAL_MONTH_IN_QUATER(String fISCAL_MONTH_IN_QUATER) {
		FISCAL_MONTH_IN_QUATER = fISCAL_MONTH_IN_QUATER;
	}

	public String getCONTRACT_TYPE() {
		return CONTRACT_TYPE;
	}

	public void setCONTRACT_TYPE(String cONTRACT_TYPE) {
		CONTRACT_TYPE = cONTRACT_TYPE;
	}

	public String getMETEREDLOB() {
		return METEREDLOB;
	}

	public void setMETEREDLOB(String mETEREDLOB) {
		METEREDLOB = mETEREDLOB;
	}

	public String getCHILD_TIER() {
		return CHILD_TIER;
	}

	public void setCHILD_TIER(String cHILD_TIER) {
		CHILD_TIER = cHILD_TIER;
	}

	public double getTRUE_USAGE() {
		return TRUE_USAGE;
	}

	public void setTRUE_USAGE(double tRUE_USAGE) {
		TRUE_USAGE = tRUE_USAGE;
	}

	public double getCONTRACT_TYPE_TARGET() {
		return CONTRACT_TYPE_TARGET;
	}

	public void setCONTRACT_TYPE_TARGET(double cONTRACT_TYPE_TARGET) {
		CONTRACT_TYPE_TARGET = cONTRACT_TYPE_TARGET;
	}

	public double getOVERALL_USAGE_TARGET() {
		return OVERALL_USAGE_TARGET;
	}

	public void setOVERALL_USAGE_TARGET(double oVERALL_USAGE_TARGET) {
		OVERALL_USAGE_TARGET = oVERALL_USAGE_TARGET;
	}

	public double getLOBTARGET() {
		return LOBTARGET;
	}

	public void setLOBTARGET(double lOBTARGET) {
		LOBTARGET = lOBTARGET;
	}

	@Override
	public String toString() {
		return "Consumption [FISCAL_QUATER=" + FISCAL_QUATER + ", FISCAL_WEEK_IN_QUATER=" + FISCAL_WEEK_IN_QUATER
				+ ", FISCAL_MONTH_IN_QUATER=" + FISCAL_MONTH_IN_QUATER + ", CONTRACT_TYPE=" + CONTRACT_TYPE
				+ ", METEREDLOB=" + METEREDLOB + ", CHILD_TIER=" + CHILD_TIER + ", TRUE_USAGE=" + TRUE_USAGE
				+ ", CONTRACT_TYPE_TARGET=" + CONTRACT_TYPE_TARGET + ", OVERALL_USAGE_TARGET=" + OVERALL_USAGE_TARGET
				+ ", LOBTARGET=" + LOBTARGET + "]";
	}

}