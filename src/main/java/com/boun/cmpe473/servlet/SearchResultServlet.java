package com.boun.cmpe473.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boun.cmpe473.dao.DAO;
import com.boun.cmpe473.model.Product;

public class SearchResultServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private static final String REQ_PRM_SEARCH_TEXT = "q";

	private DAO dao;

	@Override
	public void init() throws ServletException {
		super.init();
		dao = new DAO();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			String searchText = req.getParameter(REQ_PRM_SEARCH_TEXT);
			req.setAttribute("searchText", searchText);

			List<Product> products = dao.searchProducts(searchText);
			req.setAttribute("searchResults", products);

			display("/jsp/search.jsp", req, resp);
		} catch (Exception e) {
			throw new ServletException(e.getMessage(), e);
		}
	}
}
