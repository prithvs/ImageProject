package com.learn.ImageProject;

import java.util.Base64;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learn.ImageProject.model.User;
import com.learn.ImageProject.model.UserInfo;
import com.learn.ImageProject.model.UserPassword;
import com.learn.ImageProject.response.DashboardResponseDTO;
import com.learn.ImageProject.response.ImageListResponseDTO;
import com.learn.ImageProject.response.ProfileResponseDTO;
import com.learn.ImageProject.response.UserResponseDTO;
import com.learn.ImageProject.response.UserSignInResponseDTO;
import com.learn.ImageProject.service.UserService;
import com.learn.ImageProject.util.HashingTokenUtil;
import com.learn.ImageProject.util.Rc4Obfuscator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@RestController
public class UsersController {
	private final UserService userService;
	private final static String SUCCESS_RESPONSE = "SUCCESS";
	private final static String ERROR_RESPONSE = "ERROR";
	
	
	
	private static final Logger logger = LogManager.getLogger(UsersController.class);

	 public UsersController(UserService userService) {
	        this.userService = userService;
	 }
	
	@Operation(summary = "Get all users", description = "Retrieve a list of all users")
	@ApiResponse(responseCode = "200", description = "Successfully retrieved list")
	@GetMapping("/users")
	public List<User> getUsers(){
		logger.debug("Entered endpoint /users");
		logger.info("GET request for user list: successful");
		return userService.listUsers();
	}
	
	// Add an API endpoint to use during a call whilst typing into the field to check availability
	
	@Operation(summary = "Checks whether username is taken", description = "Returns whether the username already exists")
	@ApiResponse(responseCode = "200", description = "Successfully checked")
	@GetMapping("/checkUsername")
	
	public String checkUsername(@RequestParam("username") String username) {
		  logger.debug("Entered endpoint /checkUsername");
//        logger.debug("DEBUG level log message");
//        logger.warn("WARN level log message");
//        logger.error("ERROR level log message");

	    if (userService.isUsernameTaken(username)) {
	    	logger.info("Processing GET request for username: username not available");
	        return "Username already taken";
	    }
	    logger.info("Processing GET request for username: username available");
	    return "Username available";
	}
	
	//ResponseEntity inbuilt class, can state the return type of objects
	//
	
	@Operation(summary = "Handles sign up process", description = "Returns whether user sign up is successful")
	@ApiResponse(responseCode = "200", description = "Signed Up successfully!")
	@PostMapping("/signup")
    public String handleSignUpForm(@RequestBody User user) {
		logger.debug("Entered endpoint /signup");
		if(userService.isUsernameTaken(user.getUsername())==true) {
			logger.info("Processing POST request for signup: requested username is already taken");
			return "Username already taken!";
		}
        if(userService.createUser(user)==true) {
        		logger.info("Processing POST request for signup: sign up successful");
        		return "Signed Up successfully!";
        }else {
        		logger.info("Processing POST request for signup: sign up unsuccessful");
        		return "Sign Up unsuccessful";
        }
        
    }

	
	@Operation(summary = "Handles sign in process", description = "Returns whether user sign in is successful")
	@ApiResponse(responseCode = "200", description = "Signed In successfully!")
	@PostMapping("/signin")
	public ResponseEntity<UserSignInResponseDTO> handleSignInForm(@RequestBody User user) {// have a response object as a return type
		logger.debug("Entered endpoint /signin");
		// Using a response bean to return after populating with error codes and messages,
		//Response Entity instead of String, fix using Generics
		//JSON like response
		if(userService.isUsernameTaken(user.getUsername())==true) {
			if(userService.isPasswordCorrect(user)==true) {
				
				logger.info("Processing POST request for signin: sign in successful");
				userService.setLoginHistory(user,"SUCCESS");
				
				String obfuescatedUserName = Rc4Obfuscator.obfuscate(user.getUsername());
//				System.out.println(obfuescatedUserName);
		        String token = Base64.getEncoder().encodeToString(obfuescatedUserName.getBytes());
//				System.out.println(token);
				
				return ResponseEntity.ok(new UserSignInResponseDTO(SUCCESS_RESPONSE, token, "Signed In Successfully!"));
				//return a structured object
			}else {
				logger.info("Processing POST request for signin: sign in unsuccessful");
				userService.setLoginHistory(user,"FAILURE");
			}
		}
		logger.info("Processing POST request for signin: sign in unsuccessful");
		return ResponseEntity.ok(new UserSignInResponseDTO(ERROR_RESPONSE,null, "Username or Password is incorrect!"));
	}
	
	@Operation(summary = "Gets dashboard details for the user", description = "Returns required data for dashboard page")
	@ApiResponse(responseCode = "200", description = "Returned Successfully")	
	@GetMapping("/dashboard")
	public ResponseEntity<DashboardResponseDTO> handleDashboard(@RequestBody UserInfo uinfo){
		logger.debug("Entered endpoint /dashboard");
		logger.info("Processing GET request for dashboard: dashboard data sent successfully");
		return ResponseEntity.ok(userService.getDashboardData(uinfo.getUsername()));
	}
	
	@Operation(summary = "Gets profile details for the user", description = "Returns required data for profile page")
	@ApiResponse(responseCode = "200", description = "Returned Successfully")	
	@GetMapping("/profile")
	public ResponseEntity<ProfileResponseDTO> viewProfile(@RequestParam("username") String username, @RequestParam("token") String token){
		logger.debug("Entered endpoint /profile");
		if(!HashingTokenUtil.getUsernameFromToenMatched(username, token)) {
			logger.error("Processing GET request for profile: username doesn't match with obfuscated version");
			return ResponseEntity.badRequest().body(new ProfileResponseDTO("Bad Request"));
		}else {		 
			logger.info("Processing GET request for profile: profile data sent successfully");
			return ResponseEntity.ok(userService.getProfileData(username));
		}		
	}
	
	@Operation(summary = "Allows profile detail editing", description = "Returns whether user details are edited successfully")
	@ApiResponse(responseCode = "200", description = "Edited Successfully")	
	@PostMapping("/editProfile")
	public ResponseEntity<UserResponseDTO> editProfile(@RequestBody User user){
		logger.debug("Entered endpoint /editProfile");
		if(userService.editProfileData(user)) {
			logger.info("Processing POST request for editProfile: profile data edited successfully");
			return ResponseEntity.ok(new UserResponseDTO(SUCCESS_RESPONSE,"Profile Edited Successfully!"));
		}else {
			logger.info("Processing POST request for profile: profile data edited unsuccessfully");
			return ResponseEntity.ok(new UserResponseDTO(ERROR_RESPONSE,"Problem encountered in editing profile"));

		}
		
	}
	
	@Operation(summary = "Handles password changes", description = "Returns whether password is edited successfully")
	@ApiResponse(responseCode = "200", description = "Edited Successfully")	
	@PostMapping("/editPassword")
	public ResponseEntity<UserResponseDTO> editPassword(@RequestBody UserPassword upass){
		logger.debug("Entered endpoint /editProfile");
		if(userService.isPasswordCorrect(new User(upass.getUsername(),upass.getOldPassword()))==true){
			logger.info("Processing POST request for editPassword: password data edited successfully");
			return ResponseEntity.ok(userService.editPasswordData(new User(upass.getUsername(),upass.getNewPassword())));
		}else {
			logger.info("Processing POST request for editPassword: Old password is wrong");
			return ResponseEntity.ok(new UserResponseDTO("Wrong Old Password Entered!"));
		}
	}
	
}
