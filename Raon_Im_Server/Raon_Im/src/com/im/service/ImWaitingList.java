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
import com.im.db.service.WaitingService;
import com.im.vo.CompanyVO;
import com.im.vo.WaitingVO;

/**
 * Servlet implementation class ImWaitingRequest
 * @since 	2016. 3. 29.
 * @version 1.0	
 * @author 	Yoon JiSoo
 */
@WebServlet("/imwaitinglist")
public class ImWaitingList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private WaitingService waitingService = new WaitingService();
	private WaitingVO waitingVO = new WaitingVO();
	private List<WaitingVO> listWaitingVO = new ArrayList<WaitingVO>();
	private CompanyService companyService = new CompanyService();
	private CompanyVO companyVO = new CompanyVO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImWaitingList() {
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
	 * provide the list of the companies that requres the personal data
	 * @Method	doPost
	 * @param	request		user ID
	 * @param	response	the list of the company and the value that represents success or not 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		
		PrintWriter out = response.getWriter();

		JSONObject resObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		
		waitingVO.setUserID(request.getParameter("userID"));
		try {
			if(!waitingService.searchList(waitingVO.getUserID()).isEmpty()) {
				listWaitingVO = waitingService.searchList(waitingVO.getUserID());
				
				for(WaitingVO vo : listWaitingVO) {
					companyVO = companyService.search(vo.getCompanyUrl());
					
					JSONObject temp = new JSONObject();
					temp.put("alias", companyVO.getAlias().toString());
					temp.put("requestData", companyVO.getRequestData().toString());
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
