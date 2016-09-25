package com.hbhongfei.hfcable.pojo;

import java.io.Serializable;

/**
 * 购物车表
 * @author 谭亚东
 *
 */
public class ShoppingCart  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id;
	public int quantity;//数量
	public Product product; //产品
	public User user; //用户
	public String packages; //包装方式
	public String color; //颜色
	public Double unitPrice; //单价
	public int isOrder; //是否加入订单  0未加入，1已经加入
	
	
	public ShoppingCart(String id, int quantity, Product product, User user,
			String packages, String color,Double unitPrice,int isOrder) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.product = product;
		this.user = user;
		this.packages = packages;
		this.color = color;
		this.unitPrice=unitPrice;
		this.isOrder=isOrder;
	}


	public ShoppingCart() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	public String getPackages() {
		return packages;
	}


	public void setPackages(String packages) {
		this.packages = packages;
	}

	public String getColor() {
		return color;
	}


	public void setColor(String color) {
		this.color = color;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}


	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	//是否加入订单
	public int getIsOrder() {
		return isOrder;
	}


	public void setIsOrder(int isOrder) {
		this.isOrder = isOrder;
	}


	@Override
	public String toString() {
		return "ShoppingCart [id=" + id + ", quantity=" + quantity
				+ ", product=" + product + ", user=" + user + ", packages="
				+ packages + ", color=" + color + ", unitPrice=" + unitPrice
				+ ", isOrder=" + isOrder + "]";
	}

}
