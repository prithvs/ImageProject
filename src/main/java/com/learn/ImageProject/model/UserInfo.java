package com.learn.ImageProject.model;

import java.io.Serializable;

public class UserInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	private String username;
	private int token;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getToken() {
		return token;
	}
	public void setToken(int token) {
		this.token = token;
	}
	
}
