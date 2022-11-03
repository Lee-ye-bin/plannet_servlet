//
package com.plannet.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
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

@WebServlet("/BoardList")
public class BoardList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BoardList() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Common.corsResSet(response);
	}
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		Common.corsResSet(response);
		
		BoardDAO dao = new BoardDAO();
		List<BoardVO> list = dao.board();
		
		PrintWriter out = response.getWriter();
		JSONArray boardArray = new JSONArray();
		for(BoardVO e : list) {
			JSONObject boardText = new JSONObject();
			boardText.put("num", e.getNum());
			boardText.put("id", e.getId());
			boardText.put("title", e.getTitle());
			boardText.put("nickname", e.getNickname());
			boardText.put("views", e.getViews());
			boardText.put("date", e.getDate());
			boardText.put("detail", e.getDetail());
			boardArray.add(boardText);
		}	
		out.print(boardArray);
	}

}
