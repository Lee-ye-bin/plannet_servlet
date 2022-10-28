package com.plannet.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.plannet.dao.MemberDAO;
import com.plannet.vo.MemberVO;


@WebServlet("/MemberMemo")
public class MemberMemo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public MemberMemo() {
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
		
		System.out.println("Command : " + (String)jsonObj.get("cmd"));
		String reqCmd = (String)jsonObj.get("cmd");
		
		PrintWriter out = response.getWriter();
		
		if(!reqCmd.equals("MemberInfo")) {
			JSONObject resJson = new JSONObject();
			resJson.put("result", "NOK");
			out.print(resJson);
			return;
		}
		
		MemberDAO dao = new MemberDAO();
		List<MemberVO> list = dao.memberSelect();
		
		JSONArray memberArray = new JSONArray();
		
		
		for(MemberVO e : list) {
			JSONObject memberInfo = new JSONObject();
			memberInfo.put("id", e.getId());
			memberInfo.put("pwd", e.getPwd());
			memberInfo.put("name", e.getName());
			memberInfo.put("email", e.getEmail());
			DateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
			String dateToStr = dateFormat.format(e.getJoin());
			memberInfo.put("join", dateToStr);
			memberArray.add(memberInfo);
		}	
		out.print(memberArray);
	}
}
