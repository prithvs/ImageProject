package com.learn.ImageProject.response;

import java.io.Serializable;

public class ProfileResponseDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private String status;
	private String username;
	private String first_name;
	private String last_name;
	private String user_type;
	private String email;
	private String home_address;
	private String message;
	public ProfileResponseDTO(String status, String username, String first_name, String last_name, String user_type, String email,
			String home_address, String message) {
		this.status = status;
		this.username = username;
		this.first_name = first_name;
		this.last_name = last_name;
		this.user_type = user_type;
		this.email = email;
		this.home_address = home_address;
		this.message = message;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ProfileResponseDTO(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHome_address() {
		return home_address;
	}
	public void setHome_address(String home_address) {
		this.home_address = home_address;
	}
	
}
