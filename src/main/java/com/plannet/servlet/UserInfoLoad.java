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
import com.plannet.dao.UserInfoDAO;
import com.plannet.vo.MemberVO;


@WebServlet("/UserInfoLoad")
public class UserInfoLoad extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public UserInfoLoad() {
        super();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub //
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
		
		System.out.println("id : " + (String)jsonObj.get("id"));
		String reqId = (String)jsonObj.get("id");
		
		PrintWriter out = response.getWriter();
		
		UserInfoDAO dao = new UserInfoDAO();
		List<MemberVO> list = dao.userInfoLoad(reqId); 
		
		JSONArray infoArray = new JSONArray();
		for(MemberVO e : list) {
			JSONObject info = new JSONObject();
			info.put("nickname", e.getNickname());
			info.put("email", e.getEmail());
			info.put("phone", e.getTel());
			info.put("sns", e.getSns());
			info.put("profile", e.getProfile());
			infoArray.add(info);
		}	
		out.print(infoArray);
	}
}
