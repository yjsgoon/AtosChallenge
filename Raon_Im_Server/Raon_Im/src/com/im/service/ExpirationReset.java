package com.im.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.im.db.service.CompanyService;
import com.im.db.service.TimerService;
import com.im.vo.CompanyVO;
import com.im.vo.TimerVO;

/**
 * Servlet implementation class ExpirationResult
 * @since 	2016. 2. 25.
 * @version	1.0
 * @author 	Yoon JiSoo
 */
@WebServlet("/expirationreset")
public class ExpirationReset extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TimerService timerService = new TimerService();
	private TimerVO timerVO = new TimerVO();
	private CompanyService companyService = new CompanyService();
	private CompanyVO companyVO = new CompanyVO();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ExpirationReset() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * modify the timer
	 * @Method doPost
	 * @param request		stores the user's ID, companyUrl, date of the updated timer
	 * @param response		boolean value that represents whethrer SQL query is success or not
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		
		PrintWriter out = response.getWriter();
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = new JSONObject();

		JSONObject resObject = new JSONObject();

		try {
			jsonObject = (JSONObject)jsonParser.parse(request.getParameter("expirationReset"));
			
			String expirationDate = jsonObject.get("year").toString() + "-" +
									jsonObject.get("month").toString() + "-" +
									jsonObject.get("date").toString();
			
			timerVO.setUserID(request.getParameter("userID"));
			companyVO = companyService.searchUrl(request.getParameter("alias"));
			timerVO.setCompanyUrl(companyVO.getCompanyUrl());
			timerVO.setTimer(new Date(sdf.parse(expirationDate).getTime()));
			
			if(timerService.modify(timerVO)) {
				resObject.put("code", 1);
				resObject.put("msg", "success");
			} else {
				resObject.put("code", 0);
				resObject.put("msg", "fail");
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			out.print(resObject.toString());
			out.flush();
			out.close();
		}
	}
}
