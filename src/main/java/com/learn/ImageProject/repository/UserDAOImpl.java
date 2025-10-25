package com.learn.ImageProject.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.learn.ImageProject.UsersController;
import com.learn.ImageProject.dao.UserDAO;
import com.learn.ImageProject.model.User;
import com.learn.ImageProject.response.DashboardResponseDTO;
import com.learn.ImageProject.response.ProfileResponseDTO;
import com.learn.ImageProject.response.UserResponseDTO;
import com.learn.ImageProject.util.HashingTokenUtil;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


// Query Strings in static class or declaring as static final variable



@Repository
public class UserDAOImpl implements UserDAO{
	private final DataSource dataSource;

	private final static String FIND_ALL_USERS_QUERY = "SELECT users.user_id, users.username, users.first_name, users.last_name, users.user_type, users.email,"+ 
			" users.home_address, login.password FROM users INNER JOIN login ON users.user_id = login.user_id;";
	
	private final static String CREATE_USER_STATEMENT = "INSERT INTO users(username, first_name, last_name, user_type, email, home_address) VALUES (?, ?, ?, ?, ?, ?);";
	
	private final static String CREATE_LOGIN_FOR_USER_STATEMENT = "INSERT INTO login(user_id, password) VALUES(?,?);";

	private final static String FIND_USER_BY_USERNAME_QUERY = "SELECT COUNT(*) FROM users WHERE username = ?";
	
	private final static String CHECK_PASSWORD_QUERY = "SELECT login.password FROM users INNER JOIN login ON login.user_id = users.user_id AND users.username = ?;";
    
	private final static String SET_LOGIN_HISTORY_STATEMENT = "INSERT INTO login_history(user_id, login_timestamp, status) VALUES(?,?,?);"; 
	
	private final static String GET_DASHBOARD_INFO_QUERY = "SELECT first_name, last_name, email FROM users WHERE users.username = ?;";
	
	private final static String GET_PROFILE_INFO_QUERY = "SELECT first_name, last_name, user_type, email, home_address FROM users WHERE users.username = ?;";
	
	private final static String UPDATE_PROFILE_STATEMENT = "UPDATE users SET first_name = ?, last_name = ?, email = ?, home_address = ? WHERE users.username = ?";
	
	private final static String UPDATE_PASSWORD_STATEMENT = "UPDATE login SET password = ? WHERE user_id = ?";
	
