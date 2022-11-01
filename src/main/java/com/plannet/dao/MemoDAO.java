package com.plannet.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.plannet.common.Common;
import com.plannet.vo.MemberVO;
import com.plannet.vo.MemoVO;

public class MemoDAO {
	private Connection conn = null;
	private Statement stmt = null; //표준 SQL문을 수행하기 위한 Statement 객체 얻기
	private ResultSet rs = null; // Statement의 수행 결과를 여러행으로 받음
	// SQL문을 미리 컴파일해서 재 사용하므로 Statement 인터페이스보다 훨씬 빨르게 데이터베이스 작업을 수행
	private PreparedStatement pstmt = null; 

	
	public  List<MemoVO> memberMemo(String id) {
		List<MemoVO> list = new ArrayList<>();
		
		try {
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			String sql = "SELECT * FROM MEMO WHERE ID = " + "'" + id + "'";
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) { // 읽은 데이타가 있으면 true
				String sqlMemo = rs.getString("MEMO");
				String sqlId = rs.getString("ID");
				System.out.println("MEMO : " + sqlMemo);
				MemoVO vo = new MemoVO();
				vo.setId(sqlId);
				vo.setMemo(sqlMemo);
				list.add(vo);
			}
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void memberMemoSave(String id, String memo) {
		String sql = "UPDATE MEMO SET MEMO = ? WHERE ID = ?";
		try {
			conn = Common.getConnection();
	    	pstmt = conn.prepareStatement(sql); // 미리 만들어둔 쿼리문 양식에 맞춰 넣음
	    	pstmt.setString(1, memo);
	    	pstmt.setString(2, id);
	    	pstmt.executeUpdate();
	    	System.out.println("메모저장");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Common.close(rs);
		Common.close(pstmt);
	    Common.close(conn);
	}

}
