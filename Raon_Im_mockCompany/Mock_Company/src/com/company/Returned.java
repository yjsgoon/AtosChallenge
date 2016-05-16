package com.company;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Yoo on 2016-03-17.
 */
@WebServlet("/Returned")
public class Returned extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO
        // anything to do by company.
        String userID = request.getParameter("userID");
        String res = request.getParameter("res");
        String data ="";
        switch(res) {
            case "accept":
                System.out.println(userID + " accepted");
                JSONObject jsonObject;
                JSONParser jsonParser = new JSONParser();
                try {
                    jsonObject = (JSONObject) jsonParser.parse(request.getParameter("requestData"));
                    data = jsonObject.toString();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "deny":
                System.out.println(userID + " denied");
                break;
            case "timer":
                System.out.println(userID + " timer expired");
                break;
            default:
        }
        ResponseManager.getInstance().addResponse(new Response(userID, res, data));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
