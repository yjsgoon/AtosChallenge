package com.im.service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

import javax.swing.plaf.synth.SynthSpinnerUI;

import com.im.db.service.UserService;
import com.im.db.service.CompanyService;
import com.im.db.service.TimerService;
import com.im.vo.UserVO;
import com.im.vo.CompanyVO;
import com.im.vo.TimerVO;

/**
 * Timer Thread implementing timer function
 * @since 	2016. 01. 27.
 * @version 1.0
 * @author 	Yoon JiSoo
 */
public class Expiration extends TimerTask {
	private TimerService timerService = new TimerService();
	private List<TimerVO> timerVO;
	private UserService userService = new UserService();
	private UserVO userVO = new UserVO();
	private CompanyService companyService = new CompanyService();
	private CompanyVO companyVO = new CompanyVO();
	private GCM gcmService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private Calendar cal;
	private Date gcmDate;
	private Date expirationDate;

	public Expiration() {
		super();
	}
	
	/**
	 * Initialization method for TimerThread
	 * @Method	setDate
	 */
	public void setDate() {
		try {
			cal = Calendar.getInstance();
			
			String temp = sdf.format(cal.getTime());
			expirationDate = new Date(sdf.parse(temp).getTime());
			
			/* set gcmDate to 7 days from current time */
			cal.add(Calendar.DATE, +7);
			temp = sdf.format(cal.getTime());
			gcmDate = new Date(sdf.parse(temp).getTime());
			
			cal.add(Calendar.DATE, -7);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		setDate();
		
		try {
			/* if today is the expiration, delete the timer and alert the user and the  company  */
			if(!timerService.searchExpiration(expirationDate).isEmpty()) {
				timerVO = timerService.searchExpiration(expirationDate);
				
				for(TimerVO vo : timerVO) {
					timerService.remove(vo.getUserID(), vo.getCompanyUrl());
					userVO = userService.searchKey(vo.getUserID());
					companyVO = companyService.search(vo.getCompanyUrl());
					gcmService = new GCM(userVO.getGcmID());
					String message = "\"" + companyVO.getAlias() + "\" Provision Stop";
					gcmService.push("remove", message);
					
					HttpPoster httpPoster = new HttpPoster(companyVO.getCompanyUrl());
					httpPoster.post("timer", vo.getUserID(), null);
				}
			}
			
			/* If the expiration is 7 days left, alert the user */
			if(!timerService.searchExpiration(gcmDate).isEmpty()) {
				timerVO = timerService.searchExpiration(gcmDate);
				
				for(TimerVO vo : timerVO) {
					userVO = userService.searchKey(vo.getUserID());
					companyVO = companyService.search(vo.getCompanyUrl());
					gcmService = new GCM(userVO.getGcmID());
					String message = "\"" + companyVO.getAlias() + "\" 1 week left";
					gcmService.push("expiration", message);
				}
			}
		} catch(Exception e) {
			System.out.println("ExpriationService : [ The user is not exist ]");
			e.printStackTrace();
		}
	}
}
