package com.learn.ImageProject.response;

import java.io.Serializable;
import java.time.OffsetDateTime;

public class ImageDetailsDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private int imageId;
	private boolean isSecure;
	private String imageFormat;
	public int imageSize;
	public OffsetDateTime uploadDate;
	

	public ImageDetailsDTO() {
		super();
	}
	public String getImageFormat() {
		return imageFormat;
	}
	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}

	
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public boolean isSecure() {
		return isSecure;
	}
	public void setSecure(boolean isSecure) {
		this.isSecure = isSecure;
	}
	public int getImageSize() {
		return imageSize;
	}
	public void setImageSize(int imageSize) {
		this.imageSize = imageSize;
	}
	public OffsetDateTime getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(OffsetDateTime uploadDate) {
		this.uploadDate = uploadDate;
	}
	
}