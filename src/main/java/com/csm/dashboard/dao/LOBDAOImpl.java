package com.csm.dashboard.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class LOBDAOImpl implements LOBDAO{
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Object[]> getQuaterlyByLOB() {
		Query query = entityManager.createNativeQuery("select METEREDLOB, FISCAL_QUATER, sum(true_usage) from usage_data group by fiscal_quater,METEREDLOB order by fiscal_quater");
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
	public List<Object[]> getMonthlyByLOB() {
		Query query = entityManager.createNativeQuery("select METEREDLOB, TO_DATE(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3),'MON') AS MONTH_VAL, sum(true_usage) from usage_data group by FISCAL_MONTH_IN_QUATER,METEREDLOB ORDER BY MONTH_VAL ASC");
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
	public List<Object[]> getWeeklyByLOB() {
		Query query = entityManager.createNativeQuery("select METEREDLOB,FISCAL_WEEK_IN_QUATER, sum(true_usage) from usage_data group by FISCAL_WEEK_IN_QUATER, METEREDLOB order by FISCAL_WEEK_IN_QUATER ASC");
		List<Object[]> results = query.getResultList();
		return results;	
	}
	@Override
	public List<Object[]> getWeekly() {
		Query query = entityManager.createNativeQuery("select FISCAL_WEEK_IN_QUATER, sum(true_usage) from usage_data group by FISCAL_WEEK_IN_QUATER order by FISCAL_WEEK_IN_QUATER ASC");
		List<Object[]> results = query.getResultList();
		return results;	
	}
	public List<Object[]> getContractType(String LOB, String periodType, String periodValue) {
		System.out.println("LOB "+LOB+" periodType"+periodType+" periodValue"+periodValue);
		List<Object[]> results = null;
		Query query = null;
		if(periodType.equalsIgnoreCase("quater")) {
			String updatedQuater = "'FY19-"+periodValue+"'";
			if(LOB.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT contract_type, sum(TRUE_USAGE) AS "
						+ "SUM_TRUE_USAGE,MAX(contract_type_target) FROM USAGE_DATA where fiscal_quater = "+updatedQuater+" GROUP BY contract_type ORDER BY SUM_TRUE_USAGE DESC");
			}
			else {
				String LOBVal = "'"+LOB+"'";
				query = entityManager.createNativeQuery("SELECT contract_type, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(contract_type_target) FROM USAGE_DATA" + 
						" WHERE METEREDLOB = "+LOBVal+" AND fiscal_quater = "+updatedQuater+ " GROUP BY contract_type ORDER BY SUM_TRUE_USAGE DESC");
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("month")) {
			String updatedMonth = "'FY19-"+periodValue+"'";
			if(LOB.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT contract_type, sum(TRUE_USAGE) AS "
						+ "SUM_TRUE_USAGE,MAX(contract_type_target) FROM USAGE_DATA where FISCAL_MONTH_IN_QUATER = "+updatedMonth+" GROUP BY contract_type ORDER BY SUM_TRUE_USAGE DESC");
			}
			else {
				String LOBVal = "'"+LOB+"'";
				query = entityManager.createNativeQuery("SELECT contract_type, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(contract_type_target) FROM USAGE_DATA" + 
						" WHERE METEREDLOB = "+LOBVal+" AND FISCAL_MONTH_IN_QUATER = "+updatedMonth+ " GROUP BY contract_type ORDER BY SUM_TRUE_USAGE DESC");
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("week")) {
			String updatedWeek = "'"+periodValue+"'";
			if(LOB.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT contract_type, sum(TRUE_USAGE) AS "
						+ "SUM_TRUE_USAGE,MAX(contract_type_target) FROM USAGE_DATA where FISCAL_WEEK_IN_QUATER = "+updatedWeek+" GROUP BY contract_type ORDER BY SUM_TRUE_USAGE DESC");
			}
			else {
				String LOBVal = "'"+LOB+"'";
				query = entityManager.createNativeQuery("SELECT contract_type, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(contract_type_target) FROM USAGE_DATA" + 
						" WHERE METEREDLOB = "+LOBVal+" AND FISCAL_WEEK_IN_QUATER = "+updatedWeek+ " GROUP BY contract_type ORDER BY SUM_TRUE_USAGE DESC");
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("year")) {
			String updatedYear = "'"+periodValue+"'";
			if(LOB.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT contract_type, sum(TRUE_USAGE) AS "
						+ "SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA GROUP BY contract_type ORDER BY SUM_TRUE_USAGE DESC");
			}
			else {
				String lobVal = "'"+LOB+"'";
				query = entityManager.createNativeQuery("SELECT contract_type, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE METEREDLOB = "+lobVal+" GROUP BY contract_type ORDER BY SUM_TRUE_USAGE DESC");
			}
			results = query.getResultList();
		}
		
		return results;	
	}
	public List<Object[]> getProducts(String LOB, String periodType, String periodValue) {
		System.out.println("LOB "+LOB+" periodType"+periodType+" periodValue"+periodValue);
		List<Object[]> results = null;
		Query query = null;
		if(periodType.equalsIgnoreCase("quater")) {
			String updatedQuater = "'FY19-"+periodValue+"'";
			if(LOB.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" AND fiscal_quater ="+ updatedQuater+" "
								+ "GROUP BY CHILD_TIER UNION SELECT 'Autonomous ADW '||'&'||' ATP', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+ 
						" AND fiscal_quater = "+updatedQuater+" UNION SELECT 'Autonomous Other Services', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+ 
						" AND fiscal_quater = "+updatedQuater+ "ORDER BY 2 DESC");
				
			}
			else {
				String LOBVal = "'"+LOB+"'";			
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" AND fiscal_quater ="+ updatedQuater+" AND METEREDLOB="+LOBVal+
						" GROUP BY CHILD_TIER UNION SELECT 'Autonomous ADW '||'&'||' ATP', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+ 
						" AND fiscal_quater = "+updatedQuater+" AND METEREDLOB="+LOBVal+
						" UNION SELECT 'Autonomous Other Services', sum(TRUE_USAGE)"+
							" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+ 
							" AND fiscal_quater = "+updatedQuater+" AND METEREDLOB="+LOBVal+
						" ORDER BY 2 DESC");
				
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("month")) {
			String updatedMonth = "'FY19-"+periodValue+"'";
			if(LOB.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" AND FISCAL_MONTH_IN_QUATER ="+ updatedMonth+" "
								+ "GROUP BY CHILD_TIER UNION SELECT 'Autonomous ADW '||'&'||' ATP', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+ 
						" AND FISCAL_MONTH_IN_QUATER = "+updatedMonth+" UNION SELECT 'Autonomous Other Services', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+ 
						" AND FISCAL_MONTH_IN_QUATER = "+updatedMonth+ "ORDER BY 2 DESC");
			}
			else {
				String LOBVal = "'"+LOB+"'";			
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" AND FISCAL_MONTH_IN_QUATER ="+ updatedMonth+" AND METEREDLOB="+LOBVal+
						" GROUP BY CHILD_TIER UNION SELECT 'Autonomous ADW '||'&'||' ATP', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+ 
						" AND FISCAL_MONTH_IN_QUATER = "+updatedMonth+" AND METEREDLOB="+LOBVal+
						" UNION SELECT 'Autonomous Other Services', sum(TRUE_USAGE)"+
							" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+ 
							" AND FISCAL_MONTH_IN_QUATER = "+updatedMonth+" AND METEREDLOB="+LOBVal+
						" ORDER BY 2 DESC");
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("week")) {
			String updatedWeek = "'"+periodValue+"'";
			if(LOB.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" AND FISCAL_WEEK_IN_QUATER ="+ updatedWeek+" "
								+ "GROUP BY CHILD_TIER UNION SELECT 'Autonomous ADW '||'&'||' ATP', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+ 
						" AND FISCAL_WEEK_IN_QUATER = "+updatedWeek+" UNION SELECT 'Autonomous Other Services', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+ 
						" AND FISCAL_WEEK_IN_QUATER = "+updatedWeek+ "ORDER BY 2 DESC");
			}
			else {
				String LOBVal = "'"+LOB+"'";
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" AND FISCAL_WEEK_IN_QUATER ="+ updatedWeek+" AND METEREDLOB="+LOBVal+
						" GROUP BY CHILD_TIER UNION SELECT 'Autonomous ADW '||'&'||' ATP', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+ 
						" AND FISCAL_WEEK_IN_QUATER = "+updatedWeek+" AND METEREDLOB="+LOBVal+
						" UNION SELECT 'Autonomous Other Services', sum(TRUE_USAGE)"+
							" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+ 
							" AND FISCAL_WEEK_IN_QUATER = "+updatedWeek+" AND METEREDLOB="+LOBVal+
						" ORDER BY 2 DESC");
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("year")) {
			if(LOB.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" GROUP BY CHILD_TIER UNION SELECT 'Autonomous ADW '||'&'||' ATP', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+ 
						" UNION SELECT 'Autonomous Other Services', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+ 
						" ORDER BY 2 DESC");
			}
			else {
				String LOBVal = "'"+LOB+"'";			
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" AND METEREDLOB="+LOBVal+
						" GROUP BY CHILD_TIER UNION SELECT 'Autonomous ADW '||'&'||' ATP', sum(TRUE_USAGE)"+
						" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous ADW '||'&'||' ATP%'"+ 
						" AND METEREDLOB="+LOBVal+
						" UNION SELECT 'Autonomous Other Services', sum(TRUE_USAGE)"+
							" FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous Other Services%'"+ 
							" AND METEREDLOB="+LOBVal+
						" ORDER BY 2 DESC");
			}
			results = query.getResultList();
		}		
		return results;	
	}
}
