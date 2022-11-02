package com.plannet.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.plannet.common.Common;
import com.plannet.vo.MemberVO;

public class MemberDAO {
	private Connection conn = null;
	private Statement stmt = null; //표준 SQL문을 수행하기 위한 Statement 객체 얻기
	private ResultSet rs = null; // Statement의 수행 결과를 여러행으로 받음
	// SQL문을 미리 컴파일해서 재 사용하므로 Statement 인터페이스보다 훨씬 빨르게 데이터베이스 작업을 수행
	private PreparedStatement pstmt = null; 
	
	public boolean logingCheck(String id, String pwd) {
		try {
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			String sql = "SELECT * FROM MEMBER WHERE ID = " + "'" + id + "'";
			rs = stmt.executeQuery(sql);
			while(rs.next()) { // 읽은 데이타가 있으면 true
				String sqlId = rs.getString("ID"); // 쿼리문 수행 결과에서 ID값을 가져옴
				String sqlPwd = rs.getString("PWD"); // 쿼리문 수행 결과에서 PWD값을 가져옴
				System.out.println("ID : " + sqlId);
				System.out.println("PWD : " + sqlPwd);
				if(id.equals(sqlId) && pwd.equals(sqlPwd)) {
					Common.close(rs);
					Common.close(stmt);
					Common.close(conn);
					return true;
				}
			}
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public List<MemberVO> memberSelect() {
		List<MemberVO> list = new ArrayList<>();
		try {
			conn = Common.getConnection();
			stmt = conn.createStatement();
			String sql = "SELECT * FROM MEMBER";
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String id = rs.getString("ID");
				String pwd = rs.getString("PWD");
				String name = rs.getString("NAME");
				String nickname = rs.getString("NICKNAME");
				String email = rs.getString("EMAIL");
				String tel = rs.getString("TEL");
				//Date birth = rs.getDate("BIRTH");
				Date join = rs.getDate("JOIN_DATE");
				
				MemberVO vo = new MemberVO();
				vo.setId(id);
				vo.setPwd(pwd);
				vo.setName(name);
				vo.setNickname(nickname);
				vo.setEmail(email);
				vo.setTel(tel);
				//vo.setBirth(birth);
				vo.setJoin(join);
				list.add(vo);
			}
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	public boolean regUniCheck(String uni, String type) { // 가입되지 '않은' 경우만 진행되어야 함 
		boolean isNotReg = false;
		try {
			String sql = "";
			conn = Common.getConnection();
			stmt = conn.createStatement();
			char t = type.charAt(5);
			switch (t) {
				case 'I' : 
					sql = "SELECT * FROM MEMBER WHERE ID = '"+ uni +"'";
					break;
				case 'E' : 
					sql = "SELECT * FROM MEMBER WHERE EMAIL = '"+ uni +"'";
					break;
				case 'T' : 
					sql = "SELECT * FROM MEMBER WHERE TEL = '"+ uni +"'";
					break;
			}
			
			rs = stmt.executeQuery(sql);
			if(rs.next()) isNotReg = false;
			else isNotReg = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Common.close(rs); // close 한 뒤에 return 해야 함 
		Common.close(stmt);
		Common.close(conn);
		return isNotReg; // 가입 되어 있으면 false, 가입이 안 되어 있으면 true 
	}

	public boolean memberRegister(String id, String pwd, String name, String nickname, String email, String tel) {
		int result = 0;
		String sql = "INSERT INTO MEMBER(ID, PWD, NAME, NICKNAME, EMAIL, TEL) VALUES(?, ?, ?, ?, ?, ?);";
		try {
			conn = Common.getConnection();
	    	pstmt = conn.prepareStatement(sql); // 미리 만들어둔 쿼리문 양식에 맞춰 넣음
	    	pstmt.setString(1, id);
	    	pstmt.setString(2, pwd);
	    	pstmt.setString(3, name);
	    	pstmt.setString(4, nickname);
	    	pstmt.setString(5, email);
	    	pstmt.setString(6, tel);
	    	//pstmt.setDate(7,  birth);
	    	result = pstmt.executeUpdate();
	    	System.out.println("회원 가입 DB 결과 확인: " + result); // 1이면 성공
		} catch (Exception e) {
			e.printStackTrace();
		}
		Common.close(rs);
		Common.close(pstmt);
	    Common.close(conn);
	    if(result == 1) return true;	
	    else return false;
	}
	
	
	
}
