package com.learn.ImageProject.response;

import java.io.Serializable;


public class DashboardResponseDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	private String email;
	private String username;
	private String message;
	
	public DashboardResponseDTO(String firstName, String lastName, String email, String username, String message){
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.message = message;
	}
	
	public DashboardResponseDTO(String message){
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}
