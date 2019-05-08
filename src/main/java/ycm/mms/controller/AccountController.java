package ycm.mms.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ycm.mms.model.Account;
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
    public Map<String, Object> verifyAccount(@RequestParam("account") String account){
    	Map<String, Object> map = new HashMap<String, Object>();
    	if (account == null || account.equals("")) {	
    		map.put("error", "账号不能为空");
    		return map;
    	}
    	
    	boolean status = accountService.verifyAccount(account);
    	
		map.put("success", true);
		map.put("status", status);
		return map;
    }
    
    @RequestMapping(path="/register", method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> register(@RequestParam("phone") String phone, 
    		@RequestParam("password") String password, 
    		@RequestParam("user_name") String userName) {
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	if (!StringUtils.isMobile(phone)) {
    		map.put("error", "不是有效的手机号");
    		return map;
    	}   	
    	if (password == null || password.equals("")) {
    		map.put("error", "密码不能为空");
    		return map;
    	}
    	if (userName == null || userName.equals("")) {
    		map.put("error", "用户名不能为空");
    		return map;
    	}
    	boolean status = accountService.verifyAccount(phone);
    	if (status) {
    		map.put("error", "账户已经存在");
    		return map;
    	}
    	
    	Account account = new Account();
    	account.setLoginName(phone);
    	account.setPhone(phone);
    	account.setPassword(MD5Utils.encode(password));
    	
    	User user = new User();
    	user.setUserName(userName);
    	status = accountService.register(account, user);
    	
    	if (!status) {
    		map.put("error", "注册失败");
    		return map;
    	}
    	map.put("success", true);
    	map.put("user", user);
    	return map;
    }
    
    @RequestMapping(path="/login", method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(@RequestParam("encryp_data") String encrypData){
    	Map<String, Object> map = new HashMap<String, Object>();
    	
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
			map.put("error", "参数错误");
    		return map;
		}
		String params = new String(decryptData);
		
		String account = StringUtils.getParamByUrl(params, "account");
		String password = StringUtils.getParamByUrl(params, "password");
    	if (account == null || "".equals(account)) {
    		map.put("error", "账号不能为空");
    		return map;
    	}
    	if (password == null || "".equals(password)) {
    		map.put("error", "密码不能为空");
    		return map;
    	}
    	
    	Session session = accountService.login(account, password);
    	if (session == null) {
    		map.put("error", "账号或密码错误");
    		return map;
    	}
    	map.put("success", true);
    	map.put("session", session);
		return map;
    }
    
    @RequestMapping(path="/logout", method=RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> logout(HttpSession httpSession){
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	Session session = (Session)httpSession.getAttribute("session");
    	
    	if (session != null && session.getAccountId() > 0) {
    		boolean status = accountService.logout(session.getAccountId());
    		map.put("success", true);
        	map.put("status", status);
    	} else {
    		map.put("error", "未找到登录信息");
    	}
 
    	return map;
    }
}
