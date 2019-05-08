package ycm.mms.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ycm.mms.model.Session;
import ycm.mms.service.AccountService;

/**
 * Servlet Filter implementation class LoginFilter
 */
public class LoginFilter implements Filter {
	/**
     * Default constructor. 
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String path = request.getRequestURI();
		if (path.indexOf(".jsp") != -1 || 
			path.indexOf("/account/verify") != -1 || 
			path.indexOf("/account/register") != -1 || 
			path.indexOf("/account/login") != -1 || 
			path.indexOf("/js/") != -1 || 
			path.indexOf("/css/") != -1 || 
			path.indexOf("/images/") != -1) {
			
			// pass the request along the filter chain
			chain.doFilter(servletRequest, servletResponse);
			return;
		}
		
		// 无论是GET还是POST，获取参数的方法是相同的。
		String sign = request.getParameter("session");
		if (sign != null && !"".equals(sign)) {
			ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
			AccountService accountService = (AccountService)appContext.getBean("accountService");
			
			Session session = accountService.checkSession(sign);
			if (session != null) {
				request.getSession().setAttribute("session", session);
				
				chain.doFilter(servletRequest, servletResponse);
				return;
			}
		}
		
		response.setCharacterEncoding("UTF-8");  
	    response.setContentType("application/json; charset=utf-8");
	    PrintWriter out = null;  
	    try {  
	        out = response.getWriter();  
	        out.append("{error: \"Invalid session.\"}");
	    } catch (IOException e) {  
	        e.printStackTrace();
	        response.sendError(500);
	    } finally {  
	        if (out != null) {
	            out.close();  
	        }
	    }
		return;
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
