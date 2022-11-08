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
import com.plannet.dao.UserInfoDAO;

@WebServlet("/UserDo")
public class UserDo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public UserDo() {
        super();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response = Common.corsResSet(response);
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response = Common.corsResSet(response);
		StringBuffer sb = Common.reqStringBuff(request);
		
		JSONObject jsonObj = Common.getJsonObj(sb);
		String reqId = (String)jsonObj.get("id");
		PrintWriter out = response.getWriter();
		
		UserInfoDAO dao = new UserInfoDAO();
		int num = dao.userDo(reqId); 
		
		JSONObject resJson = new JSONObject();
	    resJson.put("pes", num);
		out.print(resJson);
	}
}
