package ycm.mms.service;

import ycm.mms.model.Account;
import ycm.mms.model.Session;
import ycm.mms.model.User;

public interface AccountService {
	
	boolean verifyAccount(String account);
	
	boolean register(Account account, User user);
	Session login(String account, String password);
	boolean logout(int accountId);
}
