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

@WebServlet("/MemberCheck")
public class MemberCheck extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
		    throws ServletException, IOException {
		Common.corsResSet(response);
	}
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		Common.corsResSet(response);

		StringBuffer sb = Common.reqStringBuff(request);
		JSONObject jsonObj = Common.getJsonObj(sb);
		String getId = (String)jsonObj.get("id");
		System.out.println(sb); // 테스트용 id 출력 
		
		MemberDAO dao = new MemberDAO();
		boolean isNotReg = dao.regIdCheck(getId); // isNotReg = TRUE 이면 가입이 안 된 경우를 나타냄
		System.out.println("DB 조회 결과값 : " + isNotReg);
		
		PrintWriter out = response.getWriter();
		JSONObject resJson = new JSONObject();
		if(isNotReg) resJson.put("result", "OK");
		else resJson.put("result", "NOK");
		out.print(resJson);
	}
}



