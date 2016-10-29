package com.hbhongfei.hfcable.pojo;

import java.io.Serializable;


/**
 * 物流信息
 */
public class Logistics implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id;
	public String logisticsCompanyName;// 物流公司名称
	public String logisticsNumber; // 物流单号
	public Long logisticsTime;// 发货日期
	public String logisticsCompanyNameCode;//物流公司编码

	public Logistics() {
		super();
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogisticsCompanyName() {
		return logisticsCompanyName;
	}

	public void setLogisticsCompanyName(String logisticsCompanyName) {
		this.logisticsCompanyName = logisticsCompanyName;
	}

	public String getLogisticsNumber() {
		return logisticsNumber;
	}

	public void setLogisticsNumber(String logisticsNumber) {
		this.logisticsNumber = logisticsNumber;
	}

	public String getLogisticsCompanyNameCode() {
		return logisticsCompanyNameCode;
	}

	public void setLogisticsCompanyNameCode(String logisticsCompanyNameCode) {
		this.logisticsCompanyNameCode = logisticsCompanyNameCode;
	}

	@Override
	public String toString() {
		return "Logistics{" +
				"id='" + id + '\'' +
				", logisticsCompanyName='" + logisticsCompanyName + '\'' +
				", logisticsNumber='" + logisticsNumber + '\'' +
				", logisticsTime=" + logisticsTime +
				", logisticsCompanyNameCode='" + logisticsCompanyNameCode + '\'' +
				'}';
	}
}
