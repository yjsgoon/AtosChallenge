package com.company;

import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Yoo on 2016-03-15.
 */
@WebServlet("/RegisterWithIm")
public class RegisterWithIm extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("name", "name");
        jSONObject.put("address", "address");
        jSONObject.put("phone", "phone");

        String ImID = request.getParameter("ImID");
        JSONRequester.executePost("http://220.149.236.22:8080/im/companyrequest",
                jSONObject, "http://220.149.236.22:8080/mock/Returned", "alias", ImID);

        response.sendRedirect("/mock/register_complete.html");
    }
}
