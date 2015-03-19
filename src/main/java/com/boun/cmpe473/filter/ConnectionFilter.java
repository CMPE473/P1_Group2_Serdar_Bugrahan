package com.boun.cmpe473.filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.boun.cmpe473.dao.ConnectionManager;

public class ConnectionFilter implements javax.servlet.Filter {
	private Logger log = Logger.getLogger(getClass());
	private ConnectionManager connectionManager;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("info level log test");
		connectionManager = new ConnectionManager();
		try {
			connectionManager.initialize();
		} catch (IOException e) {
			throw new ServletException("ConnectionManager could not be initialized!", e);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;

		String uri = req.getRequestURI();
		if (uri.contains("/static/") || uri.contains("favicon.ico")) {
			log.debug("skip=" + uri);
			chain.doFilter(request, response);
			return;
		}

		Connection conn = null;
		try {
			conn = connectionManager.openConnection();
			conn.setAutoCommit(false);
			log.debug("connection open for " + uri);

			chain.doFilter(request, response);

			conn.commit();
		} catch (Exception e) {
			if (conn != null) {
				try {
					log.error("Exception occured! making rollback for " + uri);
					conn.rollback();
				} catch (SQLException e1) {
					log.error("Rollback exception!", e1);
				}
			}

			throw new ServletException(e.getMessage(), e);
		} finally {
			connectionManager.releaseConnection();
			log.debug("connection closed for " + uri);
		}
	}

	@Override
	public void destroy() {

	}

}
