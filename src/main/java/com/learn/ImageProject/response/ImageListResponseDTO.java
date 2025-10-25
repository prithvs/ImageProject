package com.learn.ImageProject.response;

import java.util.List;
import java.io.Serializable;

public class ImageListResponseDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private List<ImageDetailsDTO> images;
	private String message;
		
	public ImageListResponseDTO(List<ImageDetailsDTO> images, String message) {
		this.images = images;
		this.message = message;
	}
	
	
	
	public ImageListResponseDTO(String message) {
		this.message = message;
	}
	
	public ImageListResponseDTO() {
		
	}


	public List<ImageDetailsDTO> getImages() {
		return images;
	}
	public void setImages(List<ImageDetailsDTO> images) {
		this.images = images;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
