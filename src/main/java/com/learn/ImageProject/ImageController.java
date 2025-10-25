package com.learn.ImageProject;

import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.learn.ImageProject.response.ImageListResponseDTO;
import com.learn.ImageProject.response.ImageResponseDTO;
import com.learn.ImageProject.response.ImageViewDTO;
import com.learn.ImageProject.service.ImageService;
import com.learn.ImageProject.util.HashingTokenUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;



@RestController
public class ImageController {
	private final ImageService imageService;
	private static final Logger logger = LogManager.getLogger(ImageController.class);
	
	 public ImageController(ImageService imageService) {
	        this.imageService = imageService;
	 }
	 
	 
	 @Operation(summary = "Get all images of a user", description = "Returns a list of all images of a user")
	 @ApiResponse(responseCode = "200", description = "Returned Successfully")	
	 @GetMapping("/getImageList")
	 public ResponseEntity<ImageListResponseDTO> getImageList(@RequestParam("username") String username, @RequestParam("token") String token) {
	 	logger.debug("Entered endpoint /getImageList");		 

		 if(!HashingTokenUtil.getUsernameFromToenMatched(username, token)) {
			 logger.error("Processing GET request for image list: Bad Request as username doesn't match with token");
			 return ResponseEntity.badRequest().body(new ImageListResponseDTO("Bad Request"));
		 }else {		 
			 logger.info("Processing GET request for image list: image list sent");
			 return ResponseEntity.ok(imageService.getImageList(username));
		 }
	 }
	 
	 
	 @Operation(summary = "Handles image uploads", description = "Returns whether image is uploaded successfully")
	 @ApiResponse(responseCode = "200", description = "Uploaded Successfully")	
	 @PostMapping("/uploadImage")
	 public ResponseEntity<ImageResponseDTO> uploadImage(@RequestParam("file") MultipartFile file,
	         @RequestParam("username") String username,
	         @RequestParam("isSecure") boolean isSecure) {
		 logger.debug("Entered endpoint /uploadImage");
	     try {
	         // Call service layer
	         ImageResponseDTO response = imageService.uploadImage(file, username, isSecure);
	 		logger.info("Processing POST request for image upload: image upload successful");

	         return ResponseEntity.ok(response);
	     } catch (Exception e) {
	    	 	logger.error("Processing POST request for image upload: image upload unsuccessful");
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ImageResponseDTO("Image upload failed"));
	     }
	 }
	 
	 
	 
	 @Operation(summary = "Gets an image", description = "Returns a particular image")
	 @ApiResponse(responseCode = "200", description = "Returned Successfully")	
	 @GetMapping("/viewImage")
	 public ResponseEntity<ImageViewDTO> viewImage(@RequestParam("imageId") int imageId, @RequestParam("username") String username, @RequestParam("token") String token) {
		 logger.debug("Entered endpoint /viewImage");
		 
		 if(!HashingTokenUtil.getUsernameFromToenMatched(username, token)) {
			 logger.error("Processing GET request for image list: Bad Request as username doesn't match with token");
			 return ResponseEntity.badRequest().body(new ImageViewDTO("Bad Request"));
		 }else {			 

			 try {
				 byte[] imageBytes = imageService.viewImage(imageId);
			        if (imageBytes == null) {
			            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			        }
			        String base64EncodedImage = Base64.getEncoder().encodeToString(imageBytes);
			        logger.info("Processing GET request for image view: successful");
			        return ResponseEntity.ok(new ImageViewDTO(base64EncodedImage));
			 }catch (Exception e) {
				logger.info("Processing GET request for image view: successful");
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			  }
		 }
	 }	 
}
