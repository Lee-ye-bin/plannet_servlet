package com.plannet.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.plannet.common.Common;
import com.plannet.dao.BoardDAO;
import com.plannet.vo.BoardVO;


@WebServlet("/BoardTitleServlet")
public class BoardTitleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public BoardTitleServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
		    throws ServletException, IOException {
		response = Common.corsResSet(response);
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
	    Common.corsResSet(response);
	    StringBuffer sb = Common.reqStringBuff(request);
	    JSONObject jsonObj = Common.getJsonObj(sb);
	    int reqNo = (int)jsonObj.get("board_no");
	    
	    PrintWriter out = response.getWriter();
	    
	    BoardDAO dao = new BoardDAO();
	    List<BoardVO> list = dao.board(reqNo);
	    
	    JSONArray boardArray = new JSONArray();
	    for(BoardVO e : list) {
	    	JSONObject boardText = new JSONObject();
	    	boardText.put("title",e.getTitle());
	    	boardText.put("id",e.getId());
	    	boardText.put("date",e.getDate());
	    }
	    out.print(boardArray);
	}

}
