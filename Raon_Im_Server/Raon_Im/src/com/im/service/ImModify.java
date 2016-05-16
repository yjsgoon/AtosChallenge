package com.im.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
 * Servlet implementation class ImModify
 * @since 	2016. 3. 23.
 * @version 1.0	
 * @author 	Yoon JiSoo
 */
@WebServlet("/immodify")
public class ImModify extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TimerService timerService = new TimerService();
	private List<TimerVO> listTimerVO = new ArrayList<TimerVO>();
	private CompanyService companyService = new CompanyService();
	private CompanyVO companyVO = new CompanyVO();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ImModify() {
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
	 * if the user updates the personal data, the companies which interacts with the user gets the updated data
	 * @Method 	doPost
	 * @param 	request		stores the user's ID and the personal data	
	 * @param 	response	the value which represents success or not
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		
		PrintWriter out = response.getWriter();
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = new JSONObject();
		
		JSONObject resObject = new JSONObject();
	
		HttpPoster httpPoster;
		
		try {
			jsonObject = (JSONObject) jsonParser.parse(request.getParameter("modifyData"));
			
			if(timerService.searchList(request.getParameter("userID")) != null) {
				listTimerVO = timerService.searchList(request.getParameter("userID"));

				for(TimerVO vo : listTimerVO) {
					companyVO = companyService.search(vo.getCompanyUrl());
					
					httpPoster = new HttpPoster(companyVO.getCompanyUrl());
					String[] reqData = new String(companyVO.getRequestData()).split("/");
					
					JSONObject temp = new JSONObject();
					for(String str : reqData)
						temp.put(str, jsonObject.get(str));
					
					/* send the updated data to the company */
					httpPoster.post("modify", request.getParameter("userID") , temp);					
				}
				
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
