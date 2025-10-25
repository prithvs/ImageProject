package com.learn.ImageProject.dao;

import java.util.List;

import com.learn.ImageProject.model.Image;
import com.learn.ImageProject.response.ImageListResponseDTO;
import com.learn.ImageProject.response.ImageResponseDTO;

public interface ImageDAO {
	ImageResponseDTO uploadImage(Image image);
	ImageListResponseDTO getImageList(String username);
	Image getImage(int imageId);
}
