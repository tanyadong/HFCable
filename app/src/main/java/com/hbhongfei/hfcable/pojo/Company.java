package com.hbhongfei.hfcable.pojo;

import java.util.ArrayList;


/**
 * Company entity. @author MyEclipse Persistence Tools
 */
public class Company implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String logo;
	private String companyName;
	private String description;
	private String productIntroduction;
	private String address;
	private String telephone;
	private Integer zipCode;
	private String email;
	private ArrayList<String> list; //存放公司图片

	// Constructors

	/** default constructor */
	public Company() {
	}

	/** full constructor */
	public Company(String logo, String companyName, String description,
			String productIntroduction, String address, String telephone,
			Integer zipCode, String email) {
		this.logo = logo;
		this.companyName = companyName;
		this.description = description;
		this.productIntroduction = productIntroduction;
		this.address = address;
		this.telephone = telephone;
		this.zipCode = zipCode;
		this.email = email;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogo() {
		return this.logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProductIntroduction() {
		return this.productIntroduction;
	}

	public void setProductIntroduction(String productIntroduction) {
		this.productIntroduction = productIntroduction;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Integer getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(Integer zipCode) {
		this.zipCode = zipCode;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	/**
	 * 公司图片，方便获取数据
	 */
	
	
	
	@Override
	public String toString() {
		return "Company [id=" + id + ", logo=" + logo + ", companyName=" + companyName + ", description=" + description
				+ ", productIntroduction=" + productIntroduction + ", address=" + address + ", telephone=" + telephone
				+ ", zipCode=" + zipCode + ", email=" + email + "]";
	}

	public ArrayList<String> getList() {
		return list;
	}

	public void setList(ArrayList<String> list) {
		this.list = list;
	}

}