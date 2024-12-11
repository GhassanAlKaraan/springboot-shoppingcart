package com.example.dreamshops.services.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.dreamshops.dto.ImageDto;
import com.example.dreamshops.models.Image;

public interface IImageService {
  Image getImageById(Long id);

  void deleteImageById(Long id);

  List<ImageDto> saveImage(List<MultipartFile> files, Long productId);

  void updateImage(MultipartFile image, Long imageId);

}
