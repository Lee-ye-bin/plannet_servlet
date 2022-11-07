package com.plannet.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.plannet.common.Common;
import com.plannet.vo.BoardVO;
import com.plannet.vo.MemberVO;


public class BoardDAO {
	private Connection conn = null;
	private Statement stmt = null; //표준 SQL문을 수행하기 위한 Statement 객체 얻기
	private ResultSet rs = null; // Statement의 수행 결과를 여러행으로 받음
	// SQL문을 미리 컴파일해서 재 사용하므로 Statement 인터페이스보다 훨씬 빨르게 데이터베이스 작업을 수행
	private PreparedStatement pstmt = null; 
	
	public List<BoardVO> board(){
		List<BoardVO> list = new ArrayList<>();
		
		try {
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			String sql = "SELECT * FROM BOARD ORDER BY BOARD_NO DESC";
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) { // 읽은 데이타가 있으면 true
				int sqlNo = rs.getInt("BOARD_NO");
				String sqlId = rs.getString("ID");
				String sqlTitle = rs.getString("TITLE");
				String sqlNickname = rs.getString("NICKNAME");
				int sqlViews=rs.getInt("VIEWS");
				String sqlDate = rs.getString("WRITE_DATE");
				String sqlDetail = rs.getString("DETAIL");
				
				System.out.println("BOARD_NO : " + sqlNo);
				BoardVO vo = new BoardVO();
				vo.setNum(sqlNo);
				vo.setId(sqlId);
				vo.setTitle(sqlTitle);
				vo.setNickname(sqlNickname);
				vo.setViews(sqlViews);
				vo.setDate(sqlDate);
				vo.setDetail(sqlDetail);
				list.add(vo);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		Common.close(rs);
		Common.close(stmt);
		Common.close(conn);
		return list;
	}
	
		
	public void boardCreate(String id, String title, String detail, boolean isChecked) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO BOARD (BOARD_NO, ID, TITLE, NICKNAME, DETAIL) VALUES (BOARD_NO_SEQ.NEXTVAL, ?, ?, ?, ?)";
		//
		try {
			String nickname = "";
			
			if (!isChecked) {
				conn = Common.getConnection();
				stmt = conn.createStatement();// 미리 만들어둔 쿼리문 양식에 맞춰 넣음
				String nicknameSql = "SELECT NICKNAME FROM MEMBER WHERE ID = " + "'" + id + "'";
				rs = stmt.executeQuery(nicknameSql);
				
				while(rs.next()) {
					nickname = rs.getString("NICKNAME");
				}
				
				Common.close(rs);
				Common.close(stmt);
			    Common.close(conn);
		    }
			else nickname = "익명";
			
			conn = Common.getConnection();
	    	pstmt = conn.prepareStatement(sql); // 미리 만들어둔 쿼리문 양식에 맞춰 넣음
	    	pstmt.setString(1, id);
	    	pstmt.setString(2, title);
	    	pstmt.setString(3, nickname);;
	    	pstmt.setString(4, detail);
	    	pstmt.executeUpdate();
	    	System.out.println("글쓰기");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Common.close(rs);
		Common.close(pstmt);
	    Common.close(conn);
	}
	
	public List<BoardVO> boardLead(Integer num) {
		List<BoardVO> list = new ArrayList<>();
		try {
			String sql = "SELECT * FROM BOARD WHERE BOARD_NO ="+"'"+num+"'";
			conn = Common.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				int sqlNo = rs.getInt("BOARD_NO");
				String sqlId = rs.getString("ID");
				String sqlTitle = rs.getString("TITLE");
				String sqlNickname = rs.getString("NICKNAME");
				int sqlViews=rs.getInt("VIEWS");
				String sqlDate = rs.getString("WRITE_DATE");
				String sqlDetail = rs.getString("DETAIL");
				
				System.out.println("BOARD_NO : " + sqlNo);
				BoardVO vo = new BoardVO();
				vo.setNum(sqlNo);
				vo.setId(sqlId);
				vo.setTitle(sqlTitle);
				vo.setNickname(sqlNickname);
				vo.setViews(sqlViews);
				vo.setDate(sqlDate);
				vo.setDetail(sqlDetail);
				list.add(vo);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean boardDelete(int num) {
		try {
			//보드 내용 삭제
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			String sql = "DELETE FROM BOARD WHERE BOARD_NO = " + num;
			stmt.executeQuery(sql);
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
