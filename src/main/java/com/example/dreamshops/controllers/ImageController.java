package com.example.dreamshops.controllers;

import java.sql.SQLException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dreamshops.dto.ImageDto;
import com.example.dreamshops.models.Image;
import com.example.dreamshops.responses.ApiResponse;
import com.example.dreamshops.services.image.IImageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {

  private final IImageService iImageService;

  @PostMapping("/upload")
  public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
    try {
      List<ImageDto> imageDtos = iImageService.saveImage(files, productId);
      return ResponseEntity.ok(new ApiResponse("Upload Success", imageDtos));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Upload Failed!", e.getMessage()));
    }
  }

  @GetMapping("/image/download/{imageId}")
  public ResponseEntity<Resource> downloadImage(@PathVariable("id") Long imageId) {

    try {
      Image image = iImageService.getImageById(imageId);
      ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
      return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=\"" + image.getFileName() + "\"")
          .body(resource);
    } catch (SQLException e) {
      return null;
    }

  }
}
