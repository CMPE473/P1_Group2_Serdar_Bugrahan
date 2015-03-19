package com.boun.cmpe473.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boun.cmpe473.dao.DAO;
import com.boun.cmpe473.model.User;

public class HomeServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private DAO dao;

	@Override
	public void init() throws ServletException {
		super.init();
		dao = new DAO();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			User currentUser = getLoggedUser(req);
			req.setAttribute("currentUser", currentUser);

			if (currentUser == null) {
				req.setAttribute("products", dao.getRandomProducts(-1L, 4));
			} else {
				req.setAttribute("products", dao.getRandomProducts(currentUser.getId(), 4));
			}

			display("/jsp/home.jsp", req, resp);
		} catch (Exception e) {
			throw new ServletException(e.getMessage(), e);
		}
	}

}
