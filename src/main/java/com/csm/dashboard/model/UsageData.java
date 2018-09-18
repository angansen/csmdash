package com.csm.dashboard.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(description="All fields of usage data entity")
public class UsageData {
	@Id
	@GeneratedValue
	private Integer id;
	private String fiscalQuater;
	private Integer fiscalWeekInQuater;
	private String fiscalMonthInQuater;
	private String contractType;
	private String meteredLOB;
	private String childTier;
	private Double trueUsage;
	private Long contractTypeTarget;
	private Long overallUsageTarget;
	private Long LOBTarget;

	protected UsageData() {
		
	}

	/**
	 * @param id
	 * @param fiscalQuater
	 * @param fiscalWeekInQuater
	 * @param fiscalMonthInQuater
	 * @param contractType
	 * @param meteredLOB
	 * @param childTier
	 * @param trueUsage
	 * @param contractTypeTarget
	 * @param overallUsageTarget
	 * @param lOBTarget
	 */
	public UsageData(Integer id, String fiscalQuater, Integer fiscalWeekInQuater, String fiscalMonthInQuater,
			String contractType, String meteredLOB, String childTier, Double trueUsage, Long contractTypeTarget,
			Long overallUsageTarget, Long lOBTarget) {
		this.id = id;
		this.fiscalQuater = fiscalQuater;
		this.fiscalWeekInQuater = fiscalWeekInQuater;
		this.fiscalMonthInQuater = fiscalMonthInQuater;
		this.contractType = contractType;
		this.meteredLOB = meteredLOB;
		this.childTier = childTier;
		this.trueUsage = trueUsage;
		this.contractTypeTarget = contractTypeTarget;
		this.overallUsageTarget = overallUsageTarget;
		LOBTarget = lOBTarget;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFiscalQuater() {
		return fiscalQuater;
	}
	public void setFiscalQuater(String fiscalQuater) {
		this.fiscalQuater = fiscalQuater;
	}
	public int getFiscalWeekInQuater() {
		return fiscalWeekInQuater;
	}
	public void setFiscalWeekInQuater(int fiscalWeekInQuater) {
		this.fiscalWeekInQuater = fiscalWeekInQuater;
	}
	public String getContractType() {
		return contractType;
	}
	public void setContractType(String contractType) {
		this.contractType = contractType;
	}
	public String getMeteredLOB() {
		return meteredLOB;
	}
	public void setMeteredLOB(String meteredLOB) {
		this.meteredLOB = meteredLOB;
	}
	public String getChildTier() {
		return childTier;
	}
	public void setChildTier(String childTier) {
		this.childTier = childTier;
	}
	public double getTrueUsage() {
		return trueUsage;
	}
	public void setTrueUsage(double trueUsage) {
		this.trueUsage = trueUsage;
	}
	public long getContractTypeTarget() {
		return contractTypeTarget;
	}
	public void setContractTypeTarget(long contractTypeTarget) {
		this.contractTypeTarget = contractTypeTarget;
	}
	public long getOverallUsageTarget() {
		return overallUsageTarget;
	}
	public void setOverallUsageTarget(long overallUsageTarget) {
		this.overallUsageTarget = overallUsageTarget;
	}
	public String getFiscalMonthInQuater() {
		return fiscalMonthInQuater;
	}
	public void setFiscalMonthInQuater(String fiscalMonthInQuater) {
		this.fiscalMonthInQuater = fiscalMonthInQuater;
	}
	public Long getLOBTarget() {
		return LOBTarget;
	}
	public void setLOBTarget(Long lOBTarget) {
		LOBTarget = lOBTarget;
	}	
}
