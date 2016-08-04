package com.hbhongfei.hfcable.pojo;

import java.io.Serializable;

/**
 * @author dell1
 *
 */
public class ProductImage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String image;

	private Product product;
	

	

	

	public ProductImage(String id, String image, Product product) {
		super();
		this.id = id;
		this.image = image;
		this.product = product;
	}

	public ProductImage() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "ProductImage [id=" + id + ", image=" + image + ", product="
				+ product + "]";
	}

	



}
