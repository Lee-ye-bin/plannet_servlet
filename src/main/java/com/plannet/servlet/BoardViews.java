package com.plannet.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.plannet.common.Common;
import com.plannet.dao.BoardDAO;


@WebServlet("/BoardViews")
public class BoardViews extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public BoardViews() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Common.corsResSet(response);
	}
	// @SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
	    Common.corsResSet(response);
	    StringBuffer sb = Common.reqStringBuff(request);
	    JSONObject jsonObj = Common.getJsonObj(sb);
	    String reqNum = (String)jsonObj.get("num");
		int inNum = Integer.parseInt(reqNum);
		System.out.println("전달 받은 num : " + reqNum);
		BoardDAO dao = new BoardDAO();
		dao.boardViews(inNum);
	}
}
