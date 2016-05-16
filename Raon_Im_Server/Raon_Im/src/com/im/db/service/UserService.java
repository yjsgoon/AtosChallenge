package com.im.db.service;

import java.util.List;

import com.im.db.dao.UserDAO;
import com.im.vo.UserVO;

/**
 * Service Object to user UserDAO
 * @since 	2016. 01. 16.
 * @version 1.0
 * @author 	Yoon JiSoo
 */
public class UserService {
	private UserDAO dao = new UserDAO();
	
	public boolean add(UserVO vo) throws Exception {
		return dao.create(vo);
	}
	
	public void modifyKey(UserVO vo) throws Exception {
		dao.update(vo);
	}
	
	public void removeKey(String userID) throws Exception {
		dao.delete(userID);
	}
	
	public UserVO searchKey(String userID) throws Exception {
		return dao.search(userID);
	}
	
	public List<UserVO> searchListKey(String userID) throws Exception {
		return dao.searchList(userID);
	}
}
