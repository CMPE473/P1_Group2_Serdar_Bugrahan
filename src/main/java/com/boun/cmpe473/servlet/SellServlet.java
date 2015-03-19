package com.boun.cmpe473.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.parse4j.Parse;
import org.parse4j.ParseFile;

import com.boun.cmpe473.dao.DAO;
import com.boun.cmpe473.model.Product;
import com.boun.cmpe473.model.User;

public class SellServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private DAO dao;

	@Override
	public void init() throws ServletException {
		super.init();
		dao = new DAO();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User currentUser = getLoggedUser(req);
		if (currentUser == null) {
			resp.sendRedirect("/login?redirect=/sell");
			return;
		}

		req.setAttribute("currentUser", currentUser);
		display("/jsp/sell.jsp", req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			User currentUser = getLoggedUser(req);

			if (currentUser == null) {
				resp.sendRedirect("/login?redirect=/sell");
				return;
			}

			HashMap<String, String> parameters = new HashMap<String, String>();
			FileItem stream = getProductImage(req, parameters);

			Parse.initialize("8VYvlfFkn7K7sGpk5HvYOu022sXkeYfijvmyclge", "dJzyXTj2uStEz5nNkKXe3xFYr0cQvHX4bDfiHvlS");

			byte[] data = IOUtils.toByteArray(stream.getInputStream());
			ParseFile file = new ParseFile(generateFileName(stream.getName()), data);
			file.save();

			Product product = new Product();
			product.setTitle(parameters.get("title"));
			product.setInfo(parameters.get("info"));
			product.setPrice(Double.valueOf(parameters.get("price")));
			product.setPictureName(file.getUrl());
			product.setSellerId(currentUser.getId());

			dao.insertProduct(product);

			resp.sendRedirect("/product?productId=" + product.getId());
		} catch (Exception e) {
			throw new ServletException("DB Error occured.", e);
		}
	}

	@SuppressWarnings("unchecked")
	private FileItem getProductImage(HttpServletRequest req, Map<String, String> parameters) throws FileUploadException, IOException, ServletException {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> fields = upload.parseRequest(req);
		Iterator<FileItem> it = fields.iterator();
		FileItem stream = null;

		while (it.hasNext()) {
			FileItem fileItem = it.next();
			boolean isFormField = fileItem.isFormField();
			if (!isFormField) {
				stream = fileItem;
			} else {
				parameters.put(fileItem.getFieldName(), fileItem.getString("UTF-8"));
			}
		}

		return stream;
	}

	private String generateFileName(String orginalName) {
		String filename = RandomStringUtils.randomAlphanumeric(10);
		String ext = StringUtils.substringAfterLast(orginalName, ".");
		return filename + "." + ext;
	}
}
