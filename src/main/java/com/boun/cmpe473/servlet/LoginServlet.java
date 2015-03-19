package com.boun.cmpe473.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.boun.cmpe473.dao.DAO;
import com.boun.cmpe473.model.User;

public class LoginServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private DAO dao;

	@Override
	public void init() throws ServletException {
		super.init();
		dao = new DAO();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO CREATE & DEVELOP this JSP.
		String redirectUrl = req.getParameter("redirect");
		if (redirectUrl != null) {
			req.setAttribute("redirectUrl", redirectUrl);
		}

		display("/jsp/login.jsp", req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO submit login form here.
		String email = req.getParameter("email");
		String pass = req.getParameter("pass");
		String passwordHash = DigestUtils.md5Hex(pass);
		String redirectUrl = req.getParameter("redirectUrl");

		User user = null;
		try {
			user = dao.getUser(email, passwordHash);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (user != null) {
			setUserLogged(req, user);
			if (StringUtils.isNotBlank(redirectUrl)) {
				resp.sendRedirect(redirectUrl);
			} else {
				resp.sendRedirect("/");
			}
		} else {
			resp.sendRedirect("/error");
		}

	}
}
