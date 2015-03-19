package com.boun.cmpe473.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;

import com.boun.cmpe473.dao.DAO;
import com.boun.cmpe473.model.User;

public class SignupServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	private DAO dao;

	@Override
	public void init() throws ServletException {
		super.init();
		dao = new DAO();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO CREATE & DEVELOP this JSP.
		display("/jsp/signup.jsp", req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Submit Signup form here.

		String fname = req.getParameter("fname");
		String lname = req.getParameter("lname");
		String email = req.getParameter("email");
		String pass = req.getParameter("pass");
		String passcheck = req.getParameter("passcheck");
		
//		try {
//			if (dao.checkUser(email) == null){
//				display("/jsp/error.jsp", req, resp);
//			}
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

		String passwordHash = DigestUtils.md5Hex(pass);
		User user = null;
		try {
			dao.insertUser(fname, lname, email, passwordHash);
			user = dao.getUser(email, passwordHash);
			setUserLogged(req, user);
			resp.sendRedirect("/");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
