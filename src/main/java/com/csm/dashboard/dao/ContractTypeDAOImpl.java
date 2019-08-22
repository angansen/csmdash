package com.csm.dashboard.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class ContractTypeDAOImpl implements ContractTypeDAO{
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Object[]> getQuaterlyByContractType() {
		Query query = entityManager.createNativeQuery("select contract_type, FISCAL_QUATER, sum(true_usage) from usage_data group by fiscal_quater,contract_type order by fiscal_quater");
		List<Object[]> results = query.getResultList();
		return results;
	}
	@Override
	public List<Object[]> getQuaterly() {
		Query query = entityManager.createNativeQuery("select FISCAL_QUATER, sum(true_usage) from usage_data group by fiscal_quater order by fiscal_quater");
		List<Object[]> results = query.getResultList();
		return results;
	}
	@Override
	public List<Object[]> getMonthlyByContractType() {
//		Query query = entityManager.createNativeQuery("select contract_type, TO_DATE(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3),'MON') AS MONTH_VAL, sum(true_usage) from usage_data group by FISCAL_MONTH_IN_QUATER,contract_type ORDER BY MONTH_VAL ASC");
		Query query = entityManager.createNativeQuery("select contract_type,TO_DATE(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3)||(case when" +
						" to_char(TO_date(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3),'mon'),'mm') between 1 and 5 then SUBSTR(FISCAL_MONTH_IN_QUATER,3,2)+1" +
						" else to_number(SUBSTR(FISCAL_MONTH_IN_QUATER,3,2)) end),'monyy' ) AS MONTH_VAL,"+
					    " sum(true_usage) from usage_data"+
					    " group by FISCAL_MONTH_IN_QUATER,contract_type ORDER BY MONTH_VAL ASC");		
		List<Object[]> results = query.getResultList();
		return results;
	}
	@Override
	public List<Object[]> getMonthly() {
//		Query query = entityManager.createNativeQuery("select TO_DATE(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3),'MON') AS MONTH_VAL, sum(true_usage) from usage_data group by FISCAL_MONTH_IN_QUATER ORDER BY MONTH_VAL ASC");
		Query query = entityManager.createNativeQuery("select TO_DATE(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3)||(case when"+
						" to_char(TO_date(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3),'mon'),'mm') between 1 and 5 then SUBSTR(FISCAL_MONTH_IN_QUATER,3,2)+1"+
						" else to_number(SUBSTR(FISCAL_MONTH_IN_QUATER,3,2)) end),'monyy' ) AS MONTH_VAL,"+
						" sum(true_usage) from usage_data"+
						" group by FISCAL_MONTH_IN_QUATER ORDER BY MONTH_VAL ASC  ");
		List<Object[]> results = query.getResultList();
		return results;
	}

	@Override
	public List<Object[]> getWeeklyByContractType() {
		Query query = entityManager.createNativeQuery("select contract_type,FISCAL_WEEK_IN_QUATER, sum(true_usage) from usage_data group by FISCAL_WEEK_IN_QUATER, contract_type order by FISCAL_WEEK_IN_QUATER ASC");
		List<Object[]> results = query.getResultList();
		return results;	
	}
	@Override
	public List<Object[]> getWeekly() {
		Query query = entityManager.createNativeQuery("select FISCAL_WEEK_IN_QUATER, sum(true_usage) from usage_data group by FISCAL_WEEK_IN_QUATER order by FISCAL_WEEK_IN_QUATER ASC");
		List<Object[]> results = query.getResultList();
		return results;	
	}
	
	public List<Object[]> getYearlyByContractType(String kpiType) {
		String queryString = null;
		if (kpiType.equalsIgnoreCase("contract")){
			queryString = "select contract_type, sum(true_usage) from usage_data group by contract_type";
		}
		else if (kpiType.equalsIgnoreCase("lob")){
			queryString = "select METEREDLOB, sum(true_usage) from usage_data group by METEREDLOB";
		}
		else if (kpiType.equalsIgnoreCase("product")){
			queryString = "select * from ( SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
				" GROUP BY CHILD_TIER UNION SELECT 'Autonomous ADW '||'&'||' ATP', sum(TRUE_USAGE)"+
				" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+
				" UNION SELECT 'Autonomous Other Services', sum(TRUE_USAGE)"+
				" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%') ORDER BY 2 DESC";
		}
		else if (kpiType.equalsIgnoreCase("autonomous")){
			queryString = "select * from ( SELECT 'All', sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER LIKE 'Autonomous%'"+
				" UNION SELECT 'Autonomous ADW '||'&'||' ATP', sum(TRUE_USAGE)"+
				" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+
				" UNION SELECT 'Autonomous Other Services', sum(TRUE_USAGE)"+
				" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%') ORDER BY 2 DESC";
		}

		Query query = entityManager.createNativeQuery(queryString);
		List<Object[]> results = query.getResultList();
		return results;	
	}
	public List<Object[]> getLOB(String contractType, String periodType, String periodValue) {
		System.out.println("contractType "+contractType+" periodType"+periodType+" periodValue"+periodValue);
		List<Object[]> results = null;
		Query query = null;
		if(periodType.equalsIgnoreCase("quater")) {
			String updatedQuater = "'FY20-"+periodValue+"'";
			if(contractType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS "
						+ "SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA where fiscal_quater = "+updatedQuater+" GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			else {
				String contractTypeVal = "'"+contractType+"'";
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE contract_type = "+contractTypeVal+" AND fiscal_quater = "+updatedQuater+ " GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("month")) {
			String updatedMonth = "'FY20-"+periodValue+"'";
			if(contractType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS "
						+ "SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA where FISCAL_MONTH_IN_QUATER = "+updatedMonth+" GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			else {
				String contractTypeVal = "'"+contractType+"'";
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE contract_type = "+contractTypeVal+" AND FISCAL_MONTH_IN_QUATER = "+updatedMonth+ " GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("week")) {
			String updatedWeek = "'"+periodValue+"'";
			if(contractType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS "
						+ "SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA where FISCAL_WEEK_IN_QUATER = "+updatedWeek+" GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			else {
				String contractTypeVal = "'"+contractType+"'";
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE contract_type = "+contractTypeVal+" AND FISCAL_WEEK_IN_QUATER = "+updatedWeek+ " GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("year")) {
			String updatedYear = "'"+periodValue+"'";
			if(contractType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS "
						+ "SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			else {
				String contractTypeVal = "'"+contractType+"'";
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE contract_type = "+contractTypeVal+" GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			results = query.getResultList();
		}

		return results;	
	}
	public List<Object[]> getProducts(String contractType, String periodType, String periodValue) {
		List<Object[]> results = null;
		Query query = null;
		if(periodType.equalsIgnoreCase("quater")) {
			String updatedQuater = "'FY20-"+periodValue+"'";
			if(contractType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" AND fiscal_quater ="+ updatedQuater+" "
								+ "GROUP BY CHILD_TIER UNION SELECT 'Autonomous ADW '||'&'||' ATP', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+ 
						" AND fiscal_quater = "+updatedQuater+" UNION SELECT 'Autonomous Other Services', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+ 
						" AND fiscal_quater = "+updatedQuater+ "ORDER BY 2 DESC");
			}
			else {
				String contractTypeVal = "'"+contractType+"'";			
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" AND fiscal_quater ="+ updatedQuater+" AND contract_type="+contractTypeVal+
						" GROUP BY CHILD_TIER UNION SELECT 'Autonomous ADW '||'&'||' ATP', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+ 
						" AND fiscal_quater = "+updatedQuater+" AND contract_type="+contractTypeVal+
						" UNION SELECT 'Autonomous Other Services', sum(TRUE_USAGE)"+
							" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+ 
							" AND fiscal_quater = "+updatedQuater+" AND contract_type="+contractTypeVal+
						" ORDER BY 2 DESC");
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("month")) {
			String updatedMonth = "'FY20-"+periodValue+"'";
			if(contractType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" AND FISCAL_MONTH_IN_QUATER ="+ updatedMonth+" "
								+ "GROUP BY CHILD_TIER UNION SELECT 'Autonomous ADW '||'&'||' ATP', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+ 
						" AND FISCAL_MONTH_IN_QUATER = "+updatedMonth+" UNION SELECT 'Autonomous Other Services', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+ 
						" AND FISCAL_MONTH_IN_QUATER = "+updatedMonth+ "ORDER BY 2 DESC");
				
			}
			else {
				String contractTypeVal = "'"+contractType+"'";			
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" AND FISCAL_MONTH_IN_QUATER ="+ updatedMonth+" AND contract_type="+contractTypeVal+
						" GROUP BY CHILD_TIER UNION SELECT 'Autonomous ADW '||'&'||' ATP', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+ 
						" AND FISCAL_MONTH_IN_QUATER = "+updatedMonth+" AND contract_type="+contractTypeVal+
						" UNION SELECT 'Autonomous Other Services', sum(TRUE_USAGE)"+
							" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+ 
							" AND FISCAL_MONTH_IN_QUATER = "+updatedMonth+" AND contract_type="+contractTypeVal+
						" ORDER BY 2 DESC");
				
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("week")) {
			String updatedWeek = "'"+periodValue+"'";
			if(contractType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" AND FISCAL_WEEK_IN_QUATER ="+ updatedWeek+" "
								+ "GROUP BY CHILD_TIER UNION SELECT 'Autonomous ADW '||'&'||' ATP', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+ 
						" AND FISCAL_WEEK_IN_QUATER = "+updatedWeek+" UNION SELECT 'Autonomous Other Services', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+ 
						" AND FISCAL_WEEK_IN_QUATER = "+updatedWeek+ "ORDER BY 2 DESC");
				
			}
			else {
				String contractTypeVal = "'"+contractType+"'";
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" AND FISCAL_WEEK_IN_QUATER ="+ updatedWeek+" AND contract_type="+contractTypeVal+
						" GROUP BY CHILD_TIER UNION SELECT 'Autonomous ADW '||'&'||' ATP', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+ 
						" AND FISCAL_WEEK_IN_QUATER = "+updatedWeek+" AND contract_type="+contractTypeVal+
						" UNION SELECT 'Autonomous Other Services', sum(TRUE_USAGE)"+
							" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+ 
							" AND FISCAL_WEEK_IN_QUATER = "+updatedWeek+" AND contract_type="+contractTypeVal+
						" ORDER BY 2 DESC");
				
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("year")) {
			if(contractType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" GROUP BY CHILD_TIER UNION SELECT 'Autonomous ADW '||'&'||' ATP', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+ 
						" UNION SELECT 'Autonomous Other Services', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+ 
						" ORDER BY 2 DESC");
			}
			else {
				String contractTypeVal = "'"+contractType+"'";			
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" AND contract_type="+contractTypeVal+
						" GROUP BY CHILD_TIER UNION SELECT 'Autonomous ADW '||'&'||' ATP', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+ 
						" AND contract_type="+contractTypeVal+
						" UNION SELECT 'Autonomous Other Services', sum(TRUE_USAGE)"+
							" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+ 
							" AND contract_type="+contractTypeVal+
						" ORDER BY 2 DESC");
			}
			results = query.getResultList();
		}		
		return results;	
	}

}
