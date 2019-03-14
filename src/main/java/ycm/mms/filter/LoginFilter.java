package ycm.mms.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ycm.mms.util.StringUtils;

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
		if (path.indexOf("/account") != -1 || 
			path.indexOf("/register.jsp") != -1 || 
			path.indexOf("/login.jsp") != -1 || 
			path.indexOf("/js/") != -1 || 
			path.indexOf("/css/") != -1 || 
			path.indexOf("/images/") != -1) {
			
			// pass the request along the filter chain
			chain.doFilter(servletRequest, servletResponse);
			return;
		}
		
		Object session = request.getSession().getAttribute("session");
		if (session == null) {
			String redirectPath = StringUtils.concat(request.getScheme(),
					"://",
					request.getServerName(),
					":",
					String.valueOf(request.getServerPort()),
					request.getContextPath(),
					"/login.jsp");
			response.sendRedirect(redirectPath);
			return;
		}
		
		chain.doFilter(servletRequest, servletResponse);
		return;
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
