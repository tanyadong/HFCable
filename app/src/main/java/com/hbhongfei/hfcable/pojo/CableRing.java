package com.hbhongfei.hfcable.pojo;

import java.util.ArrayList;
import java.util.Date;


/**
 * Shaft entity. @author MyEclipse Persistence Tools
 */
public class CableRing implements java.io.Serializable {
	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String content;// 说说内容
	private User user;// 创建的用户
	private Long createTime;// 创建时间
	private Long updateTime;// 修改时间
	private ArrayList<String> cableRingImages = new ArrayList<String>();

	/** default constructor */
	public CableRing() {
	}

	/** full constructor */
	public CableRing(String id, String content, User user, Long createTime, Long updateTime, ArrayList<String> cableRingImages) {
		super();
		this.id = id;
		this.content = content;
		this.user = user;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.cableRingImages = cableRingImages;
	}

	// Property accessors
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public ArrayList<String> getCableRingImages() {
		return cableRingImages;
	}

	public void setCableRingImages(ArrayList<String> cableRingImages) {
		this.cableRingImages = cableRingImages;
	}

	@Override
	public String toString() {
		return "CableRing [id=" + id + ", content=" + content + ", user=" + user + ", createTime=" + createTime + ", updateTime=" + updateTime + ", cableRingImages=" + cableRingImages + "]";
	}

}
