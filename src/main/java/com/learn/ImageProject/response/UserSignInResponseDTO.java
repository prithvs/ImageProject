package com.learn.ImageProject.response;

import java.io.Serializable;

public class UserSignInResponseDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private String status;
	private String token;
	private String message;
	
	public UserSignInResponseDTO(String status, String token, String message) {
		this.status = status;
		this.token = token;
		this.message = message;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
		
}
