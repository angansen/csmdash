package com.csm.dashboard.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class AutonomousDAOImpl {

	@PersistenceContext
	private EntityManager entityManager;

	public List<Object[]> getQuaterlyByAutonomous() {
		Query query = entityManager.createNativeQuery("select * from ("+
			" SELECT 'All',FISCAL_QUATER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER LIKE 'Autonomous%'"+
			" GROUP BY FISCAL_QUATER"+ 
			" UNION SELECT 'Autonomous ADW '||'&'||' ATP', FISCAL_QUATER, sum(TRUE_USAGE)"+
			" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+
			" GROUP BY FISCAL_QUATER UNION"+
			" SELECT 'Autonomous Other Services',FISCAL_QUATER, sum(TRUE_USAGE)"+
			" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+
			" GROUP BY FISCAL_QUATER) ORDER BY 2");
		List<Object[]> results = query.getResultList();
		return results;
	}

	public List<Object[]> getMonthlyByAutonomous() {
//		Query query = entityManager.createNativeQuery("select * from ("+
//				" SELECT 'All',TO_DATE(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3),'MON'), sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER LIKE 'Autonomous%'"+
//				" GROUP BY fiscal_month_in_quater"+ 
//				" UNION SELECT 'Autonomous ADW '||'&'||' ATP', TO_DATE(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3),'MON'), sum(TRUE_USAGE)"+
//				" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+
//				" GROUP BY fiscal_month_in_quater UNION"+
//				" SELECT 'Autonomous Other Services',TO_DATE(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3),'MON'), sum(TRUE_USAGE)"+
//				" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+
//				" GROUP BY fiscal_month_in_quater) ORDER BY 2 ASC");
		Query query = entityManager.createNativeQuery("select * from (SELECT 'All',TO_DATE(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3)||(case when"+
							" to_char(TO_date(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3),'mon'),'mm') between 1 and 5 then SUBSTR(FISCAL_MONTH_IN_QUATER,3,2)+1"+
							" else to_number(SUBSTR(FISCAL_MONTH_IN_QUATER,3,2)) end),'monyy' ) AS MONTH_VAL, sum(TRUE_USAGE) FROM USAGE_DATA"+  
							" where CHILD_TIER LIKE 'Autonomous%' GROUP BY fiscal_month_in_quater UNION SELECT 'Autonomous ADW '||'&'||' ATP',"+
							" TO_DATE(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3)||(case when"+
							" to_char(TO_date(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3),'mon'),'mm') between 1 and 5 then SUBSTR(FISCAL_MONTH_IN_QUATER,3,2)+1"+
							" else to_number(SUBSTR(FISCAL_MONTH_IN_QUATER,3,2)) end),'monyy' ) AS MONTH_VAL, sum(TRUE_USAGE) FROM USAGE_DATA WHERE CHILD_TIER"+ 
							" LIKE 'Autonomous ADW '||'&'||' ATP%' GROUP BY fiscal_month_in_quater UNION SELECT 'Autonomous Other Services',"+
							" TO_DATE(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3)||(case when"+
							" to_char(TO_date(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3),'mon'),'mm') between 1 and 5 then SUBSTR(FISCAL_MONTH_IN_QUATER,3,2)+1"+
							" else to_number(SUBSTR(FISCAL_MONTH_IN_QUATER,3,2)) end),'monyy' ) AS MONTH_VAL, sum(TRUE_USAGE) FROM USAGE_DATA WHERE CHILD_TIER LIKE"+ 
							" 'Autonomous Other Services%' GROUP BY fiscal_month_in_quater) ORDER BY 2 ASC");

		List<Object[]> results = query.getResultList();
		return results;
	}

	public List<Object[]> getWeeklyByAutonomous() {
		Query query = entityManager.createNativeQuery("select * from ("+
				"SELECT 'All', fiscal_week_in_quater, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER LIKE 'Autonomous%'"+
				" GROUP BY fiscal_week_in_quater"+ 
				" UNION SELECT 'Autonomous ADW '||'&'||' ATP', fiscal_week_in_quater, sum(TRUE_USAGE)"+
				" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+
				" GROUP BY fiscal_week_in_quater UNION"+
				" SELECT 'Autonomous Other Services',fiscal_week_in_quater, sum(TRUE_USAGE)"+
				" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+
				" GROUP BY fiscal_week_in_quater) ORDER BY 2");
		List<Object[]> results = query.getResultList();
		return results;	
	}

	public List<Object[]> getContractType(String autonomousType, String periodType, String periodValue) {
		List<Object[]> results = null;
		Query query = null;
		if(periodType.equalsIgnoreCase("quater")) {
			String updatedQuater = "'FY20-"+periodValue+"'";
				String ProductTypeVal = "'"+autonomousType+"%"+"'";
				query = entityManager.createNativeQuery("SELECT contract_type, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(contract_type_target) FROM USAGE_DATA" + 
						" WHERE CHILD_TIER LIKE "+ProductTypeVal+" AND fiscal_quater = "+updatedQuater+ " GROUP BY contract_type ORDER BY SUM_TRUE_USAGE DESC");
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("month")) {
			String updatedMonth = "'FY20-"+periodValue+"'";
				String ProductTypeVal = "'"+autonomousType+"%"+"'";
				query = entityManager.createNativeQuery("SELECT contract_type, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(contract_type_target) FROM USAGE_DATA" + 
						" WHERE CHILD_TIER LIKE "+ProductTypeVal+" AND FISCAL_MONTH_IN_QUATER = "+updatedMonth+ " GROUP BY contract_type ORDER BY SUM_TRUE_USAGE DESC");
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("week")) {
			String updatedWeek = "'"+periodValue+"'";
				String ProductTypeVal = "'"+autonomousType+"%"+"'";
				query = entityManager.createNativeQuery("SELECT contract_type, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(contract_type_target) FROM USAGE_DATA" + 
						" WHERE CHILD_TIER LIKE "+ProductTypeVal+" AND FISCAL_WEEK_IN_QUATER = "+updatedWeek+ " GROUP BY contract_type ORDER BY SUM_TRUE_USAGE DESC");
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("year")) {
			String updatedWeek = "'"+periodValue+"'";
				String ProductTypeVal = "'"+autonomousType+"%"+"'";
				query = entityManager.createNativeQuery("SELECT contract_type, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(contract_type_target) FROM USAGE_DATA" + 
						" WHERE CHILD_TIER LIKE "+ProductTypeVal+" GROUP BY contract_type ORDER BY SUM_TRUE_USAGE DESC");
			results = query.getResultList();
		}

		return results;		
	}

	public List<Object[]> getLOB(String autonomousType, String periodType, String periodValue) {
		List<Object[]> results = null;
		Query query = null;
		if(periodType.equalsIgnoreCase("quater")) {
			String updatedQuater = "'FY20-"+periodValue+"'";
				String ProductTypeVal = "'"+autonomousType+"%"+"'";
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE CHILD_TIER LIKE "+ProductTypeVal+" AND fiscal_quater = "+updatedQuater+ " GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("month")) {
			String updatedMonth = "'FY20-"+periodValue+"'";
				String ProductTypeVal = "'"+autonomousType+"%"+"'";
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE CHILD_TIER LIKE "+ProductTypeVal+" AND FISCAL_MONTH_IN_QUATER = "+updatedMonth+ " GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("week")) {
			String updatedWeek = "'"+periodValue+"'";
				String ProductTypeVal = "'"+autonomousType+"%"+"'";
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE CHILD_TIER LIKE "+ProductTypeVal+" AND FISCAL_WEEK_IN_QUATER = "+updatedWeek+ " GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("year")) {
			String updatedWeek = "'"+periodValue+"'";
				String ProductTypeVal = "'"+autonomousType+"%"+"'";
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE CHILD_TIER LIKE "+ProductTypeVal+" GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			results = query.getResultList();
		}

		return results;	
	}

	public List<Object[]> getAutonomous(String autonomousType, String periodType, String periodValue) {
		List<Object[]> results = null;
		Query query = null;
		if(periodType.equalsIgnoreCase("quater")) {
			String updatedQuater = "'FY20-"+periodValue+"'";
				String ProductTypeVal = "'"+autonomousType+"%"+"'";
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE CHILD_TIER LIKE "+ProductTypeVal+" AND fiscal_quater = "+updatedQuater+ " GROUP BY CHILD_TIER ORDER BY SUM_TRUE_USAGE DESC");
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("month")) {
			String updatedMonth = "'FY20-"+periodValue+"'";
				String ProductTypeVal = "'"+autonomousType+"%"+"'";
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE CHILD_TIER LIKE "+ProductTypeVal+" AND FISCAL_MONTH_IN_QUATER = "+updatedMonth+ " GROUP BY CHILD_TIER ORDER BY SUM_TRUE_USAGE DESC");
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("week")) {
			String updatedWeek = "'"+periodValue+"'";
				String ProductTypeVal = "'"+autonomousType+"%"+"'";
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE CHILD_TIER LIKE "+ProductTypeVal+" AND FISCAL_WEEK_IN_QUATER = "+updatedWeek+ " GROUP BY CHILD_TIER ORDER BY SUM_TRUE_USAGE DESC");
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("year")) {
			String updatedWeek = "'"+periodValue+"'";
				String ProductTypeVal = "'"+autonomousType+"%"+"'";
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE CHILD_TIER LIKE "+ProductTypeVal+" GROUP BY CHILD_TIER ORDER BY SUM_TRUE_USAGE DESC");
			results = query.getResultList();
		}
		
		return results;	
	}
}
