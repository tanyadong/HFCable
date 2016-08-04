package com.hbhongfei.hfcable.pojo;

import java.util.ArrayList;

/**
 * Product entity. @author MyEclipse Persistence Tools
 */

public class Product implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String productName;
	private String typeName;
	private Double price;
	private String specifications;
	private String model;
	private String lineCoreType;
	private String detail;
	private ArrayList<String> productImages;

	// Constructors

	/** default constructor */
	public Product() {
	}


	public Product(String id, String productName, String typeName, Double price, String specifications, String model, String lineCoreType, String detail, ArrayList<String> productImages) {
		this.id = id;
		this.productName = productName;
		this.typeName = typeName;
		this.price = price;
		this.specifications = specifications;
		this.model = model;
		this.lineCoreType = lineCoreType;
		this.detail = detail;
		this.productImages = productImages;
	}

	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}



	public String getProdectName() {
		return this.productName;
	}

	public void setProdectName(String prodectName) {
		this.productName = prodectName;
	}


	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public ArrayList<String> getProductImages() {
		return productImages;
	}

	public void setProductImages(ArrayList<String> productImages) {
		this.productImages = productImages;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getSpecifications() {
		return this.specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getLineCoreType() {
		return this.lineCoreType;
	}

	public void setLineCoreType(String lineCoreType) {
		this.lineCoreType = lineCoreType;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", type=" + ", productName="
				+ productName + ", price=" + price + ", specifications="
				+ specifications + ", model=" + model + ", lineCoreType="
				+ lineCoreType + ", detail=" + detail + ", productImages="
				+ productImages + "]";
	}




}