package com.tykj.mvc.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author lukw
 * @email 13507615840@163.com
 * @create 2018-02-28 13:45
 **/
public class RequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {

       HttpServletRequest request = (HttpServletRequest)servletRequest;
       String requestUri = request.getRequestURI();
       if(requestUri.indexOf(".ico") == -1){
           return;
       }
       chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
