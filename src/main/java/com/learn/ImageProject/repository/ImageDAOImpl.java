package com.learn.ImageProject.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.learn.ImageProject.dao.ImageDAO;
import com.learn.ImageProject.model.Image;
import com.learn.ImageProject.model.User;
import com.learn.ImageProject.response.ImageListResponseDTO;
import com.learn.ImageProject.response.ImageResponseDTO;
import com.learn.ImageProject.response.ImageDetailsDTO;


@Repository
public class ImageDAOImpl implements ImageDAO{
	private final DataSource dataSource;
	
	private final static String GET_IMAGE_LIST_QUERY = "SELECT image_id, images.user_id, "
		+ "image_path, isSecure, image_format, image_size, upload_date FROM images "
		+ "INNER JOIN users ON users.user_id = images.user_id "
		+ "	AND users.username = ?;";
	
	
	private final static String INSERT_IMAGE_QUERY = "INSERT INTO images (user_id, image_path, isSecure, image_format, image_size, upload_date) "
            + "VALUES (?, ?, ?, ?, ?, ?)";
	
	private final static String GET_USER_ID_QUERY = "SELECT user_id FROM users WHERE users.username = ?;" ;
	
	private final static String GET_IMAGE_DETAILS_QUERY = "SELECT image_id, user_id, image_path, isSecure, image_format, image_size, upload_date "
            + "FROM images WHERE image_id = ?";
	
	
	
	private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);
	
	@Autowired
	public ImageDAOImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}	
	
	
	
	private int getUserId(User user) {
		logger.debug("Entered getUserId");
		int user_id = 0;
		try (Connection conn = dataSource.getConnection();
	             PreparedStatement stmnt = conn.prepareStatement(GET_USER_ID_QUERY)) {
			stmnt.setString(1,user.getUsername());
			
			try (ResultSet rs = stmnt.executeQuery()) {
				if (rs.next()) {
					user_id = rs.getInt(1);
				}
				logger.info("Query successful: User ID returned");
				return user_id;
			}
			
		}catch(Exception e) {
			logger.error("Query unsuccessful: User ID not returned");
			e.printStackTrace();
			return -1;
		}
		
	}

	
	
	
	public ImageResponseDTO uploadImage(Image image) {
		logger.debug("Entered uploadImage");
	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(INSERT_IMAGE_QUERY)) {

	        stmt.setInt(1, getUserId(new User(image.getUsername())));
	        stmt.setString(2, image.getImagePath());
	        stmt.setBoolean(3, image.isSecure());
	        stmt.setString(4, image.getImageFormat());
	        stmt.setInt(5, image.getImageSize());
	        stmt.setObject(6, image.getUploadDate());

	        int rows = stmt.executeUpdate();
	        if (rows > 0) {
	        		logger.info("Query successful: Image upload successful");
	            return new ImageResponseDTO("Image uploaded successfully");
	        }
	        logger.debug("Query unsuccessful: Image upload failed");
	        return new ImageResponseDTO("Image upload failed");

	    } catch (Exception e) {
	    		logger.info("Query unsuccessful: Error uploading image");
	        e.printStackTrace();
	        return new ImageResponseDTO("Error uploading image");
	    }
	
	}

	public ImageListResponseDTO getImageList(String username){
		logger.debug("Entered getImageList");
//		System.out.println(username);
		try (Connection conn = dataSource.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(GET_IMAGE_LIST_QUERY)) {
			
			 stmt.setString(1, username);
			 
			 try (ResultSet rs = stmt.executeQuery()) {
				 ImageListResponseDTO dto = new ImageListResponseDTO();
				 List<ImageDetailsDTO> images = new ArrayList<ImageDetailsDTO>();
				 while(rs.next()) {
					 ImageDetailsDTO image = new ImageDetailsDTO();
					 image.setImageId(rs.getInt("image_id"));
					 image.setSecure(rs.getBoolean("isSecure"));
					 image.setImageFormat(rs.getString("image_format"));
					 image.setImageSize(rs.getInt("image_size"));
					 image.setUploadDate(rs.getObject("upload_date",OffsetDateTime.class));
					 images.add(image);
				 }
				 dto.setImages(images);
				 dto.setMessage("Returned Image List Successfully!");
				 logger.info("Query successful: Image list sent successfully");
				 return dto;
			 }
			
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("Query unsuccessful: Image list sending failed, couldn't retrieve images from the list");
			return new ImageListResponseDTO("Couldn't retrieve the images from the list!");
		}
	}
	
	public Image getImage(int imageId) {
		logger.debug("Entered getImageId");
	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(GET_IMAGE_DETAILS_QUERY)) {
	        stmt.setInt(1, imageId);

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                Image image = new Image();
	                image.setImageId(rs.getInt("image_id"));
	                image.setUserId(rs.getInt("user_id"));
	                image.setImagePath(rs.getString("image_path"));
	                image.setSecure(rs.getBoolean("isSecure"));
	                image.setImageFormat(rs.getString("image_format"));
	                image.setImageSize(rs.getInt("image_size"));
	                image.setUploadDate(rs.getObject("upload_date", OffsetDateTime.class));
	                logger.info("Query successful: Image ID sent successfully");
	                return image;
	            }
	        }
	    } catch (Exception e) {
	    		logger.error("Query unsuccessful: Image ID not sent successfully");
	        e.printStackTrace();
	    }
	    return null;
	}
	
	private Image decryptImage(int imageId) {
		logger.debug("Entered decryptImage");
		logger.info("Query successful: Image sent successfully");
		return new Image();
	}
	
	private ImageResponseDTO encryptImage(Image image) {
		logger.debug("Entered encryptImage");
		logger.info("Query successful: Image sent successfully");
		return new ImageResponseDTO("Encrypted");
	}
}
