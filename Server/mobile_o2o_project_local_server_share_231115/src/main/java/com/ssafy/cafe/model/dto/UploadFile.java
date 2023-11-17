package com.ssafy.cafe.model.dto;

public class UploadFile {
	private String originImgName;
	private String storeImgName;
	
	public UploadFile(String originImgName, String storeImgName) {
		super();
		this.originImgName = originImgName;
		this.storeImgName = storeImgName;
	}

	public String getStoreImgName() {
		return storeImgName;
	}

	public void setStoreImgName(String storeImgName) {
		this.storeImgName = storeImgName;
	}
	
	public String getOriginImgName() {
		return originImgName;
	}

	public void setOriginImgName(String originImgName) {
		this.originImgName = originImgName;
	}
	
	 
}
