package ycm.mms.service.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ycm.mms.dao.AccountMapper;
import ycm.mms.dao.SessionMapper;
import ycm.mms.dao.UserMapper;
import ycm.mms.model.Account;
import ycm.mms.model.Session;
import ycm.mms.model.User;
import ycm.mms.service.AccountService;
import ycm.mms.util.MD5Utils;
import ycm.mms.util.StringUtils;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

	private AccountMapper accountMapper;
	private UserMapper userMapper;
	private SessionMapper sessionMapper;

	public AccountMapper getAccountMapper() {
		return accountMapper;
	}
	
	@Autowired
	public void setAccountMapper(AccountMapper accountMapper) {
		this.accountMapper = accountMapper;
	}
	
	public UserMapper getUserMapper() {
		return userMapper;
	}
	
	@Autowired
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	public SessionMapper getSessionMapper() {
		return sessionMapper;
	}
	
	@Autowired
	public void setSessionMapper(SessionMapper sessionMapper) {
		this.sessionMapper = sessionMapper;
	}
	
	@Override
	public boolean verifyAccount(String account) {
		// TODO Auto-generated method stub
		Integer status = null;

		if (StringUtils.isMobile(account)) {
			status = accountMapper.verifyPhone(account);
		} else if(StringUtils.isEmail(account)) {
			status = accountMapper.verifyEmail(account);
		} else {
			status = accountMapper.verifyLoginName(account);
		}
		if (status !=null && status.intValue() == 1) {
			return true;
		}
		return false;
	}
	
	@Transactional
	@Override
	public boolean register(Account account, User user) {
		// TODO Auto-generated method stub
		account.setStatus(1);
		int accountId = accountMapper.insert(account);
		
		if (accountId != 0) {
			user.setAccountId(account.getId());
			int userId = userMapper.insert(user);
			
			if (userId != 0) {
				return true;
			}
		}
		
		return false;
	}
	
	@Transactional
	@Override
	public Session login(String loginField, String password) {
		// TODO Auto-generated method stub
		Account account = null;
		
		if (StringUtils.isMobile(loginField)) {
			account = accountMapper.queryByLoginName(loginField);
		} else if(StringUtils.isEmail(loginField)) {
			account = accountMapper.queryByEmail(loginField);
		} else {
			account = accountMapper.queryByLoginName(loginField);
		}
		
		if (account != null && account.getId() != 0 && MD5Utils.encode(password).equals(account.getPassword())) {
			//生成SESSION
			long nowDate = new java.util.Date().getTime();
			Timestamp expiredTime = new Timestamp(nowDate + 86400000);
			
			Session session = sessionMapper.queryByAccountId(account.getId());
			if (session != null && session.getId() != 0) {
				session.setExpiredTime(expiredTime);
				int status = sessionMapper.update(session);
				if (status != 0) {
					return session;
				}
			} else {
				session = new Session();
				session.setAccountId(account.getId());
				session.setSign(StringUtils.random(32));
				session.setExpiredTime(expiredTime);
				int status = sessionMapper.insert(session);
				if (status != 0) {
					return session;
				}
			}
		}
		return null;
	}
	
	@Transactional
	@Override
	public Session checkSession(String sign) {
		Session session = sessionMapper.queryBySign(sign);
		if (session != null && session.getId() != 0) {
			long nowDate = new java.util.Date().getTime();
			Timestamp expiredTime = new Timestamp(nowDate + 86400000);
			
			session.setExpiredTime(expiredTime);
			int status = sessionMapper.update(session);
			if (status != 0) {
				return session;
			}
		}
		return null;
	}
	
	@Transactional
	@Override
	public boolean logout(int accountId) {
		// TODO Auto-generated method stub
		
		int status = sessionMapper.delete(accountId);
		if (status != 0) {
			return true;
		}
		return false;
	}
}
