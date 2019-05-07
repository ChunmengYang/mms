package ycm.mms.controller;

import java.util.Base64;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ycm.mms.model.Account;
import ycm.mms.model.JSONResult;
import ycm.mms.model.Session;
import ycm.mms.model.User;
import ycm.mms.security.RSAUtil;
import ycm.mms.service.AccountService;
import ycm.mms.util.MD5Utils;
import ycm.mms.util.StringUtils;

@Controller
@RequestMapping("/account")
public class AccountController {
	private AccountService accountService;
    
    public AccountService getAccountService() {
        return accountService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @RequestMapping(path="/verify", method=RequestMethod.GET)
    @ResponseBody
    public JSONResult verifyAccount(@RequestParam("account") String account){
    	JSONResult result = new JSONResult();
    	if (account == null || account.equals("")) {
    		result.setError("账号不能为空");
    		return result;
    	}
    	
    	boolean status = accountService.verifyAccount(account);
    	result.setSuccess(true);
		result.setResult(status);
    	
    	return result;
    }
    
    @RequestMapping(path="/register", method=RequestMethod.POST)
    @ResponseBody
    public JSONResult register(@RequestParam("phone") String phone, 
    		@RequestParam("password") String password, 
    		@RequestParam("user_name") String userName) {
    	
    	JSONResult result = new JSONResult();
    	
    	if (!StringUtils.isMobile(phone)) {
    		result.setError("不是有效的手机号");
        	return result;
    	}   	
    	if (password == null || password.equals("")) {
    		result.setError("密码不能为空");
        	return result;
    	}
    	if (userName == null || userName.equals("")) {
    		result.setError("用户名不能为空");
        	return result;
    	}
    	boolean status = accountService.verifyAccount(phone);
    	if (status) {
    		result.setError("账户已经存在");
        	return result;
    	}
    	
    	Account account = new Account();
    	account.setLoginName(phone);
    	account.setPhone(phone);
    	account.setPassword(MD5Utils.encode(password));
    	
    	User user = new User();
    	user.setUserName(userName);
    	status = accountService.register(account, user);
    	
    	if (!status) {
    		result.setError("注册失败");
        	return result;
    	}
    	
    	result.setSuccess(true);
    	result.setResult(user);
    	return result;
    }
    
    @RequestMapping(path="/login", method=RequestMethod.POST)
    @ResponseBody
    public JSONResult login(@RequestParam("encryp_data") String encrypData){
    	JSONResult result = new JSONResult();
    	
    	encrypData = encrypData.replaceAll(" ", "+");
		byte[] decodedData = Base64.getDecoder().decode(encrypData);
		byte[] decryptData = null;
		try {
			decryptData = RSAUtil.decryptByPrivateKey(decodedData, RSAUtil.PWD_PRIVATEKEY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (decryptData == null || decryptData.length == 0) {
			result.setError("参数错误");
        	return result;
		}
		String params = new String(decryptData);
		
		String account = StringUtils.getParamByUrl(params, "account");
		String password = StringUtils.getParamByUrl(params, "password");
    	if (account == null || "".equals(account)) {
        	result.setError("账号不能为空");
        	return result;
    	}
    	if (password == null || "".equals(password)) {
    		result.setError("密码不能为空");
        	return result;
    	}
    	
    	Session session = accountService.login(account, password);
    	if (session == null) {
    		result.setError("账号或密码错误");
        	return result;
    	}
    	
    	result.setSuccess(true);
    	result.setResult(session);
    	return result;
    }
    
    @RequestMapping(path="/logout", method=RequestMethod.GET)
    @ResponseBody
    public JSONResult logout(HttpSession httpSession){
    	JSONResult result = new JSONResult();
    	
    	Session session = (Session)httpSession.getAttribute("session");
    	
    	if (session != null && session.getAccountId() > 0) {
    		boolean status = accountService.logout(session.getAccountId());
        	result.setSuccess(true);
        	result.setResult(status);
    	} else {
    		result.setError("未找到登录信息");
    	}
 
    	return result;
    }
}
