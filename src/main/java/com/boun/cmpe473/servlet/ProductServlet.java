package com.boun.cmpe473.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.boun.cmpe473.dao.DAO;
import com.boun.cmpe473.model.Negotiation;
import com.boun.cmpe473.model.NegotiationHistory;
import com.boun.cmpe473.model.Product;
import com.boun.cmpe473.model.User;

public class ProductServlet extends BaseServlet {
	private Logger log = Logger.getLogger(getClass());
	private static final long serialVersionUID = 1L;
	private static final String REQ_PRM_PRODUCT_ID = "productId";
	private static final String REQ_PRM_NEGOTIATION_ID = "negId";
	private static final String REQ_PRM_ACTION = "action";
	private static final String REQ_PRM_NEW_PRICE = "price";

	private static final String POST_ACTION_APPROVE = "approve";
	private static final String POST_ACTION_REJECT = "reject";
	private static final String POST_ACTION_NEW_PRICE = "newPrice";

	private DAO dao;

	@Override
	public void init() throws ServletException {
		super.init();
		dao = new DAO();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Create this jsp
		User currentUser = getLoggedUser(req);
		try {
			Product product = null;

			Long productId = getLongParameter(req, REQ_PRM_PRODUCT_ID);
			Long negotiationId = getLongParameter(req, REQ_PRM_NEGOTIATION_ID);

			Negotiation negotiation = null;
			List<NegotiationHistory> history = null;
			if (negotiationId != null) {
				negotiation = dao.findNegotiationById(negotiationId);
				product = dao.getProductById(negotiation.getProductId());
			} else if (productId != null) {
				product = dao.getProductById(productId);
			}

			if (product == null) {
				resp.sendRedirect("/");
				return;
			}

			boolean isSeller = isSeller(product, currentUser);

			if (!isSeller && negotiation == null && currentUser != null) {
				// A buyer can have only one negotiation for a product.
				negotiation = dao.findNegotiationOfBuyer(product.getId(), currentUser.getId());
			}

			User seller = dao.getUserById(product.getSellerId());
			User buyer = null;

			if (negotiation != null) {
				history = dao.findHistoryOfNegotiation(negotiation.getId());
				buyer = dao.getUserById(negotiation.getUserId());
			}

			req.setAttribute("currentUser", currentUser);
			req.setAttribute("isSeller", isSeller);
			req.setAttribute("canEnterPrice", canEnterNewPrice(product, currentUser, negotiation, isSeller));
			req.setAttribute("canApprove", canApprove(product, currentUser, negotiation, isSeller));
			req.setAttribute("canLeave", canLeave(product, currentUser, negotiation, isSeller));

			req.setAttribute("seller", seller);
			req.setAttribute("buyer", buyer);

			req.setAttribute("product", product);
			req.setAttribute("negotiation", negotiation);
			req.setAttribute("history", history);

			display("/jsp/product.jsp", req, resp);
		} catch (Exception e) {
			throw new ServletException(e.getMessage(), e);
		}
	}

	private void doApprove(Product product, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
		Long negotiationId = getLongParameter(req, REQ_PRM_NEGOTIATION_ID);
		Negotiation negotiation = dao.findNegotiationById(negotiationId);
		User user = getLoggedUser(req);
		boolean isSeller = isSeller(product, user);

		if (user == null) {
			throw new ServletException("Login before any action!");
		}

		if (negotiation == null || negotiation.getStatus().equals(Negotiation.STATUS_CANCELLED) || negotiation.getStatus().equals(Negotiation.STATUS_SOLD)) {
			throw new ServletException("The negotiation is not available for approval.");
		}

		if (!isSeller) {
			throw new ServletException("You can't approve a product that you don't own!");
		}

		if (!negotiation.getProductId().equals(product.getId())) {
			throw new ServletException("This negotiation is not for this product!");
		}

		if (product.getStatus().equals(Product.STATUS_SOLD)) {
			throw new ServletException("This product is already sold!");
		}

		negotiation.setStatus(Negotiation.STATUS_SOLD);
		dao.updateNegotiation(negotiation);

		product.setStatus(Product.STATUS_SOLD);
		dao.updateProduct(product);

		resp.sendRedirect("/product?" + REQ_PRM_NEGOTIATION_ID + "=" + negotiation.getId());
	}

	private void doReject(Product product, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
		Long negotiationId = getLongParameter(req, REQ_PRM_NEGOTIATION_ID);
		Negotiation negotiation = dao.findNegotiationById(negotiationId);
		User user = getLoggedUser(req);

		if (user == null) {
			throw new ServletException("Login before any action!");
		}

		if (negotiation == null || negotiation.getStatus().equals(Negotiation.STATUS_CANCELLED) || negotiation.getStatus().equals(Negotiation.STATUS_SOLD)) {
			throw new ServletException("The negotiation is not available for approval.");
		}

		negotiation.setStatus(Negotiation.STATUS_CANCELLED);
		dao.updateNegotiation(negotiation);

		resp.sendRedirect("/product?" + REQ_PRM_NEGOTIATION_ID + "=" + negotiation.getId());
	}

	private void doNewPrice(Product product, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
		User user = getLoggedUser(req);
		if (user == null) {
			resp.sendRedirect("/login?redirect=/product?productId=" + product.getId());
			return;
		}

		Double newPrice = getDoubleParameter(req, REQ_PRM_NEW_PRICE);
		if (newPrice.compareTo(product.getPrice()) > 0) {
			throw new ServletException("New price cannot be greater than product's first price");
		}

		Long negotiationId = getLongParameter(req, REQ_PRM_NEGOTIATION_ID);
		log.info("doNewPrice product=" + product + ", newPrice=" + newPrice + ", negotiationId=" + negotiationId);

		Negotiation negotiation = null;
		if (product.getSellerId().equals(user.getId())) {
			// SELLER's PRODUCT
			if (negotiationId != null) {
				negotiation = dao.findNegotiationById(negotiationId);
			}

			doNewPriceSeller(req, resp, user, product, negotiation, newPrice);

			resp.sendRedirect("/product?negId=" + negotiation.getId());
		} else {
			// BUYER!
			negotiation = dao.findNegotiationOfBuyer(product.getId(), user.getId());

			doNewPriceBuyer(req, resp, user, product, negotiation, newPrice);

			resp.sendRedirect("/product?productId=" + product.getId());
		}
	}

