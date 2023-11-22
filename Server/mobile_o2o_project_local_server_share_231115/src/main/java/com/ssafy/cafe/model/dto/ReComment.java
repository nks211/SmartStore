package com.ssafy.cafe.model.dto;

public class ReComment {
	private Integer id;
	private Integer commentId;
	private Integer productId;
	private String comment;
	
	public ReComment() {
		
	}
	
	public ReComment(Integer productId, String comment) {
		this.productId = productId;
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
	
	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
