package com.im.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.im.db.service.CompanyService;
import com.im.db.service.UserService;
import com.im.db.service.WaitingService;
import com.im.vo.CompanyVO;
import com.im.vo.UserVO;
import com.im.vo.WaitingVO;

/**
 * Servlet implementation class CompanyRequest
 * @since 	2016. 3. 11.
 * @version 1.0	
 * @author 	Yoon JiSoo
 */
@WebServlet("/companyrequest")
public class CompanyRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CompanyService companyService = new CompanyService();
	private CompanyVO companyVO = new CompanyVO();
	private UserService userService = new UserService();
	private UserVO userVO = new UserVO();
	private WaitingService waitingService = new WaitingService();
	private WaitingVO waitingVO = new WaitingVO();
	private GCM gcmService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CompanyRequest() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * request of a company that requires a userj's personal data
	 * @Method	doPost
	 * @param	request		stores the users's ID
	 * @param	response	boolean value that represents whethrer SQL query is success or not
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		
		PrintWriter out = response.getWriter();
	
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = new JSONObject();

		JSONObject resObject = new JSONObject();

		try {
			jsonObject = (JSONObject) jsonParser.parse(request.getParameter("requestData"));

			Iterator<String> dataItorator = jsonObject.keySet().iterator();
			
			String info = "";
			while(dataItorator.hasNext()) {
				info += dataItorator.next();
				info += "/";
			}
			
			companyVO.setCompanyUrl(request.getParameter("returnAddress"));
			companyVO.setAlias(request.getParameter("alias"));
			companyVO.setRequestData(info);

			waitingVO.setUserID(request.getParameter("ImID"));
			waitingVO.setCompanyUrl(request.getParameter("returnAddress"));
			
			if(companyService.add(companyVO) && waitingService.add(waitingVO)) {
				resObject.put("code", 1);
				resObject.put("msg",  "success");
			} else {
				resObject.put("code", 0);
				resObject.put("msg",  "fail");
			}
			
			userVO = userService.searchKey(request.getParameter("ImID"));

			/* provide gcm alram service that user got the alram from the company */
			gcmService = new GCM(userVO.getGcmID());
			String gcmMsg = "\"" + request.getParameter("alias") + "\" requested";
			gcmService.push("request", gcmMsg);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			out.print(resObject.toString());
			out.flush();
			out.close();
		}
	}
}
