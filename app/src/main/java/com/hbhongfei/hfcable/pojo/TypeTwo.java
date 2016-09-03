package com.hbhongfei.hfcable.pojo;

/**
 * 二级种类
 * 谭亚东
 */
public class TypeTwo implements java.io.Serializable {

	// Fields

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String typeTwoName;

	// Constructors


	public TypeTwo() {
	}

	public TypeTwo(String id, String typeTwoName) {
		this.id = id;
		this.typeTwoName = typeTwoName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTypeTwoName() {
		return typeTwoName;
	}

	public void setTypeTwoName(String typeTwoName) {
		this.typeTwoName = typeTwoName;
	}

	@Override
	public String toString() {
		return "TypeTwo{" +
				"id='" + id + '\'' +
				", typeTwoName='" + typeTwoName + '\'' +
				'}';
	}
}