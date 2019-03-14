package ycm.mms.dao;

import ycm.mms.model.Account;

public interface AccountMapper {
	int insert(Account account);
		   
	int update(Account account);
	
	Integer verifyLoginName(String loginName);
	Integer verifyPhone(String phone);
	Integer verifyEmail(String email);
	   
	Account queryByLoginName(String loginName);
	
	Account queryByPhone(String phone);
	
	Account queryByEmail(String email);
}
