package com.boun.cmpe473.model;

import java.util.Date;

public class Product {
	public static final int STATUS_OPEN = 0;
	public static final int STATUS_SOLD = 1;

	private Long id;
	private String title;
	private String info;
	private Double price;
	private Date date;
	private Integer status;
	private Long sellerId;
	private String pictureName;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Product [id=");
		builder.append(id);
		builder.append(", title=");
		builder.append(title);
		builder.append(", info=");
		builder.append(info);
		builder.append(", price=");
		builder.append(price);
		builder.append(", date=");
		builder.append(date);
		builder.append(", status=");
		builder.append(status);
		builder.append(", sellerId=");
		builder.append(sellerId);
		builder.append(", pictureName=");
		builder.append(pictureName);
		builder.append("]");
		return builder.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getPictureName() {
		return pictureName;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}

}
