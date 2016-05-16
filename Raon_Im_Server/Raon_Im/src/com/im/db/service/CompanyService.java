package com.im.db.service;

import java.util.List;

import com.im.db.dao.CompanyDAO;
import com.im.vo.CompanyVO;

/**
 * Service Object to use CompanyDAO
 * @since 	2016. 01. 16.
 * @version 1.0
 * @author 	Yoon JiSoo
 */
public class CompanyService {
	private CompanyDAO dao = new CompanyDAO();

	public boolean add(CompanyVO vo) throws Exception {
		return dao.create(vo);
	}

	public void modify(CompanyVO vo) throws Exception {
		dao.update(vo);
	}
	
	public void remove(String companyUrl) throws Exception {
		dao.delete(companyUrl);
	}
	
	public CompanyVO search(String companyUrl) throws Exception {
		return dao.search(companyUrl);
	}
	
	public CompanyVO searchUrl(String alias) throws Exception {
		return dao.searchUrl(alias);
	}
	
	public List<CompanyVO> searchList(String companyUrl) throws Exception {
		return dao.searchList(companyUrl);
	}
}
