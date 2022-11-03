package com.plannet.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
	    String reqNo = (String)jsonObj.get("board_no");
	    int reNo = Integer.parseInt(reqNo);
	    PrintWriter out = response.getWriter();
	    
	    BoardDAO dao = new BoardDAO();
	    List<BoardVO> list = dao.board(reNo);
	    
	    JSONArray boardArray = new JSONArray();
	    for(BoardVO e : list) {
	    	JSONObject boardText = new JSONObject();
	    	boardText.put("title",e.getTitle());
	    	System.out.println(e.getTitle());
	    	boardText.put("id",e.getId());
	    	boardText.put("view", e.getView());
	    	DateFormat dateFormat = new SimpleDateFormat("YY/MM/dd");
	    	String dateTostr = dateFormat.format(e.getDate());
	    	boardText.put("write_date",dateTostr);
	    	boardArray.add(boardText);
	    }
	    out.print(boardArray);
	}
	
}
