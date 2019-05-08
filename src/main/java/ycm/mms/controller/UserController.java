package ycm.mms.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import ycm.mms.model.Session;
import ycm.mms.model.User;
import ycm.mms.service.UserService;
import ycm.mms.util.StringUtils;


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
    public Map<String, Object> updateUserName(@RequestParam("id") int id, @RequestParam("user_name") String userName, @RequestParam("nick_name") String nickName, @RequestParam("sex") int sex){
    	Map<String, Object> map = new HashMap<String, Object>();
    	if (id == 0) {
    		map.put("error", "用户ID不能为空");
    		return map;
    	}
    	
    	boolean status = userService.updateUser(id, userName, nickName, sex);
    	if (!status) {
    		map.put("error", "更新用户信息失败");
    		return map;
    	}
    	
    	User user = userService.getUser(id);
    	    
        map.put("success", true);
        map.put("user", user);
        
		return map;
    }
    
    @RequestMapping(path="/{id}", method=RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getPerson(@PathVariable("id") int id){
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	if (id == 0) {
    		map.put("error", "用户ID不能为空");
    		return map;
    	}
    	
        User user = userService.getUser(id);
        if (user == null) {
        	map.put("error", "用户不存在");
    		return map;
        }

        map.put("success", true);
        map.put("user", user);
        
        return map;
    }
    
    @RequestMapping(path="/icon/download", method=RequestMethod.GET)
    public ResponseEntity<byte[]> iconDownload(HttpSession httpSession)  throws IOException{
    	Session session = (Session)httpSession.getAttribute("session");
        if (session != null && session.getAccountId() > 0) {
        	User user = userService.getUserByAccountId(session.getAccountId());
        	if (user != null && user.getId() > 0) {
        		byte[] icon = user.getIcon();
        		String suffix = user.getIconSuffix();
        		if (icon != null && icon.length > 0) {
        			if (suffix == null || "".equals(suffix)) {
        				suffix = "JPG";
        			}
        			String filename =  user.getId() + "." + suffix;
                    HttpHeaders headers = new HttpHeaders();
                    String downloadFileName = new String(filename.getBytes("UTF-8"),"iso-8859-1");
                    headers.setContentDispositionFormData("attachment", downloadFileName);
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

                    // MediaType:互联网媒介类型contentType:具体请求中的媒体类型信息  
                    return new ResponseEntity<byte[]>(icon, headers,HttpStatus.CREATED);
        		}
        	}
        }
		return null;
    }
    
    @RequestMapping(path="/icon/upload", method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> iconUpload(HttpServletRequest request,HttpSession httpSession) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver != null && multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
            
            // 获取multiRequest中所有的文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                // 一次遍历所有文件
                MultipartFile file = multiRequest.getFile(iter.next().toString());
                if (file != null) {
                    String fileName = file.getOriginalFilename();
                    if (fileName == null || "".equals(fileName)) {
                    	continue;
                    }
                    if (!StringUtils.checkImageFileSuffix(fileName)) {
                    	map.put("error", "上传的头像不是图片类型");
                		return map;
                    }
                    
                    Session session = (Session)httpSession.getAttribute("session");
                    if (session != null && session.getAccountId() > 0) {
                    	User user = userService.getUserByAccountId(session.getAccountId());
                    	if (user != null && user.getId() > 0) {
                    		String suffix = fileName.substring(fileName.lastIndexOf(".")
                                    + 1, fileName.length());

    						try {
								boolean status = userService.updateUserIcon(user.getId(), file.getBytes(), suffix);
								if (status) {
									map.put("success", true);
			                		return map;
								} else {
									map.put("error", "保存头像失败");
			                		return map;
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								map.put("error", "保存头像失败");
		                		return map; 
							} 
                    	} else {
                    		map.put("error", "用户未登录");
	                		return map;
                    	}
                    } else {
                    	map.put("error", "用户未登录");
                		return map;
                    }
                }
            }
            
            map.put("error", "未找到上传的头像");
    		return map;
        }
        
        map.put("error", "未找到上传的头像");
		return map;
    }
}