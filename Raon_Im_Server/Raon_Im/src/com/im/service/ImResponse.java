package com.im.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.im.db.service.CompanyService;
import com.im.db.service.TimerService;
import com.im.db.service.WaitingService;
import com.im.vo.CompanyVO;
import com.im.vo.TimerVO;
import com.im.vo.WaitingVO;

/**
 * Servlet implementation class AppResponse
 * @since 	2016. 3. 12.
 * @version 1.0	
 * @author 	Yoon JiSoo
 */
@WebServlet("/imresponse")
public class ImResponse extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Calendar cal = Calendar.getInstance();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private Date expirationDate;
	private CompanyService companyService = new CompanyService();
	private CompanyVO companyVO = new CompanyVO();
	private TimerService timerService = new TimerService();
	private TimerVO timerVO = new TimerVO();
	private WaitingService waitingService = new WaitingService();
	private WaitingVO waitingVO = new WaitingVO();
	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ImResponse() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * set the timer and send the data depending on the result(accept/deny) 
	 * @Method	doPost
	 * @param	request		the result, user's ID, personal data
	 * @param	response	success or not
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		
		PrintWriter out = response.getWriter();
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = new JSONObject();
		
		JSONObject resObject = new JSONObject();

		
		try {
			companyVO = companyService.searchUrl(request.getParameter("alias"));
			HttpPoster httpPoster = new HttpPoster(companyVO.getCompanyUrl());
			
			/* if the user accepts, set the timer and send the user's personal data to the company
			 * if the user denies, send the result only */
			if(request.getParameter("res").equals("accept")) {
				jsonObject = (JSONObject)jsonParser.parse(request.getParameter("personalData"));

				timerVO.setUserID(request.getParameter("userID"));
				timerVO.setCompanyUrl(companyVO.getCompanyUrl());
				
				/* initial timer is set to a month */
				cal.add(Calendar.MONTH, +1);
				String temp = sdf.format(cal.getTime());
				expirationDate = new Date(sdf.parse(temp).getTime());
				timerVO.setTimer(expirationDate);
				
				cal.add(Calendar.MONTH, -1);
				
				waitingVO.setUserID(request.getParameter("userID"));
				waitingVO.setCompanyUrl(companyVO.getCompanyUrl());
				
				if(timerService.add(timerVO) && waitingService.remove(waitingVO.getUserID(), waitingVO.getCompanyUrl())) {
					resObject.put("code", 1);
					resObject.put("msg", "success");
				} else {
					resObject.put("code", 0);
					resObject.put("msg", "fail");
				}
			} else {
				jsonObject = null;
				
				waitingVO.setUserID(request.getParameter("userID"));
				waitingVO.setCompanyUrl(companyVO.getCompanyUrl());
				
				if(waitingService.remove(waitingVO.getUserID(), waitingVO.getCompanyUrl())) {
					resObject.put("code", 1);
					resObject.put("msg", "success");
				} else {
					resObject.put("code", 0);
					resObject.put("msg", "fail");
				}
			}
			
			httpPoster.post(request.getParameter("res"), request.getParameter("userID"), jsonObject);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			out.print(resObject.toString());
			out.flush();
			out.close();
		}
	}
}