	private void doNewPriceBuyer(HttpServletRequest req, HttpServletResponse resp, User user, Product product, Negotiation negotiation, Double price) throws ServletException,
			SQLException {
		if (negotiation == null) {
			// New negotiation
			negotiation = new Negotiation();
			negotiation.setProductId(product.getId());
			negotiation.setStartDate(new Date());
			negotiation.setStatus(Negotiation.STATUS_WAIT_SELLER);
			negotiation.setUserId(user.getId());
			dao.insertNegotiation(negotiation);

			NegotiationHistory negotiationHistory = new NegotiationHistory();
			negotiationHistory.setBuyerPrice(price);
			negotiationHistory.setDate(new Date());
			negotiationHistory.setNegotiationId(negotiation.getId());
			negotiationHistory.setSellerPrice(product.getPrice());
			dao.insertNegotiationHistory(negotiationHistory);
		} else {
			// Old negotiation I guess.
			List<NegotiationHistory> history = dao.findHistoryOfNegotiation(negotiation.getId());
			if (history.size() == 0) {
				throw new ServletException("Empty history! Damn. I didn't expect this error.");
			}

			if (!negotiation.getStatus().equals(Negotiation.STATUS_WAIT_BUYER)) {
				throw new ServletException("It's not buyer's turn!");
			}

			NegotiationHistory lastOffer = history.get(history.size() - 1);
			lastOffer.setBuyerPrice(price);
			dao.updateNegotiationHistory(lastOffer);

			negotiation.setStatus(Negotiation.STATUS_WAIT_SELLER);
			dao.updateNegotiation(negotiation);
		}
	}

	private void doNewPriceSeller(HttpServletRequest req, HttpServletResponse resp, User user, Product product, Negotiation negotiation, Double price) throws ServletException,
			SQLException {
		if (negotiation == null) {
			throw new ServletException("Seller can't enter new price if there is no negotiation!");
		}

		if (!negotiation.getStatus().equals(Negotiation.STATUS_WAIT_SELLER)) {
			throw new ServletException("It is buyer's turn in negotiation!");
		}

		NegotiationHistory newHistory = new NegotiationHistory();
		newHistory.setDate(new Date());
		newHistory.setNegotiationId(negotiation.getId());
		newHistory.setSellerPrice(price);
		dao.insertNegotiationHistory(newHistory);

		negotiation.setStatus(Negotiation.STATUS_WAIT_BUYER);
		dao.updateNegotiation(negotiation);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String action = req.getParameter(REQ_PRM_ACTION);
			Long productId = getLongParameter(req, REQ_PRM_PRODUCT_ID);

			Product product = dao.getProductById(productId);

			if (POST_ACTION_APPROVE.equals(action)) {
				doApprove(product, req, resp);
			} else if (POST_ACTION_REJECT.equals(action)) {
				doReject(product, req, resp);
			} else if (POST_ACTION_NEW_PRICE.equals(action)) {
				doNewPrice(product, req, resp);
			} else {
				// Form submit by pressing enter key.
				doNewPrice(product, req, resp);
			}
		} catch (SQLException e) {
			throw new ServletException("DB error occured!", e);
		}
	}

	private boolean canEnterNewPrice(Product product, User currentUser, Negotiation negotiation, boolean isSeller) {
		if (currentUser == null) {
			// Login is required for negotiation
			return false;
		}

		if (negotiation == null && isSeller) {
			// Seller can't start negotiation
			return false;
		}

		if (product.getStatus().equals(Product.STATUS_SOLD)) {
			// Product must be on sale.
			return false;
		}

		if (negotiation != null) {
			if (negotiation.getStatus().equals(Negotiation.STATUS_SOLD) || negotiation.getStatus().equals(Negotiation.STATUS_CANCELLED)) {
				// You can't continue sold or cancelled negotiations
				return false;
			}

			if (negotiation.getStatus().equals(Negotiation.STATUS_WAIT_BUYER) && isSeller) {
				// it is buyer's turn
				return false;
			} else if (negotiation.getStatus().equals(Negotiation.STATUS_WAIT_SELLER) && !isSeller) {
				// it is seller's turn
				return false;
			}
		}

		return true;
	}

	private boolean canApprove(Product product, User currentUser, Negotiation negotiation, boolean isSeller) {
		if (product == null || negotiation == null || currentUser == null || !isSeller) {
			return false;
		}

		if (negotiation.getStatus().equals(Negotiation.STATUS_SOLD) || negotiation.getStatus().equals(Negotiation.STATUS_CANCELLED)) {
			return false;
		}

		return true;
	}

	private boolean canLeave(Product product, User currentUser, Negotiation negotiation, boolean isSeller) {
		if (product == null || negotiation == null || currentUser == null) {
			return false;
		}

		if (negotiation.getStatus().equals(Negotiation.STATUS_SOLD) || negotiation.getStatus().equals(Negotiation.STATUS_CANCELLED)) {
			return false;
		}

		return true;
	}

	private boolean isSeller(Product product, User currentUser) {
		if (product == null || currentUser == null) {
			return false;
		}

		return product.getSellerId().equals(currentUser.getId());
	}
}
