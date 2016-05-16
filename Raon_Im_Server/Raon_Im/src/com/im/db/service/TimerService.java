package com.im.db.service;

import java.sql.Date;
import java.util.List;

import com.im.db.dao.TimerDAO;
import com.im.vo.TimerVO;

/**
 * Service Object to user TimerDAO
 * @since 	2016. 01. 16.
 * @version 1.0
 * @author 	Yoon JiSoo
 */
public class TimerService {
	private TimerDAO dao = new TimerDAO();
	
	public boolean add(TimerVO vo) throws Exception {
		return dao.create(vo);
	}
	
	public boolean modify(TimerVO vo) throws Exception {
		return dao.update(vo);
	}
	
	public void remove(String userID, String companyUrl) throws Exception {
		dao.delete(userID, companyUrl);
	}
	
	public TimerVO search(String userID) throws Exception {
		return dao.search(userID);
	}
	
	public List<TimerVO> searchList(String userID) throws Exception {
		return dao.searchList(userID);
	}
	
	public List<TimerVO> searchExpiration(Date expirationDate) throws Exception {
		return dao.searchExpiration(expirationDate);
	}
}
