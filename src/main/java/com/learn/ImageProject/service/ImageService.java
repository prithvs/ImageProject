package com.learn.ImageProject.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.learn.ImageProject.dao.ImageDAO;
import com.learn.ImageProject.model.Image;
import com.learn.ImageProject.response.ImageListResponseDTO;
import com.learn.ImageProject.response.ImageResponseDTO;
import com.learn.ImageProject.util.ImageEncryptUtil;

@Service
public class ImageService {
	private final ImageDAO imageDAO;
	
	@Value("${image.upload.dir}") // configurable upload directory in application.properties
	    private String uploadDir;
	
	
	public ImageService(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}
	
	public ImageListResponseDTO getImageList(String username) {
		return imageDAO.getImageList(username);
	}
	

	public ImageResponseDTO uploadImage(MultipartFile file, String username, boolean isSecure) throws IOException{
		
		Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
		Files.createDirectories(uploadPath);
		
		//Generating Image path
		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
	    Path targetPath = uploadPath.resolve(fileName);
	    
	    //Checking for encryption 
	    if (isSecure) {
            ImageEncryptUtil.encryptAndSaveFile(file, targetPath);
        } else {
        		//Storing the image
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
	    
	    
	     
	     Image image = new Image();
	     image.setImagePath(targetPath.toString());
	     image.setImageFormat(file.getContentType());
	     image.setImageSize((int) file.getSize());
	     image.setSecure(isSecure);
	     image.setUploadDate(java.time.OffsetDateTime.now());
	     image.setUsername(username);
	     
	     
	     return imageDAO.uploadImage(image);
	}
	

	 	 
	 public byte[] viewImage(int imageId) throws IOException {
		    Image image = imageDAO.getImage(imageId);
		    if (image == null) {
		        return null;
		    }

		    Path path = Paths.get(image.getImagePath());
		    byte[] fileBytes = Files.readAllBytes(path);

		    if (image.isSecure()) {
		        return ImageEncryptUtil.decryptFile(fileBytes);
		    } else {
		        return fileBytes;
		    }
		}

	 
}
