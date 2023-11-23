package com.ssafy.cafe.model.dto;

import java.util.Date;

public class Note {
	private Integer id;
	private String title;
	private String content;
	private Date orderTime;
	
	public Note() {}
	
	public Note(String title, String content, String senderId, String receiverId) {
		this.title = title;
		this.content = content;
		this.senderId = senderId;
		this.receiverId = receiverId;
	}
	
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	private String senderId;
	private String receiverId;
	private boolean isRead;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	
}
