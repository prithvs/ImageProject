package com.learn.ImageProject.dao;

import java.util.List;

import com.learn.ImageProject.model.User;
import com.learn.ImageProject.response.DashboardResponseDTO;
import com.learn.ImageProject.response.ProfileResponseDTO;
import com.learn.ImageProject.response.UserResponseDTO;

public interface UserDAO {
	List<User> findAll();
	boolean createUser(User user); // turn into boolean
	boolean existsByUsername(String username);
	boolean checkPassword(User user);
	boolean setLoginHistory(User user, String status);
	DashboardResponseDTO getDashboardInfo(String username);
	ProfileResponseDTO getProfileInfo(String username);
	boolean updateProfileInfo(User user);
	UserResponseDTO updatePassword(User user);
}
