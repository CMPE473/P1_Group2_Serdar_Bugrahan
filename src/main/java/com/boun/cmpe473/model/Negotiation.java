package com.boun.cmpe473.model;

import java.io.Serializable;
import java.util.Date;

public class Negotiation implements Serializable {
	public static final long serialVersionUID = 1L;
	public static final int STATUS_SOLD = 0;
	public static final int STATUS_WAIT_SELLER = 1;
	public static final int STATUS_WAIT_BUYER = 2;
	public static final int STATUS_CANCELLED = 3;

	private Long id;
	private Long productId;
	private Long userId;
	private Integer status;
	private Date startDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}
