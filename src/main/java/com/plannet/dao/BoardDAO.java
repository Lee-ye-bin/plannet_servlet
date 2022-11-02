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


public class BoardDAO {
	private Connection conn = null;
	private Statement stmt = null; //표준 SQL문을 수행하기 위한 Statement 객체 얻기
	private ResultSet rs = null; // Statement의 수행 결과를 여러행으로 받음
	// SQL문을 미리 컴파일해서 재 사용하므로 Statement 인터페이스보다 훨씬 빨르게 데이터베이스 작업을 수행
	private PreparedStatement pstmt = null; 
	
	public List<BoardVO> board(int no){
		List<BoardVO> list = new ArrayList<>();
		
		try {
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			String sql = "SELECT * FROM BOARD WHERE BOARD_NO = " + "'" + no + "'";
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) { // 읽은 데이타가 있으면 true
				
				int sqlNo = rs.getInt("BOARD_NO");
				String sqlId = rs.getString("ID");
				String sqlTitle = rs.getString("TITLE");
				String sqlDetail = rs.getString("DITAIL");
				Date sqlDate = rs.getDate("Date");
				
				System.out.println("BOARD_NO : " + sqlNo);
				BoardVO vo = new BoardVO();
				
				vo.setBoard_no(sqlNo);
				vo.setId(sqlId);
				vo.setTitle(sqlTitle);
				vo.setDetail(sqlDetail);
				vo.setDate(sqlDate);
				list.add(vo);
			}
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
