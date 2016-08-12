package com.hbhongfei.hfcable.entity;

import java.io.Serializable;


/**
 * 收货地址 entity. @author MyEclipse Persistence Tools
 */

public class ShoppingAddress implements Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private User user;
	private String consignee;//收货人
	private String phone;//收货人
	private String localArea; //所在地区
	private String detailAddress;//详细地址
	private int tag;//是否为默认地址   0表示不是默认地址，1表示默认地址
	// Constructors
	
	
	

	public ShoppingAddress() {
		super();
		// TODO Auto-generated constructor stub
	}



	public ShoppingAddress(String id, User user, String consignee,
			String phone, String localArea, String detailAddress, int tag) {
		super();
		this.id = id;
		this.user = user;
		this.consignee = consignee;
		this.phone = phone;
		this.localArea = localArea;
		this.detailAddress = detailAddress;
		this.tag = tag;
	}



	// Property accessors

	public String getId() {
		return this.id;
	}

	

	public void setId(String id) {
		this.id = id;
	}

	
	
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLocalArea() {
		return localArea;
	}

	public void setLocalArea(String localArea) {
		this.localArea = localArea;
	}
	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	
	public int getTag() {
		return tag;
	}



	public void setTag(int tag) {
		this.tag = tag;
	}



	@Override
	public String toString() {
		return "ShoppingAddress [id=" + id + ", user=" + user + ", consignee="
				+ consignee + ", phone=" + phone + ", localArea=" + localArea
				+ ", detailAddress=" + detailAddress + ", tag=" + tag + "]";
	}



	

	

}