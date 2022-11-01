package com.plannet.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.plannet.common.Common;
import com.plannet.dao.MemberDAO;
import com.plannet.dao.MemoDAO;
import com.plannet.dao.QuoteDAO;
import com.plannet.vo.MemoVO;
import com.plannet.vo.QuoteVO;


@WebServlet("/QuoteRandom")
public class QuoteRandom extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public QuoteRandom() {
        super();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
		    throws ServletException, IOException {
		response = Common.corsResSet(response);
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response = Common.corsResSet(response);

		StringBuffer sb = Common.reqStringBuff(request);
		JSONObject jsonObj = Common.getJsonObj(sb);
		
		String reqNum = (String)jsonObj.get("num");
		System.out.print("랜덤 수 : " + reqNum);
		
		PrintWriter out = response.getWriter();
		
		QuoteDAO dao = new QuoteDAO();
		String quote = dao.quoteRandom(reqNum); 
		
		JSONObject resJson = new JSONObject();
	    resJson.put("quote", quote);
		out.print(resJson);
	}
}
