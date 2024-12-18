package com.example.dreamshops.controllers;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.sql.SQLException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dreamshops.dto.ImageDto;
import com.example.dreamshops.exceptions.ResourceNotFoundException;
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
  public ResponseEntity<ApiResponse> saveImages(@RequestParam(name = "files") List<MultipartFile> files,
      @RequestParam(name = "productId") Long productId) {
    try {
      List<ImageDto> imageDtos = iImageService.saveImage(files, productId);
      return ResponseEntity.ok(new ApiResponse("Upload Image Success!", imageDtos));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Upload Failed!", e.getMessage()));
    }
  }

  @GetMapping("/image/download/{imageId}")
  public ResponseEntity<Resource> downloadImage(@PathVariable(name = "imageId") Long imageId) {
    try {
      Image image = iImageService.getImageById(imageId);
      ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));

      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(image.getFileType()))
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
          .body(resource);
    } catch (SQLException e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/image/{imageId}/update")
  public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
    try {
      Image image = iImageService.getImageById(imageId);
      if (image != null) {
        iImageService.updateImage(file, imageId);
        return ResponseEntity.ok(new ApiResponse("Update Success!", image));
      }
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .body(new ApiResponse("Update Failed!", INTERNAL_SERVER_ERROR));
  }

  @DeleteMapping("/image/{imageId}/delete")
  public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
    try {
      Image image = iImageService.getImageById(imageId);
      if (image != null) {
        iImageService.deleteImageById(imageId);
        return ResponseEntity.ok(new ApiResponse("Delete Success!", image));
      }
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .body(new ApiResponse("Delete Failed!", INTERNAL_SERVER_ERROR));
  }
}
