package com.example.dreamshops.controllers;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dreamshops.exceptions.AlreadyExistException;
import com.example.dreamshops.models.Category;
import com.example.dreamshops.responses.ApiResponse;
import com.example.dreamshops.services.category.ICategoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
  private final ICategoryService iCategoryService;

  @GetMapping("/all")
  public ResponseEntity<ApiResponse> getAllCategories() {
    try {
      List<Category> categories = iCategoryService.getAllCategories();
      return ResponseEntity.ok(new ApiResponse("Found!", categories));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error!", INTERNAL_SERVER_ERROR));

    }
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
    try {
      Category newCategory = iCategoryService.addCategory(category);
      return ResponseEntity.ok(new ApiResponse("Add Success", newCategory));
    } catch (AlreadyExistException e) {
      return ResponseEntity.status(CONFLICT)
          .body(new ApiResponse(e.getMessage(), null));
    }
  }
}