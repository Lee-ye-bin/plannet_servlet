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
import com.plannet.dao.MemberDAO;

@WebServlet("/MemberNewPwd")
public class MemberNewPwd extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Common.corsResSet(response);
	}
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		Common.corsResSet(response);
		StringBuffer sb = Common.reqStringBuff(request);
		
		JSONObject jsonObj = Common.getJsonObj(sb);
		String getId = (String)jsonObj.get("id");
		String getPwd = (String)jsonObj.get("pwd");
		
		MemberDAO dao = new MemberDAO();
		boolean isChange = dao.memberNewPwd(getId, getPwd);
		PrintWriter out = response.getWriter();
		
		JSONObject jsonobj = new JSONObject();
		if(isChange) jsonobj.put("result", "OK");
		else jsonobj.put("result", "NOK");			
		out.print(jsonobj);
	}
}