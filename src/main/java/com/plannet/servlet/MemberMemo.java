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
import com.plannet.dao.MemoDAO;
import com.plannet.vo.MemberVO;

@WebServlet("/MemberMemo")
public class MemberMemo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public MemberMemo() {
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
		
		MemoDAO dao = new MemoDAO();
		List<MemberVO> list = dao.memberMemo(reqId); 
		JSONArray memoArray = new JSONArray();
		for(MemberVO e : list) {
			JSONObject memoText = new JSONObject();
			memoText.put("memo", e.getMemo());
			memoArray.add(memoText);
		}	
		out.print(memoArray);
	}
}