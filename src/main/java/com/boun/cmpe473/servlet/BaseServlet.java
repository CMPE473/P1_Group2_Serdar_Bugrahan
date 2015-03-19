package com.boun.cmpe473.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.boun.cmpe473.model.User;

public class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void display(String jspPath, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		getServletContext().getRequestDispatcher(jspPath).forward(req, resp);
	}

	public Long getLongParameter(HttpServletRequest req, String paramName) {
		String parameter = req.getParameter(paramName);
		if (StringUtils.isNotBlank(parameter)) {
			return Long.valueOf(parameter);
		}
		return null;
	}

	public Integer getIntParameter(HttpServletRequest req, String paramName) {
		String parameter = req.getParameter(paramName);
		if (StringUtils.isNotBlank(parameter)) {
			return Integer.valueOf(parameter);
		}
		return null;
	}

	public Double getDoubleParameter(HttpServletRequest req, String paramName) {
		String parameter = req.getParameter(paramName);
		if (StringUtils.isNotBlank(parameter)) {
			return Double.valueOf(parameter);
		}
		return null;
	}

	public void setUserLogged(HttpServletRequest req, User user) {
		if (user == null) {
			req.getSession().removeAttribute("user");
		} else {
			req.getSession().setAttribute("user", user);
		}
	}

	public User getLoggedUser(HttpServletRequest req) {
		return (User) req.getSession().getAttribute("user");
	}
}
