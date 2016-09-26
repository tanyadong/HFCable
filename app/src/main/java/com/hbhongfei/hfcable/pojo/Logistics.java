package com.hbhongfei.hfcable.pojo;

import java.io.Serializable;
import java.util.Date;


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
	public Date logisticsTime;// 发货日期

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

	public Date getLogisticsTime() {
		return logisticsTime;
	}

	public void setLogisticsTime(Date logisticsTime) {
		this.logisticsTime = logisticsTime;
	}

	@Override
	public String toString() {
		return "Logistics [id=" + id + ", logisticsCompanyName=" + logisticsCompanyName + ", logisticsNumber=" + logisticsNumber + ", logisticsTime=" + logisticsTime + "]";
	}

}
