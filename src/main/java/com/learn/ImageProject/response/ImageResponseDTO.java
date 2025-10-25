package com.learn.ImageProject.response;

import java.io.Serializable;

public class ImageResponseDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	public ImageResponseDTO(String message) {
		this.message = message;
	}

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
