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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.im.db.service.CompanyService;
import com.im.db.service.TimerService;
import com.im.vo.CompanyVO;
import com.im.vo.TimerVO;

/**
 * Servlet implementation class ImRequest
 * @since 	2016. 3. 21.
 * @version	1.0
 * @author 	Yoon JiSoo
 */
@WebServlet("/imcompanylist")
public class ImCompanyList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TimerService timerService = new TimerService();
	private List<TimerVO> listTimerVO = new ArrayList<TimerVO>();
	private TimerVO timerVO = new TimerVO();
	private CompanyService companyService = new CompanyService();
	private CompanyVO companyVO = new CompanyVO();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ImCompanyList() {
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
	 * send the list of the companies the user is offering his data to user
	 * @Method	doPost
	 * @param	request		user ID
	 * @param	response	the list of the company and whether the servie is successful or not 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");

		PrintWriter out = response.getWriter();

		timerVO.setUserID(request.getParameter("userID"));

		JSONObject resObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		try {
			if(!timerService.searchList(timerVO.getUserID()).isEmpty()) {
				listTimerVO = timerService.searchList(timerVO.getUserID());

				for(TimerVO vo : listTimerVO) {
					companyVO = companyService.search(vo.getCompanyUrl());

					JSONObject temp = new JSONObject();
					temp.put("alias", companyVO.getAlias());
					temp.put("requestData", companyVO.getRequestData());
					temp.put("timer", vo.getTimer());
					jsonArray.add(temp);
				}
				resObject.put("code", 1);
				resObject.put("msg", "success");
				resObject.put("res", jsonArray);
			} else {
				resObject.put("code", 0);
				resObject.put("msg", "fail");
				resObject.put("res", null);
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
