package com.learn.ImageProject.model;

import java.io.Serializable;
import java.time.OffsetDateTime;

public class Image implements Serializable{
	private static final long serialVersionUID = 1L;

	private int imageId;
	private int userId;
	private String imagePath;
	private boolean isSecure;
	private String imageFormat;
	public int imageSize;
	public OffsetDateTime uploadDate;
	private String username;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
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
