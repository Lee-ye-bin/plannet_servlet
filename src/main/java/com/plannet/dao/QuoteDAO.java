package com.plannet.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.plannet.common.Common;
import com.plannet.vo.MemoVO;
import com.plannet.vo.QuoteVO;

public class QuoteDAO {
	private Connection conn = null;
	private Statement stmt = null; //표준 SQL문을 수행하기 위한 Statement 객체 얻기
	private ResultSet rs = null; // Statement의 수행 결과를 여러행으로 받음
	// SQL문을 미리 컴파일해서 재 사용하므로 Statement 인터페이스보다 훨씬 빨르게 데이터베이스 작업을 수행
	private PreparedStatement pstmt = null; 
	
	public String quoteRandom(String num) {
		String sqlQuote = "";
		
		try {
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기			
			String sql = "SELECT * FROM QUOTE WHERE QUOTE_NO = " + "'" + num + "'";
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) { // 읽은 데이타가 있으면 true
				sqlQuote = rs.getString("QUOTE"); // 쿼리문 수행 결과에서 QUOTE 값을 가져옴
				System.out.println("QUOTE : " + sqlQuote);
			}
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return sqlQuote;
	}
}
//
