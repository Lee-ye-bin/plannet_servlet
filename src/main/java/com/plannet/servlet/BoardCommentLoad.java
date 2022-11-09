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
import com.plannet.dao.CommentDAO;
import com.plannet.vo.BoardVO;
import com.plannet.vo.CommentVO;

@WebServlet("/BoardCommentLoad")
public class BoardCommentLoad extends HttpServlet {
	private static final long serialVersionUID = 1L;   
    
    public BoardCommentLoad() {

        super();
    }
    
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Common.corsResSet(response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CommentDAO dao = new CommentDAO();
		request.setCharacterEncoding("utf-8");
		Common.corsResSet(response);
		StringBuffer sb = Common.reqStringBuff(request);
	
		JSONObject jsonObj = Common.getJsonObj(sb);
		String reqNum = (String)jsonObj.get("num");
		int inNum = Integer.parseInt(reqNum);
		
		List<CommentVO> list =dao.boardCommetLoad(inNum);
		PrintWriter out = response.getWriter();
		
		JSONArray boardLeadArray = new JSONArray();
		for(CommentVO e : list) {
			JSONObject boardStr = new JSONObject();
			boardStr.put("no", e.getnum());
			boardStr.put("id", e.getId());
			boardStr.put("nickname", e.getNickname());
			boardStr.put("date", e.getDate());
			boardStr.put("detail", e.getDetail());
			boardLeadArray.add(boardStr);
		}
		out.print(boardLeadArray);
	}	
}
