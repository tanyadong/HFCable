package com.hbhongfei.hfcable.pojo;

/**
 * 项目类  entity. @author MyEclipse Persistence Tools
 */

public class Project implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	private String id;
	private String projectName;//项目标题
	private String introduce;//项目介绍
	private String projectImg;//项目图片

	

	// Constructors

	/** default constructor */
	public Project() {
	}
	
	
	public Project(String id, String projectName, String introduce,
			String projectImg) {
		super();
		this.id = id;
		this.projectName = projectName;
		this.introduce = introduce;
		this.projectImg = projectImg;
	}



	public String getId() {
		return this.id;
	}

	
	public void setId(String id) {
		this.id = id;
	}

	


	public String getProjectName() {
		return projectName;
	}


	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getIntroduce() {
		return introduce;
	}


	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getProjectImg() {
		return projectImg;
	}


	public void setProjectImg(String projectImg) {
		this.projectImg = projectImg;
	}


	@Override
	public String toString() {
		return "Project [id=" + id + ", projectName=" + projectName
				+ ", introduce=" + introduce + ", projectImg=" + projectImg
				+ "]";
	}

	



	

}