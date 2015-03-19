package com.boun.cmpe473.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class ExceptionRedirectFilter implements Filter {
	private Logger log = Logger.getLogger(getClass());
	private ServletContext servletContext;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		servletContext = filterConfig.getServletContext();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		try {
			chain.doFilter(req, res);
		} catch (Exception e) {
			log.error("Exception caught", e);
			if (StringUtils.isNotBlank(e.getMessage())) {
				req.setAttribute("errorMessage", e.getMessage());
			} else {
				req.setAttribute("errorMessage", "General Internal Error!");
			}

			servletContext.getRequestDispatcher("/jsp/error.jsp").forward(req, res);
		}
	}

	@Override
	public void destroy() {

	}

}
