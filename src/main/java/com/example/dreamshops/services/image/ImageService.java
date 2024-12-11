package com.example.dreamshops.services.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dreamshops.dto.ImageDto;
import com.example.dreamshops.exceptions.ResourceNotFoundException;
import com.example.dreamshops.models.Image;
import com.example.dreamshops.models.Product;
import com.example.dreamshops.repositories.ImageRepository;
import com.example.dreamshops.services.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

  private final ImageRepository imageRepository;
  private final IProductService iProductService;

  @Override
  public Image getImageById(Long id) {
    return imageRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No Image Found With Id: " + id));
  }

  @Override
  public void deleteImageById(Long id) {
    imageRepository.findById(id).ifPresentOrElse(
        imageRepository::delete,
        () -> {
          throw new ResourceNotFoundException("No Image Found With Id: " + id);
        });
  }

  @Override
  public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
    Product product = iProductService.getProductById(productId);
    List<ImageDto> savedImageDto = new ArrayList<>();

    for (MultipartFile file : files) {
      try {
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        image.setImage(new SerialBlob(file.getBytes()));
        image.setProduct(product);

        String buildDownloadUrl = "/api/v1/images/image/download/";

        image.setDownloadUrl(buildDownloadUrl + image.getId());
        Image savedImage = imageRepository.save(image);

        savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
        imageRepository.save(image);

        ImageDto imageDto = new ImageDto();
        imageDto.setImageId(savedImage.getId());
        imageDto.setImageName(savedImage.getFileName());
        imageDto.setDownloadUrl(savedImage.getDownloadUrl());

        savedImageDto.add(imageDto);

      } catch (IOException | SQLException e) {
        throw new RuntimeException(e.getMessage());
      }
    }
    return savedImageDto;
  }

  @Override
  public void updateImage(MultipartFile file, Long imageId) {
    Image existingImage = getImageById(imageId);
    try {
      existingImage.setFileName(file.getOriginalFilename());
      existingImage.setImage(new SerialBlob(file.getBytes()));
      imageRepository.save(existingImage);
    } catch (IOException | SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

}
