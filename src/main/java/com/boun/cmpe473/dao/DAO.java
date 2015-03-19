package com.boun.cmpe473.dao;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.boun.cmpe473.model.Negotiation;
import com.boun.cmpe473.model.NegotiationHistory;
import com.boun.cmpe473.model.Product;
import com.boun.cmpe473.model.User;

public class DAO {

	private PreparedStatement getStmt(String sql) throws SQLException {
		return ConnectionManager.getConnection().prepareStatement(sql);
	}

	public List<Product> searchProducts(String query) throws SQLException {
		PreparedStatement stmt = getStmt("select * from PRODUCT where title like ? or info like ?");
		stmt.setString(1, '%' + query + '%');
		stmt.setString(2, '%' + query + '%');

		ResultSet rs = stmt.executeQuery();

		List<Product> products = new ArrayList<Product>();
		while (rs.next()) {
			products.add(convertResultSetToProduct(rs));
		}
		return products;
	}

	public List<Negotiation> getBuyerNegotiations(Long buyerId) throws SQLException {
		PreparedStatement stmt = getStmt("select * from NEGOTIATION where USER_ID = ? order by START_DATE desc");
		stmt.setLong(1, buyerId);

		ResultSet rs = stmt.executeQuery();

		List<Negotiation> negotiations = new ArrayList<Negotiation>();
		while (rs.next()) {
			negotiations.add(convertResultSetToNegotiation(rs));
		}
		return negotiations;
	}

	public List<Negotiation> getSellerNegotiations(Long sellerUserId) throws SQLException {
		PreparedStatement stmt = getStmt("select * from NEGOTIATION where PRODUCT_ID in (select ID from PRODUCT where SELLER_ID = ?) and STATUS in (?, ?) order by START_DATE desc");
		stmt.setLong(1, sellerUserId);
		stmt.setInt(2, Negotiation.STATUS_WAIT_BUYER);
		stmt.setInt(3, Negotiation.STATUS_WAIT_SELLER);
		ResultSet rs = stmt.executeQuery();

		List<Negotiation> negotiations = new ArrayList<Negotiation>();
		while (rs.next()) {
			negotiations.add(convertResultSetToNegotiation(rs));
		}
		return negotiations;
	}

	public Map<Long, Product> getBuyerProducts(Long buyerId) throws SQLException {
		PreparedStatement stmt = getStmt("select * from PRODUCT where ID in (select PRODUCT_ID from NEGOTIATION where USER_ID = ?)");
		stmt.setLong(1, buyerId);
		ResultSet rs = stmt.executeQuery();

		LinkedHashMap<Long, Product> products = new LinkedHashMap<Long, Product>();
		while (rs.next()) {
			Product product = convertResultSetToProduct(rs);
			products.put(product.getId(), product);
		}
		return products;
	}

	public Map<Long, Product> getSellerProducts(Long sellerUserId) throws SQLException {
		PreparedStatement stmt = getStmt("select * from PRODUCT where SELLER_ID = ? order by CREATE_DATE desc");
		stmt.setLong(1, sellerUserId);
		ResultSet rs = stmt.executeQuery();

		LinkedHashMap<Long, Product> products = new LinkedHashMap<Long, Product>();
		while (rs.next()) {
			Product product = convertResultSetToProduct(rs);
			products.put(product.getId(), product);
		}
		return products;
	}

	public Product getProductById(Long productId) throws SQLException {
		PreparedStatement stmtGetProductById = ConnectionManager.getConnection().prepareStatement("SELECT * FROM PRODUCT WHERE ID = ?");

		stmtGetProductById.setLong(1, productId);
		ResultSet resultSet = stmtGetProductById.executeQuery();

		Product product = null;
		if (resultSet.next()) {
			product = convertResultSetToProduct(resultSet);
		}

		stmtGetProductById.close();
		return product;
	}

	public List<Product> getRandomProducts(Long sellerId, int limit) throws SQLException {
		PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement("SELECT * FROM PRODUCT WHERE SELLER_ID != ? AND STATUS = ? ORDER BY RAND() LIMIT ?");
		stmt.setLong(1, sellerId);
		stmt.setInt(2, Product.STATUS_OPEN);
		stmt.setInt(3, limit);
		ResultSet rs = stmt.executeQuery();

		List<Product> products = new ArrayList<Product>();
		while (rs.next()) {
			products.add(convertResultSetToProduct(rs));
		}

		return products;
	}

