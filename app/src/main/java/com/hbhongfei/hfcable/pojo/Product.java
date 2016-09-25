package com.hbhongfei.hfcable.pojo;

import java.util.ArrayList;

/**
 * Product entity. @author MyEclipse Persistence Tools
 */

public class Product implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private Double price;//价格
	private String specifications;//规格
	public String introduce;//产品简介

	private TypeTwo typeTwo;//二级种类  型号
	private String 	voltage; //电压
	private String crossSection;//横截面
	private String coreNumber; //芯数
	private String purpose;//用途
	private String applicationRange;//应用范围
	private String outsideDiameter;//平均外径上线
	private String diameterLimit;//外径下限
	private String implementationStandards;//执行标准
	private String conductorMaterial;//导体材质
	private String sheathMaterial;//护套材质
	private String referenceWeight;//参考重量
//	private String length;//长度


	private ArrayList<String> productImages=new ArrayList<String>();

	// Constructors

	/** default constructor */
	public Product() {
	}


	public Product(String id, Double price, String specifications, TypeTwo typeTwo, String voltage, String crossSection, String coreNumber, String purpose, String applicationRange, String outsideDiameter, String diameterLimit, String implementationStandards, String conductorMaterial, String sheathMaterial, String referenceWeight, ArrayList<String> productImages) {
		this.id = id;
		this.price = price;
		this.specifications = specifications;

		this.typeTwo = typeTwo;
		this.voltage = voltage;
		this.crossSection = crossSection;
		this.coreNumber = coreNumber;
		this.purpose = purpose;
		this.applicationRange = applicationRange;
		this.outsideDiameter = outsideDiameter;
		this.diameterLimit = diameterLimit;
		this.implementationStandards = implementationStandards;
		this.conductorMaterial = conductorMaterial;
		this.sheathMaterial = sheathMaterial;
		this.referenceWeight = referenceWeight;
		this.productImages = productImages;
	}

	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
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

	public TypeTwo getTypeTwo() {
		return typeTwo;
	}

	public void setTypeTwo(TypeTwo typeTwo) {
		this.typeTwo = typeTwo;
	}

	public String getVoltage() {
		return voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

	public String getCrossSection() {
		return crossSection;
	}

	public void setCrossSection(String crossSection) {
		this.crossSection = crossSection;
	}

	public String getCoreNumber() {
		return coreNumber;
	}

	public void setCoreNumber(String coreNumber) {
		this.coreNumber = coreNumber;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getApplicationRange() {
		return applicationRange;
	}

	public void setApplicationRange(String applicationRange) {
		this.applicationRange = applicationRange;
	}

	public String getOutsideDiameter() {
		return outsideDiameter;
	}

	public void setOutsideDiameter(String outsideDiameter) {
		this.outsideDiameter = outsideDiameter;
	}

	public String getDiameterLimit() {
		return diameterLimit;
	}

	public void setDiameterLimit(String diameterLimit) {
		this.diameterLimit = diameterLimit;
	}

	public String getImplementationStandards() {
		return implementationStandards;
	}

	public void setImplementationStandards(String implementationStandards) {
		this.implementationStandards = implementationStandards;
	}

	public String getConductorMaterial() {
		return conductorMaterial;
	}

	public void setConductorMaterial(String conductorMaterial) {
		this.conductorMaterial = conductorMaterial;
	}

	public String getSheathMaterial() {
		return sheathMaterial;
	}

	public void setSheathMaterial(String sheathMaterial) {
		this.sheathMaterial = sheathMaterial;
	}

	public String getReferenceWeight() {
		return referenceWeight;
	}

	public void setReferenceWeight(String referenceWeight) {
		this.referenceWeight = referenceWeight;
	}

	@Override
	public String toString() {
		return "Product{" +
				"id='" + id + '\'' +
				", price=" + price +
				", specifications='" + specifications + '\'' +
				", typeTwo=" + typeTwo +
				", voltage='" + voltage + '\'' +
				", crossSection='" + crossSection + '\'' +
				", coreNumber='" + coreNumber + '\'' +
				", purpose='" + purpose + '\'' +
				", applicationRange='" + applicationRange + '\'' +
				", outsideDiameter='" + outsideDiameter + '\'' +
				", diameterLimit='" + diameterLimit + '\'' +
				", implementationStandards='" + implementationStandards + '\'' +
				", conductorMaterial='" + conductorMaterial + '\'' +
				", sheathMaterial='" + sheathMaterial + '\'' +
				", referenceWeight='" + referenceWeight + '\'' +
				", productImages=" + productImages +
				'}';
	}
}