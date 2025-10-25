package com.learn.ImageProject.model;

import java.io.Serializable;

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private long userId;
	private String username;
	private String firstName;
	private String lastName;
	private String userType;
	private String email;
	private String homeAddress;
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public User() {
	}
	
	public User(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	private String password;
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String toString() {
        return "User{" +
        			"User ID: " + userId +
                ", Username: " + username +
                ", First Mame: " + firstName +
                ", Last Name: " + lastName +
                ", User Type: " + userType +
                ", Password: " + password +
                "}";
    }
}
