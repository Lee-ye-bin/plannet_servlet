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
import com.plannet.vo.MemberVO;

@WebServlet("/MemberFind")
public class MemberFind extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Common.corsResSet(response);
	}
	
	@SuppressWarnings("unchecked")//
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		Common.corsResSet(response);
		StringBuffer sb = Common.reqStringBuff(request);
		
		JSONObject jsonObj = Common.getJsonObj(sb);
		String getUni = (String)jsonObj.get("uni");
		String getEmail = (String)jsonObj.get("email");
		String getType = (String)jsonObj.get("type");
		
		MemberDAO dao = new MemberDAO();
		List<MemberVO> list = dao.memberFindCheck(getUni, getEmail, getType);
		PrintWriter out = response.getWriter();
		
		JSONArray findUni = new JSONArray();
		for(MemberVO e : list) {
			JSONObject jsonobj = new JSONObject();
			if(e.isReg() == true) jsonobj.put("result", "OK");
			else jsonobj.put("result", "NOK");
			if(getType.equals("Type_ID")) jsonobj.put("id", e.getId());
			findUni.add(jsonobj);
		}	
		out.print(findUni);
	}
}