package com.hbhongfei.hfcable.pojo;

import java.util.Date;


/**
 * Shaft entity. @author MyEclipse Persistence Tools
 */
public class Comment implements java.io.Serializable {
	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private User user;// 评论者
	private String commentContent;// 评论的内容
	private Date createTime;// 评论的时间
	private CableRing cableRing;// 评论的主体

	/** default constructor */
	public Comment() {
	}

	/** full constructor */
	public Comment(String id, User user, String commentContent, Date createTime, CableRing cableRing) {
		super();
		this.id = id;
		this.user = user;
		this.commentContent = commentContent;
		this.createTime = createTime;
		this.cableRing = cableRing;
	}

	// Property accessors
	public String getId() {
		return id;
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

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public CableRing getCableRing() {
		return cableRing;
	}

	public void setCableRing(CableRing cableRing) {
		this.cableRing = cableRing;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", user=" + user + ", commentContent=" + commentContent + ", createTime=" + createTime + ", cableRing=" + cableRing + "]";
	}

}
