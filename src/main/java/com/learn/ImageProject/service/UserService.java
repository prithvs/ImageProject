package com.learn.ImageProject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.learn.ImageProject.dao.UserDAO;
import com.learn.ImageProject.model.User;
import com.learn.ImageProject.response.DashboardResponseDTO;
import com.learn.ImageProject.response.ProfileResponseDTO;
import com.learn.ImageProject.response.UserResponseDTO;

@Service
public class UserService {
	private final UserDAO userDAO;
	public UserService(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	public List<User> listUsers(){
		return userDAO.findAll();
	}
	
	 public boolean createUser(User user) {
	        return userDAO.createUser(user);
	 }
	 
	 public boolean isUsernameTaken(String username) {
		 return userDAO.existsByUsername(username);
	 }
	 
	 public boolean isPasswordCorrect(User user) {
		 return userDAO.checkPassword(user);
	 }
	 
	 
	 public boolean setLoginHistory(User user, String status) {
		 return userDAO.setLoginHistory(user, status);
	 }
	 
	 public DashboardResponseDTO getDashboardData(String username) {
		 return userDAO.getDashboardInfo(username);
	 }
	 
	 public ProfileResponseDTO getProfileData(String username) {
		 return userDAO.getProfileInfo(username);
	 }
	 
	 public boolean editProfileData(User user) {
		 return userDAO.updateProfileInfo(user);
	 }
	 
	 public UserResponseDTO editPasswordData(User user) {
		 return userDAO.updatePassword(user);
	 }
	 
}
