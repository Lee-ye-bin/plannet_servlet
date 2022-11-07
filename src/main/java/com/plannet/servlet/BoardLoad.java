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
import com.plannet.dao.BoardDAO;
import com.plannet.vo.BoardVO;



@WebServlet("/BoardLoad")
public class BoardLoad extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public BoardLoad() {
        super();
        
    }
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Common.corsResSet(response);
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
//
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		Common.corsResSet(response);
		StringBuffer sb = Common.reqStringBuff(request);
		JSONObject jsonObj = Common.getJsonObj(sb);
		
		String reqNum = (String)jsonObj.get("num");
		int inNum = Integer.parseInt(reqNum);
		System.out.println("전달 받은 num : " + reqNum);
		
		BoardDAO dao = new BoardDAO();
		List<BoardVO> list =dao.boardLead(inNum);
		PrintWriter out = response.getWriter();
		JSONArray boardLeadArray = new JSONArray();
		
		for(BoardVO e : list) {
			JSONObject boardStr = new JSONObject();
			boardStr.put("num", e.getNum());
			boardStr.put("id", e.getId());
			boardStr.put("title", e.getTitle());
			boardStr.put("nickname", e.getNickname());
			boardStr.put("views", e.getViews());
			boardStr.put("date", e.getDate());
			boardStr.put("detail", e.getDetail());
			boardStr.put("isChecked", e.isChecked());
			System.out.print(boardStr);
			boardLeadArray.add(boardStr);
		}
		out.print(boardLeadArray);
	}	
	
}
