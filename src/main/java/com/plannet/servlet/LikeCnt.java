package com.plannet.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.plannet.common.Common;
import com.plannet.dao.BoardDAO;
import com.plannet.dao.QuoteDAO;

@WebServlet("/LikeCnt")
public class LikeCnt extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public LikeCnt() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Common.corsResSet(response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	     request.setCharacterEncoding("utf-8");
	     Common.corsResSet(response);
	     StringBuffer sb = Common.reqStringBuff(request);
	     JSONObject jsonObj = Common.getJsonObj(sb);
	     
	     String getId = (String)jsonObj.get("id");
	     String reqNum = (String)jsonObj.get("num");
	     int getNum = Integer.parseInt(reqNum);
	     System.out.println(getId);
	     System.out.println(getNum);
	
	     PrintWriter out = response.getWriter();
	     BoardDAO dao = new BoardDAO();
	     int likeCnt = dao.boardLikeCnt(getId, getNum);
	     JSONObject resJson = new JSONObject();
	     resJson.put("likeCnt", likeCnt);
	     System.out.println(likeCnt);
		 out.print(resJson);
	  }

}
