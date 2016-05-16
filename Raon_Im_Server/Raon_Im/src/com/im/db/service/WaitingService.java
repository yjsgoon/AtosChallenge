package com.im.db.service;

import java.util.List;

import com.im.db.dao.WaitingDAO;
import com.im.vo.WaitingVO;

/**
 * Service Object to user WaitingDAO
 * @since 	2016. 3. 29.
 * @version	1.0
 * @author 	Yoon JiSoo
 */
public class WaitingService {
	private WaitingDAO dao = new WaitingDAO();
	
	public boolean add(WaitingVO vo) throws Exception {
		return dao.create(vo);
	}
	
	public boolean remove(String userID, String companyUrl) throws Exception {
		return dao.delete(userID, companyUrl);
	}
	
	public List<WaitingVO> searchList(String userID) throws Exception {
		return dao.searchList(userID);
	}
}