	private final static String GET_USER_ID_QUERY = "SELECT user_id FROM users WHERE users.username = ?;";

	
	private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);
	
	@Autowired
	public UserDAOImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public List<User> findAll() {
		logger.debug("Entered findAll");
		ArrayList<User> users = new ArrayList<User>();
		try (Connection connection = dataSource.getConnection();
				Statement selectStmt = connection.createStatement();
				ResultSet rs = selectStmt.executeQuery(FIND_ALL_USERS_QUERY);) {
			while (rs.next()) {
				User user = new User();
				user.setUserId(rs.getLong("user_id"));
				user.setUsername(rs.getString("username"));
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
				user.setEmail(rs.getString("email"));
				user.setUserType(rs.getString("user_type"));
				user.setHomeAddress(rs.getString("home_address"));
				user.setPassword(rs.getString("password"));
				users.add(user);
			}
		}catch(Exception e) {
			logger.error("Query unsuccessful: User list not found");
			e.printStackTrace();
		}
		logger.info("Query successful: User list returned");
		return users;
	}
	
	public boolean createUser(User user) {	
		logger.debug("Entered createUser");
		try (Connection conn = dataSource.getConnection();
	             PreparedStatement stmnt = conn.prepareStatement(CREATE_USER_STATEMENT)) {
			
			stmnt.setString(1, user.getUsername());
			stmnt.setString(2, user.getFirstName());
			stmnt.setString(3, user.getLastName());
			stmnt.setString(4, "USER");
			stmnt.setString(5, user.getEmail());
			stmnt.setString(6, user.getHomeAddress());
			stmnt.executeUpdate();
			
		}catch(Exception e) {
			logger.error("Query unsuccessful: User was not created");
			e.printStackTrace();
			return false;
		}
		
		
		long user_id = getUserId(user);
		user.setUserId(user_id);
		
		logger.info("Query successful: User created");
		return createLoginForUser(user);
		
	}
	
	public boolean existsByUsername(String username) {
		logger.debug("Entered existsByUsername");
		try (Connection conn = dataSource.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(FIND_USER_BY_USERNAME_QUERY)) {
			 stmt.setString(1, username);
			 try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    int count = rs.getInt(1);
	                    logger.info("Query successful: User "+((count>0)?"found":"not found"));
	                    return count > 0;
	                }
	            }
		}catch(Exception e) {
			logger.error("Query unsuccessful: Username check has failed");
			e.printStackTrace();
		}
		return false;
	}
	
	
	public boolean checkPassword(User user) {
		logger.debug("Entered checkPassword");
		boolean outcome = false;
		try (Connection conn = dataSource.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(CHECK_PASSWORD_QUERY)) {
			stmt.setString(1, user.getUsername());
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
                    String savedHashPassword = rs.getString(1);
                    if(HashingTokenUtil.hashPassword(user.getPassword()).equals(savedHashPassword)) {
                    		outcome = true;
                    }else {
                    	outcome = false;
                    }
                }
			}
		}catch(Exception e) {
			logger.error("Query unsuccessful: Password check has failed");
			e.printStackTrace();
		}
		logger.info("Query successful: User password "+(outcome?"matched":"didn't match"));
		return outcome;
	}	
	
	public boolean setLoginHistory(User user, String status) {
		logger.debug("Entered setLoginHistory");
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC); 	
	    long user_id = 0;
	    user_id = getUserId(user);
	    try (Connection conn = dataSource.getConnection();
            PreparedStatement stmnt = conn.prepareStatement(SET_LOGIN_HISTORY_STATEMENT)) {
    	 		stmnt.setLong(1, user_id);
    	 		stmnt.setObject(2, now);
    	 		stmnt.setString(3, status);
    	 		stmnt.executeUpdate();
    	 		logger.info("Query successful: Login History is stored");
	    	 	return true;
	    }catch(Exception e) {
	    		logger.error("Query unsuccessful: Login History is not stored");
	    	 	e.printStackTrace();
	    	 	return false;
	    }
	}

	
	public DashboardResponseDTO getDashboardInfo(String username) {
		logger.debug("Entered getDashboardInfo");
		try (Connection conn = dataSource.getConnection();
	             PreparedStatement stmnt = conn.prepareStatement(GET_DASHBOARD_INFO_QUERY)) {
			stmnt.setString(1,username);
			
			try (ResultSet rs = stmnt.executeQuery()) {
				if (rs.next()) {
					String firstName = rs.getString(1);
					String lastName = rs.getString(2);
					String email = rs.getString(3);
					logger.info("Query successful: Dashboard data is returned");
					return new DashboardResponseDTO(firstName, lastName, email, username, 
							"Dashboard Data Returned Successfully!");
				}
				
			}
			
		}catch(Exception e) {
			logger.error("Query unsuccessful: Dashboard data is not returned");
			e.printStackTrace();
			return new DashboardResponseDTO("Couldn't access Dashboard Data!");
		}
		logger.info("Executed Query: No user found with given username");
		return new DashboardResponseDTO("No user found with username: " + username);
	}
	
	
	public ProfileResponseDTO getProfileInfo(String username) {
		logger.debug("Entered getProfileInfo");
		try (Connection conn = dataSource.getConnection();
			PreparedStatement stmnt = conn.prepareStatement(GET_PROFILE_INFO_QUERY)) {
			
			stmnt.setString(1,username);
			
			try (ResultSet rs = stmnt.executeQuery()) {
				if (rs.next()) {
					
					String firstName = rs.getString(1);
					String lastName = rs.getString(2);
					String userType = rs.getString(3);
					String email = rs.getString(4);
					String homeAddress = rs.getString(5);
					logger.info("Query successful: Profile data is returned");
					return new ProfileResponseDTO("SUCCESS", username, firstName, lastName, userType, email, homeAddress, 
							"Profile Data Returned Successfully!");
					
				}
			
			}
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("Query unsuccessful: Profile data is not returned");
			return new ProfileResponseDTO("Couldn't access Profile Data!");
		}
		logger.info("Executed Query: No user found with given username");
		return new ProfileResponseDTO("No user found with username: "+username);
	}
	
	
	
	
	public boolean updateProfileInfo(User user) {
		logger.debug("Entered updateProfileInfo");
		try (Connection conn = dataSource.getConnection();
	             PreparedStatement stmnt = conn.prepareStatement(UPDATE_PROFILE_STATEMENT)) {
			
			stmnt.setString(1,user.getFirstName());
			stmnt.setString(2,user.getLastName());
			stmnt.setString(3,user.getEmail());
			stmnt.setString(4,user.getHomeAddress());
			stmnt.setString(5,user.getUsername());
			stmnt.executeUpdate();
			logger.info("Query successful: Profile data is edited");
			return true;
			
		}catch(Exception e) {
			logger.error("Query unsuccessful: Profile data is not edited");
			e.printStackTrace();
			return false;
		}
	}
	
	public UserResponseDTO updatePassword(User user) {
		logger.debug("Entered updatePassword");
		long userId = getUserId(user);
		try (Connection conn = dataSource.getConnection();
	             PreparedStatement stmnt = conn.prepareStatement(UPDATE_PASSWORD_STATEMENT)) {
			stmnt.setString(1,HashingTokenUtil.hashPassword(user.getPassword()));
			stmnt.setLong(2, userId);
			stmnt.executeUpdate();
			logger.info("Query successful: Password is updated");
			return new UserResponseDTO("SUCCESS", "User Password Edited Successfully!"); 
		}catch(Exception e) {
			logger.error("Query unsuccessful: Profile data is not edited");
			e.printStackTrace();
			
			return new UserResponseDTO("ERROR", "Some error has been encountered while updating password!");
		}
			
	}
	
	
	
	
	private long getUserId(User user) {
		logger.debug("Entered updatePassword");
		long user_id = 0;
		try (Connection conn = dataSource.getConnection();
	             PreparedStatement stmnt = conn.prepareStatement(GET_USER_ID_QUERY)) {
			stmnt.setString(1,user.getUsername());
			
			try (ResultSet rs = stmnt.executeQuery()) {
				if (rs.next()) {
					user_id = rs.getInt(1);
				}
				logger.info("Query Successful: User ID is returned");
				return user_id;
			}
			
		}catch(Exception e) {
			logger.error("Query Unsuccessful: User not found");
			e.printStackTrace();
			return -1;
		}
		
	}
	
	
	private boolean createLoginForUser(User user) {	
		logger.debug("Entered createLoginForUser");
		try (Connection conn = dataSource.getConnection();
	             PreparedStatement stmnt = conn.prepareStatement(CREATE_LOGIN_FOR_USER_STATEMENT)) {
				stmnt.setLong(1, user.getUserId());
				stmnt.setString(2, HashingTokenUtil.hashPassword(user.getPassword()));
				stmnt.executeUpdate();
				logger.info("Query Successful: User login data is stored");
				return true;
		}catch(Exception e) {
			logger.error("Query Unsuccessful: User login data is not stored");
			e.printStackTrace();
			return false;
		}
		
	}	
		
	
}
