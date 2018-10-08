package com.csm.dashboard.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class ProductDAOImpl implements ProductDAO{
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Object[]> getQuaterlyByProduct() {
		Query query = entityManager.createNativeQuery("select * from ("+
			" SELECT CHILD_TIER,FISCAL_QUATER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
			" GROUP BY CHILD_TIER,FISCAL_QUATER"+ 
			" UNION SELECT 'Autonomous ADW '||'&'||' ATP', FISCAL_QUATER, sum(TRUE_USAGE)"+
			" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+
			" GROUP BY FISCAL_QUATER UNION"+
			" SELECT 'Autonomous Other Services',FISCAL_QUATER, sum(TRUE_USAGE)"+
			" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+
			" GROUP BY FISCAL_QUATER) ORDER BY 2");
		List<Object[]> results = query.getResultList();
		return results;
	}
	@Override
	public List<Object[]> getQuaterly() {
		Query query = entityManager.createNativeQuery("select FISCAL_QUATER, sum(true_usage) from usage_data group by fiscal_quater");
		List<Object[]> results = query.getResultList();
		return results;
	}
	@Override
	public List<Object[]> getMonthlyByProduct() {
		Query query = entityManager.createNativeQuery("select * from ("+
				" SELECT CHILD_TIER,TO_DATE(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3),'MON'), sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
				" GROUP BY CHILD_TIER,fiscal_month_in_quater"+ 
				" UNION SELECT 'Autonomous ADW '||'&'||' ATP', TO_DATE(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3),'MON'), sum(TRUE_USAGE)"+
				" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+
				" GROUP BY fiscal_month_in_quater UNION"+
				" SELECT 'Autonomous Other Services',TO_DATE(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3),'MON'), sum(TRUE_USAGE)"+
				" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+
				" GROUP BY fiscal_month_in_quater) ORDER BY 2 ASC");
		List<Object[]> results = query.getResultList();
		return results;
	}
	@Override
	public List<Object[]> getMonthly() {
		Query query = entityManager.createNativeQuery("select TO_DATE(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3),'MON') AS MONTH_VAL, sum(true_usage) from usage_data group by FISCAL_MONTH_IN_QUATER ORDER BY MONTH_VAL ASC");
		List<Object[]> results = query.getResultList();
		return results;
	}

	@Override
	public List<Object[]> getWeeklyByProduct() {
		Query query = entityManager.createNativeQuery("select * from ("+
				"SELECT CHILD_TIER,fiscal_week_in_quater, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
				" GROUP BY CHILD_TIER,fiscal_week_in_quater"+ 
				" UNION SELECT 'Autonomous ADW '||'&'||' ATP', fiscal_week_in_quater, sum(TRUE_USAGE)"+
				" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+
				" GROUP BY fiscal_week_in_quater UNION"+
				" SELECT 'Autonomous Other Services',fiscal_week_in_quater, sum(TRUE_USAGE)"+
				" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+
				" GROUP BY fiscal_week_in_quater) ORDER BY 2");
		List<Object[]> results = query.getResultList();
		return results;	
	}
	@Override
	public List<Object[]> getWeekly() {
		Query query = entityManager.createNativeQuery("select FISCAL_WEEK_IN_QUATER, sum(true_usage) from usage_data group by FISCAL_WEEK_IN_QUATER order by FISCAL_WEEK_IN_QUATER ASC");
		List<Object[]> results = query.getResultList();
		return results;	
	}
	public List<Object[]> getContractType(String productType, String periodType, String periodValue) {
		List<Object[]> results = null;
		Query query = null;
		if(periodType.equalsIgnoreCase("quater")) {
			String updatedQuater = "'FY19-"+periodValue+"'";
			if(productType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT contract_type, sum(TRUE_USAGE) AS "
						+ "SUM_TRUE_USAGE,MAX(contract_type_target) FROM USAGE_DATA where fiscal_quater = "+updatedQuater+" GROUP BY contract_type ORDER BY SUM_TRUE_USAGE DESC");
			}
			else {
				String ProductTypeVal = "'"+productType+"%"+"'";
				query = entityManager.createNativeQuery("SELECT contract_type, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(contract_type_target) FROM USAGE_DATA" + 
						" WHERE CHILD_TIER LIKE "+ProductTypeVal+" AND fiscal_quater = "+updatedQuater+ " GROUP BY contract_type ORDER BY SUM_TRUE_USAGE DESC");
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("month")) {
			String updatedMonth = "'FY19-"+periodValue+"'";
			if(productType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT contract_type, sum(TRUE_USAGE) AS "
						+ "SUM_TRUE_USAGE,MAX(contract_type_target) FROM USAGE_DATA where FISCAL_MONTH_IN_QUATER = "+updatedMonth+" GROUP BY contract_type ORDER BY SUM_TRUE_USAGE DESC");
			}
			else {
				String ProductTypeVal = "'"+productType+"%"+"'";
				query = entityManager.createNativeQuery("SELECT contract_type, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(contract_type_target) FROM USAGE_DATA" + 
						" WHERE CHILD_TIER LIKE "+ProductTypeVal+" AND FISCAL_MONTH_IN_QUATER = "+updatedMonth+ " GROUP BY contract_type ORDER BY SUM_TRUE_USAGE DESC");
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("week")) {
			String updatedWeek = "'"+periodValue+"'";
			if(productType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT contract_type, sum(TRUE_USAGE) AS "
						+ "SUM_TRUE_USAGE,MAX(contract_type_target) FROM USAGE_DATA where FISCAL_WEEK_IN_QUATER = "+updatedWeek+" GROUP BY contract_type ORDER BY SUM_TRUE_USAGE DESC");
			}
			else {
				String ProductTypeVal = "'"+productType+"%"+"'";
				query = entityManager.createNativeQuery("SELECT contract_type, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(contract_type_target) FROM USAGE_DATA" + 
						" WHERE CHILD_TIER LIKE "+ProductTypeVal+" AND FISCAL_WEEK_IN_QUATER = "+updatedWeek+ " GROUP BY contract_type ORDER BY SUM_TRUE_USAGE DESC");
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("year")) {
			String updatedYear = "'"+periodValue+"'";
			if(productType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT contract_type, sum(TRUE_USAGE) AS "
						+ "SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA GROUP BY contract_type ORDER BY SUM_TRUE_USAGE DESC");
			}
			else {
				String productVal = "'"+productType+"'";
				query = entityManager.createNativeQuery("SELECT contract_type, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE CHILD_TIER = "+productVal+" GROUP BY contract_type ORDER BY SUM_TRUE_USAGE DESC");
			}
			results = query.getResultList();
		}
		
		return results;	
	}
	public List<Object[]> getLOB(String productType, String periodType, String periodValue) {
		List<Object[]> results = null;
		Query query = null;
		if(periodType.equalsIgnoreCase("quater")) {
			String updatedQuater = "'FY19-"+periodValue+"'";
			if(productType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS "
						+ "SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA where fiscal_quater = "+updatedQuater+" GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			else {
				String ProductTypeVal = "'"+productType+"%"+"'";
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE CHILD_TIER LIKE "+ProductTypeVal+" AND fiscal_quater = "+updatedQuater+ " GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("month")) {
			String updatedMonth = "'FY19-"+periodValue+"'";
			if(productType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS "
						+ "SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA where FISCAL_MONTH_IN_QUATER = "+updatedMonth+" GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			else {
				String ProductTypeVal = "'"+productType+"%"+"'";
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE CHILD_TIER LIKE "+ProductTypeVal+" AND FISCAL_MONTH_IN_QUATER = "+updatedMonth+ " GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("week")) {
			String updatedWeek = "'"+periodValue+"'";
			if(productType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS "
						+ "SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA where FISCAL_WEEK_IN_QUATER = "+updatedWeek+" GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			else {
				String ProductTypeVal = "'"+productType+"%"+"'";
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE CHILD_TIER LIKE "+ProductTypeVal+" AND FISCAL_WEEK_IN_QUATER = "+updatedWeek+ " GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("year")) {
			String updatedYear = "'"+periodValue+"'";
			if(productType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS "
						+ "SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			else {
				String ProductTypeVal = "'"+productType+"%"+"'";
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE CHILD_TIER LIKE "+ProductTypeVal+" GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			results = query.getResultList();
		}
		
		return results;	
	}
}
