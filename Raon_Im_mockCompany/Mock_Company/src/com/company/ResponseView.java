package com.company;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Yoo on 2016-04-21.
 */
@WebServlet("/ResponseView")
public class ResponseView extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        ResponseManager responseManager = ResponseManager.getInstance();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Response View</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<table border=1>");
        out.println("<tr>");
        out.println("<th>UserID</th>");
        out.println("<th>Response</th>");
        out.println("<th>Data</th>");
        out.println("<tr>");


        for (Response oneResponse : responseManager.getResponses()) {
            out.println("<tr>");
            out.println("<td>" + oneResponse.getUserID() + "</td>");
            out.println("<td>" + oneResponse.getRes() + "</td>");
            out.println("<td>" + oneResponse.getData() + "</td>");
            out.println("</tr>");
        }
        out.println( "</table>");
        out.println("</body>");
        out.println("</html>");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }
}
