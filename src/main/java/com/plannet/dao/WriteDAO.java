package com.plannet.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.plannet.common.Common;
import com.plannet.vo.MemberVO;
import com.plannet.vo.MemoVO;

public class WriteDAO {
	private Connection conn = null;
	private Statement stmt = null; //표준 SQL문을 수행하기 위한 Statement 객체 얻기
	private ResultSet rs = null; // Statement의 수행 결과를 여러행으로 받음
	// SQL문을 미리 컴파일해서 재 사용하므로 Statement 인터페이스보다 훨씬 빨르게 데이터베이스 작업을 수행
	private PreparedStatement pstmt = null; 

	
//	public  List<MemoVO> memberMemo(String id) {
//		List<MemoVO> list = new ArrayList<>();
//		
//		try {
//			conn = Common.getConnection();
//			stmt = conn.createStatement(); // Statement 객체 얻기
//			String sql = "SELECT * FROM MEMO WHERE ID = " + "'" + id + "'";
//			rs = stmt.executeQuery(sql);
//			
//			while(rs.next()) { // 읽은 데이타가 있으면 true
//				String sqlMemo = rs.getString("MEMO"); // 쿼리문 수행 결과에서 ID값을 가져옴
//				String sqlId = rs.getString("ID");
//				System.out.println("MEMO : " + sqlMemo);
//				MemoVO vo = new MemoVO();
//				vo.setId(sqlId);
//				vo.setMemo(sqlMemo);
//				list.add(vo);
//			}
//			Common.close(rs);
//			Common.close(stmt);
//			Common.close(conn);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		return list;
//	}
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
	    	int cnt = 1;
	    	
	    	for(Map<String, Object> p : plan) {
	    		String deleted = (String) p.get("deleted");
	    		//System.out.println(p.get("deleted"));
	    		if(deleted.equals("false")) {
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

}
