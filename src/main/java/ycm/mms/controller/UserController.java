package ycm.mms.controller;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
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

import ycm.mms.model.JSONResult;
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
    
    @RequestMapping(path="/icon/download", method=RequestMethod.GET)
    public ResponseEntity<byte[]> iconDownload(HttpSession httpSession)  throws IOException{
    	Session session = (Session)httpSession.getAttribute("session");
        if (session != null && session.getAccountId() > 0) {
        	User user = userService.getUserByAccountId(session.getAccountId());
        	if (user != null && user.getId() > 0) {
        		String filePath = user.getIconPath();
        		if (filePath != null && !"".equals(filePath)) {
        			File file = new File(filePath);
            		if (file.exists()) {
            			String filename =  "usericon" + filePath.substring(filePath.lastIndexOf("."), filePath.length());
                        HttpHeaders headers = new HttpHeaders();
                        String downloadFileName = new String(filename.getBytes("UTF-8"),"iso-8859-1");
                        headers.setContentDispositionFormData("attachment", downloadFileName);
                        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

                        //MediaType:互联网媒介类型contentType:具体请求中的媒体类型信息  
                        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers,HttpStatus.CREATED);
            		}
        		}
        	}
        }
		return null;
    }
    
    @RequestMapping(path="/icon/upload", method=RequestMethod.POST)
    @ResponseBody
    public JSONResult iconUpload(HttpServletRequest request,HttpSession httpSession) {
    	JSONResult result = new JSONResult();
    	
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
                    	result.setError("上传的头像不是图片类型");
						return result; 
                    }
                    
                    Session session = (Session)httpSession.getAttribute("session");
                    if (session != null && session.getAccountId() > 0) {
                    	User user = userService.getUserByAccountId(session.getAccountId());
                    	if (user != null && user.getId() > 0) {
                    		String suffix = fileName.substring(fileName.lastIndexOf(".")
                                    + 1, fileName.length());
                    		String filePath = "/Users/mash5/Downloads/mmsUpload/" + user.getId() + "." + suffix;
                            try {
        						file.transferTo(new File(filePath));
        						
        						userService.updateUserIcon(user.getId(), filePath);
        						result.setSuccess(true);
        			            return result; 
        					} catch (IllegalStateException | IOException e) {
        						// TODO Auto-generated catch block
        						e.printStackTrace();
        						
        						result.setError(e.getMessage());
        						return result; 
        					}
                    	} else {
                    		result.setError("用户未登录");
                            return result; 
                    	}
                    } else {
                    	result.setError("用户未登录");
                        return result; 
                    }
                }
            }
            result.setError("未找到上传的图片");
            return result; 
        }
        
        result.setError("未找到上传的图片");
        return result;
    }
}