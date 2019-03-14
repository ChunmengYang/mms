package ycm.mms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ycm.mms.model.JSONResult;
import ycm.mms.model.User;
import ycm.mms.service.UserService;


@Controller
@RequestMapping("/user")
public class UserController {
    
    private UserService userService;
    
    public UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    @RequestMapping(path="/update", method=RequestMethod.POST)
    @ResponseBody
    public JSONResult updateUserName(@RequestParam("id") int id, @RequestParam("user_name") String userName, @RequestParam("nick_name") String nickName, @RequestParam("sex") int sex){
    	JSONResult result = new JSONResult();
    	if (id == 0) {
    		result.setError("用户ID不能为空");
    		return result;
    	}
    	
    	boolean status = userService.updateUser(id, userName, nickName, sex);
    	if (!status) {
    		result.setError("更新用户信息失败");
    		return result;
    	}
    	
    	User user = userService.getUser(id);
		result.setSuccess(true);
		result.setResult(user);
		return result;
    }
    
    @RequestMapping(path="/{id}", method=RequestMethod.GET)
    @ResponseBody
    public JSONResult getPerson(@PathVariable("id") int id){
    	JSONResult result = new JSONResult();
    	if (id == 0) {
    		result.setError("用户ID不能为空");
    		return result;
    	}
    	
        User user = userService.getUser(id);
        if (user == null) {
        	result.setError("用户不存在");
    		return result;
        }
        
        result.setSuccess(true);
        result.setResult(user);
        return result;
    }
}