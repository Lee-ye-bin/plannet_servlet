package com.plannet.dao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.plannet.common.Common;
import com.plannet.vo.WriteVO;

public class WriteDAO {
	private Connection conn = null;
	private Statement stmt = null; //표준 SQL문을 수행하기 위한 Statement 객체 얻기
	private ResultSet rs = null; // Statement의 수행 결과를 여러행으로 받음

	// SQL문을 미리 컴파일해서 재 사용하므로 Statement 인터페이스보다 훨씬 빨르게 데이터베이스 작업을 수행
	private PreparedStatement pstmt = null; 

	
	public  List<WriteVO> writeLoad(String id, Date date) {
		List<WriteVO> list = new ArrayList<>();
		
		try {
			//리스트 가져오기
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			
			String sqlListLoad = "SELECT * FROM PLAN WHERE ID = " + "'" + id + "'" + " AND PLAN_DATE = " + "'" + date + "' ORDER BY PLAN_NO ASC";
			rs = stmt.executeQuery(sqlListLoad);
			
			WriteVO vo = new WriteVO();
			
			List<Map<String, Object>> planList = new ArrayList<Map<String, Object>>();
			while(rs.next()) { // 읽은 데이타가 있으면 true
				Map<String, Object> planItem = new LinkedHashMap<String, Object>();
				
				planItem.put("id", rs.getString("PLAN_NO"));
				if(rs.getInt("PLAN_CHECK") == 1) planItem.put("checked", true);
				else planItem.put("checked", false);
				planItem.put("text", rs.getString("PLAN"));
				planItem.put("deleted", false);
				
				planList.add(planItem);
			}
			vo.setPlan(planList);
			
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
			
			
			
			//다이어리 가져오기
			conn = Common.getConnection();
			stmt = conn.createStatement();
			
			String sqlDiaryLoad = "SELECT * FROM DIARY WHERE ID = "+ "'" + id + "'" + " AND DIARY_DATE = " + "'" + date +"'";
			rs = stmt.executeQuery(sqlDiaryLoad);
			while(rs.next()) {
				String sqlDiary = rs.getString("DIARY");
				vo.setDiary(sqlDiary);
			}
			
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
    		//리스트에 담기
			list.add(vo);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
//	
	public void writeSave(String id, Date date, List<Map<String, Object>> plan, String diary) {
		String sqlDiaryCheck = "SELECT * FROM DIARY WHERE ID = '" + id + "' AND DIARY_DATE = '" + date + "'" ;
		String sqlDiaryUdate = "UPDATE DIARY SET DIARY = ? WHERE ID = ? AND DIARY_DATE = ?";
		String sqlDiaryInsert = "INSERT INTO DIARY VALUES(?, ?, ?)";
		String sqlListRemove = "DELETE FROM PLAN WHERE ID = ? AND PLAN_DATE = ?";
		String sqlListInsert = "INSERT INTO PLAN VALUES(?, ?, ?, ?, ?)";
		try {
			conn = Common.getConnection();
			
			//PLAN 일괄삭제
			pstmt = conn.prepareStatement(sqlListRemove); // 미리 만들어둔 쿼리문 양식에 맞춰 넣음
	    	pstmt.setString(1, id);
	    	pstmt.setDate(2, date);
	    	pstmt.executeUpdate();
	    	
	    	//PLAN 저장
	    	int cnt = 0;
	    	
	    	for(Map<String, Object> p : plan) {
	    		boolean deleted = (boolean) p.get("deleted");
	    		//System.out.println(p.get("deleted"));
	    		if(deleted == false) {
	    			pstmt = conn.prepareStatement(sqlListInsert); // 미리 만들어둔 쿼리문 양식에 맞춰 넣음
	    	    	pstmt.setString(1, id);
	    	    	pstmt.setDate(2, date);
	    	    	pstmt.setInt(3, cnt);
	    	    	if(p.get("checked").equals(true)) pstmt.setInt(4, 1);
	    	    	else pstmt.setInt(4, 0);
	    	    	pstmt.setString(5, (String) p.get("text"));
	    	    	pstmt.executeUpdate();
	    	    	cnt++;
	    		} 
	    	}
	    	

			//DIARY 업데이트
			stmt = conn.createStatement(); // Statement 객체 얻기
			rs = stmt.executeQuery(sqlDiaryCheck);
			
			if(rs.next()) {
				pstmt = conn.prepareStatement(sqlDiaryUdate); // 미리 만들어둔 쿼리문 양식에 맞춰 넣음
				pstmt.setString(1, diary);
		    	pstmt.setString(2, id);
		    	pstmt.setDate(3, date);
		    	pstmt.executeUpdate();
			} else {
				pstmt = conn.prepareStatement(sqlDiaryInsert); // 미리 만들어둔 쿼리문 양식에 맞춰 넣음
				pstmt.setString(1, id);
		    	pstmt.setDate(2, date);
		    	pstmt.setString(3, diary);
		    	pstmt.executeUpdate();
			};
			
			
	    	
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
		Common.close(rs);
		Common.close(pstmt);
	    Common.close(conn);
	}
	
	public  List<List<String>> planLoad(String id) {
		List<List<String>> allPlan = new ArrayList<List<String>>();
		try {
			//리스트 가져오기
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			
			String sqlEndPlan = "SELECT * FROM PLAN WHERE ID = '" + id + "' AND PLAN_CHECK = 1";
			rs = stmt.executeQuery(sqlEndPlan);
			
			List<String> endPlan = new ArrayList<String>();
			while(rs.next()) { // 읽은 데이타가 있으면 true
				String date = (rs.getString("PLAN_DATE")).substring(0, 10);
				endPlan.add(date);
			}
			
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
		
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			
			String sqlDoPlan = "SELECT * FROM PLAN WHERE ID = '" + id + "' AND PLAN_CHECK = 0";
			rs = stmt.executeQuery(sqlDoPlan);
			
			List<String> doPlan = new ArrayList<String>();
			while(rs.next()) { // 읽은 데이타가 있으면 true
				String date = (rs.getString("PLAN_DATE")).substring(0, 10);
				doPlan.add(date);
			}
			
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
			
			allPlan.add(endPlan);
			allPlan.add(doPlan);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return allPlan;
	}
	public  Map<String, List<Map<String, Object>>> weekList(String id) {
		Map<String, List<Map<String, Object>>> weekList = new LinkedHashMap<String, List<Map<String, Object>>>();
		
		
		
		try {
			for(int i = 0; i < 7; i++) {
				conn = Common.getConnection();
				stmt = conn.createStatement(); // Statement 객체 얻기
				
				String sql = "SELECT * FROM PLAN WHERE PLAN_DATE = (SELECT TRUNC(sysdate, 'd')+" + i + " FROM DUAL) AND ID = '" + id +  "'";
				String[] weekday = {"sun", "mon", "tue", "wed", "thu", "fri", "sat"};
				
				rs = stmt.executeQuery(sql);
				
				List<Map<String, Object>> dayList = new ArrayList<Map<String, Object>>();
				while(rs.next()) { // 읽은 데이타가 있으면 true	
					Map<String, Object> listItem = new LinkedHashMap<String, Object>();
					listItem.put("no", rs.getString("PLAN_NO"));
					listItem.put("plan", rs.getString("PLAN"));
					listItem.put("checked", rs.getString("PLAN_CHECK"));
					dayList.add(listItem);
				}
				
				Common.close(rs);
				Common.close(stmt);
				Common.close(conn);
				
				weekList.put(weekday[i], dayList);
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return weekList;
	}

}
