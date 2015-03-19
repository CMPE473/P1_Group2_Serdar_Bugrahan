package com.boun.cmpe473.model;

import java.io.Serializable;
import java.util.Date;

public class NegotiationHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long negotiationId;
	private Double sellerPrice;
	private Double buyerPrice;
	private Date date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNegotiationId() {
		return negotiationId;
	}

	public void setNegotiationId(Long negotiationId) {
		this.negotiationId = negotiationId;
	}

	public Double getSellerPrice() {
		return sellerPrice;
	}

	public void setSellerPrice(Double sellerPrice) {
		this.sellerPrice = sellerPrice;
	}

	public Double getBuyerPrice() {
		return buyerPrice;
	}

	public void setBuyerPrice(Double buyerPrice) {
		this.buyerPrice = buyerPrice;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
