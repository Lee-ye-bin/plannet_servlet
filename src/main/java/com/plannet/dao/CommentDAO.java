package com.plannet.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.plannet.common.Common;
import com.plannet.vo.CommentVO;

public class CommentDAO {
	private Connection conn = null;
	private Statement stmt = null; //표준 SQL문을 수행하기 위한 Statement 객체 얻기
	private ResultSet rs = null; // Statement의 수행 결과를 여러행으로 받음
	// SQL문을 미리 컴파일해서 재 사용하므로 Statement 인터페이스보다 훨씬 빨르게 데이터베이스 작업을 수행
	private PreparedStatement pstmt = null; 
	
	public void boardComment(int bnum, String id, String detail) {
		try {
			String nickname = "";
			//닉네임가져오기
			conn = Common.getConnection();
			stmt = conn.createStatement();
			String sqlNickname = "SELECT * FROM MEMBER WHERE ID ='" + id + "'";
			rs = stmt.executeQuery(sqlNickname);
			
			while(rs.next()) {nickname = rs.getString("NICKNAME");}
				Common.close(rs);
				Common.close(pstmt);
			    Common.close(conn);
			
			
			String sql = "INSERT INTO COMMENTS VALUES (?, COMMENT_NO_SEQ.NEXTVAL, ?, ?, sysdate, ?)";
			
			conn = Common.getConnection();
			pstmt = conn.prepareStatement(sql); // 미리 만들어둔 쿼리문 양식에 맞춰 넣음
			pstmt.setInt(1, bnum);
	    	pstmt.setString(2, id);
	    	pstmt.setString(3, nickname);
	    	pstmt.setString(4, detail);
	    	pstmt.executeUpdate();	
		} catch (Exception e) {
			e.printStackTrace();
		}
		Common.close(rs);
		Common.close(pstmt);
		Common.close(conn);
	}
	
	public List<CommentVO> boardCommetLoad(int num) {
		List<CommentVO> list = new ArrayList<>();
		
		try {
			conn = Common.getConnection();
			stmt = conn.createStatement();
			String sql = "SELECT * FROM COMMENTS WHERE BOARD_NO =" + num + "ORDER BY COMMENT_NO";
			rs = stmt.executeQuery(sql);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			while(rs.next()) {
				CommentVO vo = new CommentVO();
				int sqlNo = rs.getInt("COMMENT_NO");
				String sqlId = rs.getString("ID");
				String sqlNickname = rs.getString("NICKNAME");
				Date sqlDate = rs.getDate("WRITE_DATE");
				String sdfStr = sdf.format(sqlDate);
				String sqlDetail = rs.getString("DETAIL");
				
				vo.setnum(sqlNo);
				vo.setId(sqlId);
				vo.setNickname(sqlNickname);
				vo.setDate(sdfStr);
				vo.setDetail(sqlDetail);
				list.add(vo);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		Common.close(rs);
		Common.close(pstmt);
	    Common.close(conn);
		return list;
	}
}
