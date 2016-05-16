package com.im.service;

import java.util.Timer;

import javax.servlet.http.HttpServlet;

/**
 * Servlet that executes as the service starts
 * @since 	2016. 1. 30.
 * @version	1.0
 * @author 	Yoo MinJeong
 */
public class Init extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	public Init() {
		
		/* create Timer Thread that executes once a day (1 day = 86400sec) */
		Expiration expirationService = new Expiration();
		Timer expirationScheduler = new Timer(true);
		expirationScheduler.scheduleAtFixedRate(expirationService, 1000, 86400000);
	}
}