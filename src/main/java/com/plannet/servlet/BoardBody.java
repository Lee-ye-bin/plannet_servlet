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


@WebServlet("/BoardBody")
public class BoardBody extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public BoardBody() {
        super();
        
    }
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Common.corsResSet(response);
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		Common.corsResSet(response);
		
		BoardDAO dao = new BoardDAO();
		List<BoardVO> list = dao.board();
		
		PrintWriter out = response.getWriter();
		JSONArray boardBodyArray = new JSONArray();
		
		for(BoardVO e : list) {
			JSONObject boardMain = new JSONObject();
			boardMain.put("num", e.getNum());
			boardMain.put("id", e.getId());
			boardMain.put("title", e.getTitle());
			boardMain.put("nickname", e.getNickname());
			boardMain.put("views", e.getViews());
			boardMain.put("date", e.getDate());
			boardMain.put("detail", e.getDetail());
			boardBodyArray.add(boardMain);
		}	
		out.print(boardBodyArray);
	}

}
