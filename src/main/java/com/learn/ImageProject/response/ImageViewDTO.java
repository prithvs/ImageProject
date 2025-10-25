package com.learn.ImageProject.response;

import java.io.Serializable;

public class ImageViewDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private String base64Image;

	public ImageViewDTO(String base64Image) {
		super();
		this.base64Image = base64Image;
	}

	public String getBase64Image() {
		return base64Image;
	}

	public void setBase64Image(String base64Image) {
		this.base64Image = base64Image;
	}


	
}
