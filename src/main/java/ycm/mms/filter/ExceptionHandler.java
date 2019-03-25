package ycm.mms.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class ExceptionHandler implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		// TODO Auto-generated method stub
		ModelAndView mv = new ModelAndView();
        // 上传文件超过最大限制，异常处理
        if (ex instanceof org.springframework.web.multipart.MaxUploadSizeExceededException) { 
            mv.addObject("message", "上传文件过大");
            mv.setViewName("error");
            return mv;
        }
		
		return null;
	}

}
