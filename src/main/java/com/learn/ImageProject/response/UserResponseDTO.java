package com.learn.ImageProject.response;

import java.io.Serializable;

public class UserResponseDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	
	private String status;
	private String message;
	
	
	public UserResponseDTO(String message) {
		this.message = message;
	}	
	
	public UserResponseDTO(String status, String message) {
		this.status = status;
		this.message = message;		
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
		
}
