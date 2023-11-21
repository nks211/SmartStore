package com.ssafy.cafe.model.dto;

public class ReComment {
	private Integer id;
	private Integer commentId;
	private String comment;
	
	public ReComment() {
		
	}
	
	public ReComment(Integer commentId, String comment) {
		this.commentId = commentId;
		this.comment = comment;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCommentId() {
		return commentId;
	}
	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
