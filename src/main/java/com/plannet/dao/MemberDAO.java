package com.plannet.dao;

import java.sql.Connection;
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
	
	// 로그인 체크
	public boolean logingCheck(String id, String pwd) {
		try {
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			String sql = "SELECT * FROM MEMBER WHERE ID = " + "'" + id + "'";
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) { // 읽은 데이타가 있으면 true
				String sqlId = rs.getString("ID"); // 쿼리문 수행 결과에서 ID값을 가져옴
				String sqlPwd = rs.getString("PWD"); // 쿼리문 수행 결과에서 PWD값을 가져옴
				
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

	public boolean regUniCheck(String uni, String type) { // 가입되지 '않은' 경우만 진행되어야 함 
		boolean isNotReg = false;
		
		try {
			String sql = "";
			conn = Common.getConnection();
			stmt = conn.createStatement();
			char t = type.charAt(5);
			
			switch (t) {
				case 'I' : 
					sql = "SELECT * FROM MEMBER WHERE ID = '" + uni + "'";
					break;
				case 'E' : 
					sql = "SELECT * FROM MEMBER WHERE EMAIL = '" + uni + "'";
					break;
				case 'T' : 
					sql = "SELECT * FROM MEMBER WHERE TEL = '" + uni + "'";
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
		String sql = "INSERT INTO MEMBER(ID, PWD, NAME, NICKNAME, EMAIL, TEL) VALUES(?, ?, ?, ?, ?, ?)";
		
		try {
			conn = Common.getConnection();
	    	pstmt = conn.prepareStatement(sql); // 미리 만들어둔 쿼리문 양식에 맞춰 넣음
	    	pstmt.setString(1, id);
	    	pstmt.setString(2, pwd);
	    	pstmt.setString(3, name);
	    	pstmt.setString(4, nickname);
	    	pstmt.setString(5, email);
	    	pstmt.setString(6, tel);
	    	result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Common.close(rs);
		Common.close(pstmt);
	    Common.close(conn);
	    if(result == 1) return true;	
	    else return false;
	}
	
	public boolean memberDelete(String id) {
		try {
			// 댓글삭제
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			String sql = "DELETE FROM COMMENTS WHERE ID = '" + id + "'";
			stmt.executeQuery(sql);
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
			
			// 좋아요삭제
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			sql = "DELETE FROM LIKE_CNT WHERE ID = '" + id + "'";
			stmt.executeQuery(sql);
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
			
			// 좋아요삭제
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			sql = "DELETE FROM LIKE_CNT WHERE ID = '" + id + "'";
			stmt.executeQuery(sql);
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
			
			// 해당하는 회원이 작성한 보드 삭제
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			sql = "DELETE FROM BOARD WHERE ID = '" + id + "'";
			stmt.executeQuery(sql);
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
			
			// 다이어리 삭제
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			sql = "DELETE FROM DIARY WHERE ID = '" + id + "'";
			stmt.executeQuery(sql);
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
			
			// 플랜삭제
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			sql = "DELETE FROM PLAN WHERE ID = '" + id + "'";
			stmt.executeQuery(sql);
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
			
			// 다이어리삭제
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			sql = "DELETE FROM MEMBER WHERE ID = '" + id + "'";
			stmt.executeQuery(sql);
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
			
			// 회원삭제
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			sql = "DELETE FROM MEMBER WHERE ID = '" + id + "'";
			stmt.executeQuery(sql);
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	// 아이디 & 비밀번호 찾기
	public List<MemberVO> memberFindCheck(String uni, String email, String type) { 
		List<MemberVO> list = new ArrayList<>();
		try {
			MemberVO vo = new MemberVO();
			String sql = "";
			conn = Common.getConnection();
			stmt = conn.createStatement();
			char t = type.charAt(5);
			
			switch (t) {
				case 'I' : 
					sql = "SELECT * FROM MEMBER WHERE NAME = '" + uni + "' AND EMAIL = '" + email + "'";
					rs = stmt.executeQuery(sql);
					if(rs.next()) {
						vo.setReg(true);
						vo.setId(rs.getString("ID"));
					} else vo.setReg(false);
					list.add(vo);
					break;
				case 'P' : 
					sql = "SELECT * FROM MEMBER WHERE ID = '" + uni + "' AND EMAIL = '" + email + "'";
					rs = stmt.executeQuery(sql);
					if(rs.next()) vo.setReg(true);
					else vo.setReg(false);
					list.add(vo);
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Common.close(rs); // close 한 뒤에 return 해야 함 
		Common.close(stmt);
		Common.close(conn);
		return list; // 가입 되어 있으면 true, 가입이 안 되어 있으면 false 
	}
	
	// 비밀번호 찾기 시 새 비밀번호 설정
	public boolean memberNewPwd(String id, String pwd) {
		int result = 0;
		
		try {
			String sql = "UPDATE MEMBER SET PWD = ? WHERE ID = ?";
			conn = Common.getConnection();
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, pwd);
	    	pstmt.setString(2, id);
	    	result = pstmt.executeUpdate();
	    	
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(result == 1) return true;
		else return false;
	}
}
