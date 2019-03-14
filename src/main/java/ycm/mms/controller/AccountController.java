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
    
    @RequestMapping(path="/register", method=RequestMethod.POST)
    public String register(@RequestParam("phone") String phone, 
    		@RequestParam("password") String password, 
    		@RequestParam("user_name") String userName, HttpSession httpSession){
    	
    	if (!StringUtils.isMobile(phone)) {
    		httpSession.setAttribute("error", "不是有效的手机号");
    		return "redirect:/register.jsp";
    	}   	
    	if (password == null || password.equals("")) {
    		httpSession.setAttribute("error", "密码不能为空");
    		return "redirect:/register.jsp";
    	}
    	if (userName == null || userName.equals("")) {
    		httpSession.setAttribute("error", "用户名不能为空");
    		return "redirect:/register.jsp";
    	}
    	boolean status = accountService.verifyAccount(phone);
    	if (status) {
    		httpSession.setAttribute("error", "账户已经存在");
    		return "redirect:/register.jsp";
    	}
    	
    	Account account = new Account();
    	account.setLoginName(phone);
    	account.setPhone(phone);
    	account.setPassword(MD5Utils.encode(password));
    	
    	User user = new User();
    	user.setUserName(userName);
    	status = accountService.register(account, user);
    	
    	if (!status) {
    		httpSession.setAttribute("error", "注册失败");
    		return "redirect:/register.jsp";
    	}
        return "redirect:/login.jsp";
    }
    
    @RequestMapping(path="/login", method=RequestMethod.POST)
    public String login(@RequestParam("encryp_data") String encrypData, HttpSession httpSession){
    	
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
			httpSession.setAttribute("error", "参数错误");
    		return "redirect:/login.jsp";
		}
		String params = new String(decryptData);
		
		String account = StringUtils.getParamByUrl(params, "account");
		String password = StringUtils.getParamByUrl(params, "password");
    	if (account == null || "".equals(account)) {
    		httpSession.setAttribute("error", "账号不能为空");
    		return "redirect:/login.jsp";
    	}
    	if (password == null || "".equals(password)) {
    		httpSession.setAttribute("error", "密码不能为空");
    		return "redirect:/login.jsp";
    	}
    	
    	Session session = accountService.login(account, password);
    	if (session == null) {
    		httpSession.setAttribute("error", "账号或密码错误");
    		return "redirect:/login.jsp";
    	}
    	
    	httpSession.setAttribute("session", session);
    	return "redirect:/index.jsp";
    }
    
    @RequestMapping(path="/logout", method=RequestMethod.GET)
    @ResponseBody
    public JSONResult logout(HttpSession httpSession){
    	JSONResult result = new JSONResult();
    	Session session = (Session)httpSession.getAttribute("session");
    	
    	if (session != null) {
    		
    	}

    	result.setSuccess(true);
		result.setResult(true);
    	
    	return result;
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
}
