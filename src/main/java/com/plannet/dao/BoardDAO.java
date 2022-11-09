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
	
	public List<BoardVO> boardList(){
		List<BoardVO> list = new ArrayList<>();
		
		try {
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			String sql = "SELECT * FROM BOARD ORDER BY BOARD_NO DESC";
			rs = stmt.executeQuery(sql);	
			
			while(rs.next()) { // 읽은 데이터가 있으면 true
				BoardVO vo = new BoardVO();
				int sqlNo = rs.getInt("BOARD_NO");
				String sqlId = rs.getString("ID");
				String sqlTitle = rs.getString("TITLE");
				String sqlNickname = rs.getString("NICKNAME");
				int sqlViews=rs.getInt("VIEWS");
				String sqlDate = rs.getString("WRITE_DATE");
		
				vo.setNum(sqlNo);
				vo.setId(sqlId);
				vo.setTitle(sqlTitle);
				vo.setNickname(sqlNickname);
				vo.setViews(sqlViews);
				vo.setDate(sqlDate);
	
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
	
	public void boardCreate(String id, String title, String detail, boolean isChecked) {
		String sql = "INSERT INTO BOARD (BOARD_NO, ID, TITLE, NICKNAME, DETAIL, ISCHECKED) VALUES (BOARD_NO_SEQ.NEXTVAL, ?, ?, ?, ?, ?)";
		
		try {
			String nickname = "";
			int boardNo = 0;
			
			if (!isChecked) {
				conn = Common.getConnection();
				stmt = conn.createStatement(); // 미리 만들어둔 쿼리문 양식에 맞춰 넣음
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
	    	pstmt.setString(3, nickname);
	    	pstmt.setString(4, "DETAIL");
	    	
	    	if (isChecked) pstmt.setInt(5, 1);
	    	else pstmt.setInt(5, 0);
	    	
	    	pstmt.executeUpdate();
	    	Common.close(rs);
			Common.close(pstmt);
		    Common.close(conn);
		    
		    // BOARD_NO 구하기
		    conn = Common.getConnection();
			stmt = conn.createStatement();// 미리 만들어둔 쿼리문 양식에 맞춰 넣음
			String numSql = "SELECT MAX(BOARD_NO) FROM BOARD";
			rs = stmt.executeQuery(numSql);
			
			while(rs.next()) boardNo = rs.getInt("MAX(BOARD_NO)");
	    	Common.close(rs);
			Common.close(pstmt);
		    Common.close(conn);
		    
		    // 디테일 업데이트 구문
		    conn = Common.getConnection();
			stmt = conn.createStatement();
			String detailSql = ""; 
			
			// 4000바이트가 넘으면
	    	if(detail.getBytes("UTF-8").length > 4000) {
	    		StringBuffer updateSql = new StringBuffer("");
	    		// 업데이트 구문
	    		System.out.println(detail.length());
	    		int cnt = ((detail.getBytes("UTF-8").length)/3800) + 1; // 몇번 ||을 해야하는지
	    		int beginIdx = 0, maxByte = 3800; // 시작인덱스, 자르는 기준 바이트 수
	    		
	    		int slen = 0, blen = 0; // str의 length(endIdx로 사용), byte기준 length
	    		char c; // 잘리는 마지막 글자
	    		
	    		for(int i = 0; i < cnt; i++) { 
	    			while(blen + 1 < maxByte - 1 && slen < detail.length() - 1) { //합한 바이트의 수가 maxbyte를 넘지 않을때까지
	    				c = detail.charAt(slen);
	    				slen++;
	    				if(c > 127) blen += 3; //아스키코드 기준으로 1바이트 이상의 문자가 들어올경우
	    				else blen++;
	    			} // while 끝
	    			
	    			if(i == 0) updateSql.append("TO_CLOB('" + detail.substring(beginIdx, slen) + "')");
	    			else updateSql.append("|| TO_CLOB('" + detail.substring(beginIdx, slen) + "')");
	    			beginIdx = slen;
	    			maxByte += blen;
	    		}	    		
	    		detailSql = "UPDATE BOARD SET DETAIL = " + updateSql + "WHERE BOARD_NO =" + boardNo;
	    	} else {
	    		detailSql = "UPDATE BOARD SET DETAIL = TO_CLOB('" + detail + "') WHERE BOARD_NO =" + boardNo;
	    	}
	    	stmt.executeUpdate(detailSql);
	    	Common.close(rs);
			Common.close(stmt);
		    Common.close(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Common.close(rs);
		Common.close(pstmt);
	    Common.close(conn);
	}

	public List<BoardVO> boardLoad(int num) {
		List<BoardVO> list = new ArrayList<>();
		
		try {
			BoardVO vo = new BoardVO();
			
			// 디테일 외 전부 불러오기
			conn = Common.getConnection();
			stmt = conn.createStatement();
			String sql = "SELECT * FROM BOARD WHERE BOARD_NO =" + num;
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				int sqlNo = rs.getInt("BOARD_NO");
				String sqlId = rs.getString("ID");
				String sqlTitle = rs.getString("TITLE");
				String sqlNickname = rs.getString("NICKNAME");
				int sqlViews=rs.getInt("VIEWS");
				String sqlDate = rs.getString("WRITE_DATE");
				String sqlDetail = rs.getString("DETAIL");
				boolean sqlChecked = rs.getBoolean("ISCHECKED");
				
				vo.setNum(sqlNo);
				vo.setId(sqlId);
				vo.setTitle(sqlTitle);
				vo.setNickname(sqlNickname);
				vo.setViews(sqlViews);
				vo.setDate(sqlDate);
				vo.setDetail(sqlDetail);
				vo.setChecked(sqlChecked);
			}
		    list.add(vo);
		} catch(Exception e) {
			e.printStackTrace();
		}
		Common.close(rs);
		Common.close(pstmt);
	    Common.close(conn);
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
	
	// 작성한 게시물 수정
	public void boardEdit(String id, int num, String title, String detail) {
		String sql = "UPDATE BOARD SET TITLE = ? WHERE BOARD_NO = ?";
		
		try {
			conn = Common.getConnection();
			pstmt = conn.prepareStatement(sql); // 미리 만들어둔 쿼리문 양식에 맞춰 넣음
	    	pstmt.setString(1, title);
	    	pstmt.setInt(2, num);
	    	pstmt.executeUpdate();
	    	
	    	Common.close(rs);
			Common.close(pstmt);
			Common.close(conn);
	    	
	    	// 디테일 업데이트 구문
		    conn = Common.getConnection();
			stmt = conn.createStatement();
			String detailSql = ""; 
			
			// 4000바이트가 넘으면
	    	if(detail.getBytes("UTF-8").length > 4000) {
	    		StringBuffer updateSql = new StringBuffer("");
	    		// 업데이트 구문
	    		System.out.println(detail.length());
	    		int cnt = ((detail.getBytes("UTF-8").length)/3800) + 1; // 몇번 ||을 해야하는지
	    		int beginIdx = 0, maxByte = 3800; // 시작인덱스, 자르는 기준 바이트 수
	    		
	    		int slen = 0, blen = 0; // str의 length(endIdx로 사용), byte기준 length
	    		char c; // 잘리는 마지막 글자
	    		
	    		for(int i = 0; i < cnt; i++) { 
	    			while(blen + 1 < maxByte - 1 && slen < detail.length() - 1) { //합한 바이트의 수가 maxbyte를 넘지 않을때까지
	    				c = detail.charAt(slen);
	    				slen++;
	    				if(c > 127) blen += 3; //아스키코드 기준으로 1바이트 이상의 문자가 들어올경우
	    				else blen++;
	    			} // while 끝
	    			
	    			if(i == 0) updateSql.append("TO_CLOB('" + detail.substring(beginIdx, slen) + "')");
	    			else updateSql.append("|| TO_CLOB('" + detail.substring(beginIdx, slen) + "')");
	    			beginIdx = slen;
	    			maxByte += blen;
	    		}	    		
	    		detailSql = "UPDATE BOARD SET DETAIL = " + updateSql + "WHERE BOARD_NO =" + num;
	    	} else {
	    		detailSql = "UPDATE BOARD SET DETAIL = TO_CLOB('" + detail + "') WHERE BOARD_NO =" + num;
	    	}
	    	stmt.executeUpdate(detailSql);
	    	Common.close(rs);
			Common.close(stmt);
		    Common.close(conn);
	    	
	    	
	    	
	    	
	    	
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	// 게시물 조회수
	public void boardViews(int num) {
		String sql = "UPDATE BOARD SET VIEWS=VIEWS+1 WHERE BOARD_NO = " + "'" + num + "'";
		
		try {
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			stmt.executeQuery(sql);
			Common.close(stmt);
			Common.close(conn);
		} catch(Exception e) {
				e.printStackTrace();
		}
	}
	
	// 게시물 좋아요 수
	public int boardLikeCnt(String id, int num) {
		int likeCnt = 0;
		try {
			conn = Common.getConnection();
			stmt = conn.createStatement(); // Statement 객체 얻기
			String sql = "SELECT COUNT(*) FROM LIKE_CNT WHERE BOARD_NO = " + num;
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) { // 읽은 데이타가 있으면 true
				likeCnt = rs.getInt("COUNT(*)");
			}
			Common.close(rs);
			Common.close(stmt);
		    Common.close(conn);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return likeCnt;
	}
	
	// 내가 좋아요한 게시물 표시
	public boolean boardLikeChecked(String id, int num) {
		boolean likeChecked = false;
		int result = 0;
		
		try {
			String sql = "SELECT COUNT(*) FROM LIKE_CNT WHERE BOARD_NO = ? AND id = ?";	
			System.out.println(sql);
			conn = Common.getConnection();
			pstmt = conn.prepareStatement(sql);	 // Statement 객체 얻기
			pstmt.setInt(1, num);
	    	pstmt.setString(2, id);
	    	rs = pstmt.executeQuery();
	    
	    	while(rs.next()) { // 읽은 데이타가 있으면 true
	    		result = rs.getInt("COUNT(*)");
	    		
				if (result == 1) { // 이미 좋아요를 누른 상태이면
					likeChecked = false; // 좋아요 상태 해제
					String deleteSql = "DELETE FROM LIKE_CNT WHERE BOARD_NO = ? AND id = ?"; // SQL 삭제
					pstmt = conn.prepareStatement(deleteSql);			
					pstmt.setInt(1, num);
			    	pstmt.setString(2, id);
			    	pstmt.executeUpdate();
				} else { // 좋아요를 누르지 않은 상태이면
					likeChecked = true; // 좋아요 상태로 전환
					String insertSql = "INSERT INTO LIKE_CNT VALUES (?, ?)"; // SQL 추가
					pstmt = conn.prepareStatement(insertSql);			
					pstmt.setString(1, id);
			    	pstmt.setInt(2, num);
			    	pstmt.executeUpdate();
				}
	    	}
			Common.close(rs);
			Common.close(stmt);
		    Common.close(conn);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return likeChecked;
	}
}
