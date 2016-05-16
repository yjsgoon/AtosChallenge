package com.im.service;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.im.db.service.UserService;
import com.im.vo.UserVO;

/**
 * Servlet implementation class SignUp
 * @since 	2016. 2. 25.
 * @version	1.0
 * @author 	Yoon JiSoo
 */
@WebServlet("/signup")
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private UserService service = new UserService();
    private UserVO vo = new UserVO();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUp() {
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
	 * stores the user's I'm ID, password, GCM ID to server's Database while signing up on App
	 * @Method	doPost
	 * @param 	request		stores the data while signing up
	 * @param 	response	success or not of service provision
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		
		PrintWriter out = response.getWriter();
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = new JSONObject();
		
		JSONObject resObject = new JSONObject();

		try {
			jsonObject = (JSONObject)jsonParser.parse(request.getParameter("information"));
			
			vo.setUserID(jsonObject.get("userID").toString());
			vo.setUserPW(jsonObject.get("userPW").toString());
			vo.setGcmID(jsonObject.get("gcmID").toString());
			
			if(service.add(vo)) {
				resObject.put("code", 1);
				resObject.put("msg",  "success");
			} else {
				resObject.put("code", -1);
				resObject.put("msg",  "fail");
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
