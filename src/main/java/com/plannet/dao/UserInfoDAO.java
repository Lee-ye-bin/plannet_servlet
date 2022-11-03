package com.plannet.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.plannet.common.Common;
import com.plannet.vo.MemberVO;

public class UserInfoDAO {
	private Connection conn = null;
	private Statement stmt = null; //표준 SQL문을 수행하기 위한 Statement 객체 얻기
	private ResultSet rs = null; // Statement의 수행 결과를 여러행으로 받음
	// SQL문을 미리 컴파일해서 재 사용하므로 Statement 인터페이스보다 훨씬 빨르게 데이터베이스 작업을 수행
	private PreparedStatement pstmt = null; 

	
	public  List<MemberVO> userInfoLoad(String id) {
		List<MemberVO> list = new ArrayList<>();
		
		try {
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			String sql = "SELECT * FROM MEMBER WHERE ID = '" + id + "'";
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) { // 읽은 데이타가 있으면 true
				String sqlNickname = rs.getString("NICKNAME");
				String sqlEmail = rs.getString("EMAIL");
				String sqlPhone = rs.getString("TEL");
				String sqlSNS = rs.getString("SNS");
				String sqlProfile = rs.getString("PROFILE");
				MemberVO vo = new MemberVO();
				vo.setNickname(sqlNickname);
				vo.setEmail(sqlEmail);
				vo.setTel(sqlPhone);
				vo.setSns(sqlSNS);
				vo.setProfile(sqlProfile);
				list.add(vo);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		Common.close(rs);
		Common.close(stmt);
		Common.close(conn);
		return list;
	}
	
	public void userInfoSave(String id, String nickname, String email, String phone, String sns, String profile) {
		String sqlNickUdate = "UPDATE MEMBER SET NICKNAME=?, EMAIL=?, TEL=?, SNS=?, PROFILE=? WHERE ID = ?";
		try {
			conn = Common.getConnection();
			pstmt = conn.prepareStatement(sqlNickUdate); // 미리 만들어둔 쿼리문 양식에 맞춰 넣음
	    	pstmt.setString(1, nickname);
	    	pstmt.setString(2, email);
	    	pstmt.setString(3, phone);
	    	pstmt.setString(4, sns);
	    	pstmt.setString(5, profile);
	    	pstmt.setString(6, id);

	    	pstmt.executeUpdate();
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
		Common.close(rs);
		Common.close(pstmt);
	    Common.close(conn);
	}
	
	public int userDo(String id) {
		int checkPlan = 0, allPlan = 0;
		
		try {
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			String sqlPlan = "SELECT COUNT(*) FROM PLAN WHERE ID = '" + id + "'";
			rs = stmt.executeQuery(sqlPlan);
			
			while(rs.next()) { // 읽은 데이타가 있으면 true
				allPlan = rs.getInt("COUNT(*)");
			}
			
			Common.close(rs);
			Common.close(pstmt);
		    Common.close(conn);
				//-------------------------------------------------------------------------------------
		    conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
		    String sqlCheckPlan = "SELECT COUNT(*) FROM PLAN WHERE ID = '" + id + "' AND PLAN_CHECK = 1";
		    rs = stmt.executeQuery(sqlCheckPlan);
		    
		    while(rs.next()) { // 읽은 데이타가 있으면 true
				checkPlan = rs.getInt("COUNT(*)");
		    }
		    
		    Common.close(rs);
			Common.close(pstmt);
		    Common.close(conn);
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (int)Math.floor(checkPlan*100/allPlan);
		

	}
}