	public void insertNegotiation(Negotiation negotiation) throws SQLException {
		PreparedStatement stmtInsertNegotiation = ConnectionManager.getConnection().prepareStatement(
				"insert into NEGOTIATION (PRODUCT_ID,USER_ID,STATUS,START_DATE) values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

		stmtInsertNegotiation.setLong(1, negotiation.getProductId());
		stmtInsertNegotiation.setLong(2, negotiation.getUserId());
		stmtInsertNegotiation.setInt(3, negotiation.getStatus());
		stmtInsertNegotiation.setDate(4, new java.sql.Date(negotiation.getStartDate().getTime()));
		stmtInsertNegotiation.executeUpdate();

		ResultSet rs = stmtInsertNegotiation.getGeneratedKeys();
		if (rs.next()) {
			negotiation.setId(rs.getLong(1));
		}

		stmtInsertNegotiation.close();
	}

	public void updateNegotiation(Negotiation negotiation) throws SQLException {
		PreparedStatement stmtUpdateNegotiation = ConnectionManager.getConnection().prepareStatement("update NEGOTIATION set STATUS = ? where ID = ?");

		stmtUpdateNegotiation.setInt(1, negotiation.getStatus());
		stmtUpdateNegotiation.setLong(2, negotiation.getId());
		stmtUpdateNegotiation.executeUpdate();

		stmtUpdateNegotiation.close();
	}

	public void updateProduct(Product product) throws SQLException {
		PreparedStatement stmtUpdateProduct = ConnectionManager.getConnection().prepareStatement("update PRODUCT set STATUS = ? where ID = ?");

		stmtUpdateProduct.setInt(1, product.getStatus());
		stmtUpdateProduct.setLong(2, product.getId());
		stmtUpdateProduct.executeUpdate();

		stmtUpdateProduct.close();
	}

	public void insertNegotiationHistory(NegotiationHistory negotiationHistory) throws SQLException {
		PreparedStatement stmtInsertNegotiationHistory = ConnectionManager.getConnection().prepareStatement(
				"insert into NEGOTIATION_HISTORY (NEGOTIATION_ID,SELLER_PRICE,BUYER_PRICE,OP_DATE) values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

		stmtInsertNegotiationHistory.setLong(1, negotiationHistory.getNegotiationId());
		stmtInsertNegotiationHistory.setDouble(2, negotiationHistory.getSellerPrice());
		if (negotiationHistory.getBuyerPrice() == null) {
			stmtInsertNegotiationHistory.setNull(3, java.sql.Types.NULL);
		} else {
			stmtInsertNegotiationHistory.setDouble(3, negotiationHistory.getBuyerPrice());
		}
		stmtInsertNegotiationHistory.setDate(4, new java.sql.Date(negotiationHistory.getDate().getTime()));
		stmtInsertNegotiationHistory.executeUpdate();

		ResultSet rs = stmtInsertNegotiationHistory.getGeneratedKeys();
		if (rs.next()) {
			negotiationHistory.setId(rs.getLong(1));
		}

		stmtInsertNegotiationHistory.close();
	}

	public void updateNegotiationHistory(NegotiationHistory negotiationHistory) throws SQLException {
		PreparedStatement stmtUpdateNegotiationHistory = ConnectionManager.getConnection().prepareStatement(
				"UPDATE NEGOTIATION_HISTORY set SELLER_PRICE = ?, BUYER_PRICE = ? WHERE ID = ?", Statement.RETURN_GENERATED_KEYS);

		stmtUpdateNegotiationHistory.setDouble(1, negotiationHistory.getSellerPrice());
		stmtUpdateNegotiationHistory.setDouble(2, negotiationHistory.getBuyerPrice());
		stmtUpdateNegotiationHistory.setLong(3, negotiationHistory.getId());
		stmtUpdateNegotiationHistory.executeUpdate();

		stmtUpdateNegotiationHistory.close();
	}

	public Negotiation findNegotiationOfBuyer(Long productId, Long buyerId) throws SQLException {
		PreparedStatement stmtFindNegotiationOfBuyer = ConnectionManager.getConnection().prepareStatement("SELECT * FROM NEGOTIATION WHERE PRODUCT_ID = ? AND USER_ID = ?");
		stmtFindNegotiationOfBuyer.setLong(1, productId);
		stmtFindNegotiationOfBuyer.setLong(2, buyerId);
		ResultSet resultSet = stmtFindNegotiationOfBuyer.executeQuery();

		Negotiation negotiation = null;
		if (resultSet.next()) {
			negotiation = convertResultSetToNegotiation(resultSet);
		}

		stmtFindNegotiationOfBuyer.close();
		return negotiation;
	}

	public Negotiation findNegotiationById(Long id) throws SQLException {
		PreparedStatement stmtFindNegotiationById = ConnectionManager.getConnection().prepareStatement("SELECT * FROM NEGOTIATION WHERE ID = ?");

		stmtFindNegotiationById.setLong(1, id);
		ResultSet resultSet = stmtFindNegotiationById.executeQuery();

		Negotiation negotiation = null;
		if (resultSet.next()) {
			negotiation = convertResultSetToNegotiation(resultSet);
		}

		stmtFindNegotiationById.close();
		return negotiation;
	}

	public List<NegotiationHistory> findHistoryOfNegotiation(Long negotiationId) throws SQLException {
		PreparedStatement stmtFindNegotiationHistory = ConnectionManager.getConnection().prepareStatement(
				"SELECT * FROM NEGOTIATION_HISTORY WHERE NEGOTIATION_ID = ? ORDER BY OP_DATE ASC");

		stmtFindNegotiationHistory.setLong(1, negotiationId);
		ResultSet resultSet = stmtFindNegotiationHistory.executeQuery();

		List<NegotiationHistory> history = new ArrayList<NegotiationHistory>();
		while (resultSet.next()) {
			history.add(convertResultSetToNegotiationHistory(resultSet));
		}

		stmtFindNegotiationHistory.close();
		return history;
	}

	private NegotiationHistory convertResultSetToNegotiationHistory(ResultSet rs) throws SQLException {
		NegotiationHistory history = new NegotiationHistory();
		Double buyerPrice = rs.getObject("BUYER_PRICE") != null ? rs.getDouble("BUYER_PRICE") : null;
		history.setBuyerPrice(buyerPrice);
		history.setDate(rs.getDate("OP_DATE"));
		history.setId(rs.getLong("ID"));
		history.setNegotiationId(rs.getLong("NEGOTIATION_ID"));
		history.setSellerPrice(rs.getDouble("SELLER_PRICE"));
		return history;
	}

	private Negotiation convertResultSetToNegotiation(ResultSet rs) throws SQLException {
		Negotiation n = new Negotiation();
		n.setId(rs.getLong("ID"));
		n.setProductId(rs.getLong("PRODUCT_ID"));
		n.setStartDate(rs.getDate("START_DATE"));
		n.setStatus(rs.getInt("STATUS"));
		n.setUserId(rs.getLong("USER_ID"));
		return n;
	}

	public List<Product> getProducts() throws SQLException {
		Statement statement = ConnectionManager.getConnection().createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCT");

		List<Product> products = new ArrayList<Product>();
		while (resultSet.next()) {
			products.add(convertResultSetToProduct(resultSet));
		}

		statement.close();
		return products;
	}

	private Product convertResultSetToProduct(ResultSet rs) throws SQLException {
		Product p = new Product();
		p.setId(rs.getLong("id"));
		p.setTitle(rs.getString("title"));
		p.setInfo(rs.getString("info"));
		p.setPictureName(rs.getString("picture"));
		p.setPrice(rs.getDouble("price"));
		p.setSellerId(rs.getLong("seller_id"));
		p.setStatus(rs.getInt("status"));
		// TODO fill other fields...
		return p;
	}

	public User getUser(String email, String pass) throws SQLException {
		Statement statement = ConnectionManager.getConnection().createStatement();
		ResultSet resultSet = statement.executeQuery("select * from users where email='" + email + "' and pass='" + pass + "'");
		User user = null;
		if (resultSet.next()) {
			user = convertResultSetToUser(resultSet);
		}
		statement.close();
		return user;
	}

	public User getUserById(Long id) throws SQLException {
		PreparedStatement statement = ConnectionManager.getConnection().prepareStatement("select * from users where id = ?");
		statement.setLong(1, id);
		ResultSet resultSet = statement.executeQuery();
		User user = null;
		if (resultSet.next()) {
			user = convertResultSetToUser(resultSet);
		}
		statement.close();
		return user;
	}

	private User convertResultSetToUser(ResultSet resultSet) throws SQLException {
		User user = new User();
		user.setFirstName(resultSet.getString("first_name"));
		user.setLastName(resultSet.getString("last_name"));
		user.setId(resultSet.getLong("id"));
		user.setDate(resultSet.getDate("regdate"));
		user.setMail(resultSet.getString("email"));
		user.setPassword(resultSet.getString("pass"));
		return user;
	}

	public User getUserByEmail(String email) throws SQLException {
		Statement statement = ConnectionManager.getConnection().createStatement();
		ResultSet resultSet = statement.executeQuery("select * from users where email='" + email + "'");
		User user = null;
		if (resultSet.next()) {
			user = convertResultSetToUser(resultSet);
		}
		statement.close();
		return user;
	}

	public void insertUser(String fname, String lname, String email, String passwordHash) throws SQLException {
		PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement("insert into users (first_name, last_name, email, pass, regdate) values(?, ?, ?, ?, ?)");

		Date now = new Date();
		stmt.setString(1, fname);
		stmt.setString(2, lname);
		stmt.setString(3, email);
		stmt.setString(4, passwordHash);
		stmt.setDate(5, new java.sql.Date(now.getTime()));

		stmt.executeUpdate();
		stmt.close();
	}

	public void insertProduct(Product product) throws SQLException {
		PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(
				"insert into product (title, info, price, create_date, status, seller_id, picture) values(?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		Date now = new Date();
		stmt.setString(1, product.getTitle());
		stmt.setString(2, product.getInfo());
		stmt.setDouble(3, product.getPrice());
		stmt.setDate(4, new java.sql.Date(now.getTime()));
		stmt.setInt(5, Product.STATUS_OPEN);
		stmt.setLong(6, product.getSellerId());
		stmt.setString(7, product.getPictureName());

		stmt.executeUpdate();

		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next()) {
			product.setId(rs.getLong(1));
		}

		stmt.close();
	}

	public void insertProductPhoto(Long productId, InputStream photoStream) throws SQLException {
		PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement("insert into PRODUCT_PHOTO (PRODUCT_ID,PHOTO) values (?,?)");
		stmt.setLong(1, productId);
		stmt.setBinaryStream(2, photoStream);
		stmt.executeUpdate();
	}

}
