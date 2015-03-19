package com.boun.cmpe473.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boun.cmpe473.dao.DAO;
import com.boun.cmpe473.model.Negotiation;
import com.boun.cmpe473.model.Product;
import com.boun.cmpe473.model.User;

public class HistoryServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private static final String REQ_PRM_HISTORY_TYPE = "type";
	private static final String HISTORY_TYPE_SELL = "sell";
	private static final String HISTORY_TYPE_BUY = "buy";

	private DAO dao;

	@Override
	public void init() throws ServletException {
		super.init();
		dao = new DAO();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String historyType = req.getParameter(REQ_PRM_HISTORY_TYPE);
		User currentUser = getLoggedUser(req);
		req.setAttribute("currentUser", currentUser);
		if (currentUser == null) {
			resp.sendRedirect("/login?redirect=/history?" + REQ_PRM_HISTORY_TYPE + "=" + historyType);
			return;
		}

		if (HISTORY_TYPE_BUY.equals(historyType)) {
			// get history for items that I'm buying
			showBuyHistory(req, resp, currentUser);
		} else if (HISTORY_TYPE_SELL.equals(historyType)) {
			// get history for items that I'm selling
			showSellHistory(req, resp, currentUser);
		} else {
			// Invalid History Type Parameter.
			// Redirect to a valid history page.
			resp.sendRedirect("/history?" + REQ_PRM_HISTORY_TYPE + "=" + HISTORY_TYPE_SELL);
			return;
		}

		// TODO CREATE & DEVELOP this JSP.
		display("/jsp/history.jsp", req, resp);
	}

	private void showSellHistory(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
		try {
			List<Negotiation> sellerNegotiations = dao.getSellerNegotiations(user.getId());
			Map<Long, Product> sellerProducts = dao.getSellerProducts(user.getId());

			req.setAttribute("sellerNegotiations", sellerNegotiations);
			req.setAttribute("sellerProducts", sellerProducts);
			req.setAttribute("showSellHistory", true);
		} catch (SQLException e) {
			throw new ServletException("DB error occured", e);
		}
	}

	private void showBuyHistory(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
		try {
			List<Negotiation> buyerNegotiations = dao.getBuyerNegotiations(user.getId());
			Map<Long, Product> buyerProducts = dao.getBuyerProducts(user.getId());

			req.setAttribute("buyerNegotiations", buyerNegotiations);
			req.setAttribute("buyerProducts", buyerProducts);
			req.setAttribute("showSellHistory", false);
		} catch (SQLException e) {
			throw new ServletException("DB error occured", e);
		}
	}

}
