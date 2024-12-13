package com.example.dreamshops.controllers;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dreamshops.exceptions.AlreadyExistException;
import com.example.dreamshops.exceptions.ResourceNotFoundException;
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
      return ResponseEntity.ok(new ApiResponse("Get Categories Success!", categories));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error!", INTERNAL_SERVER_ERROR));

    }
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
    try {
      Category newCategory = iCategoryService.addCategory(category);
      return ResponseEntity.ok(new ApiResponse("Add Category Success!", newCategory));
    } catch (AlreadyExistException e) {
      return ResponseEntity.status(CONFLICT)
          .body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/category/{id}/category")
  public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
    try {
      Category theCategory = iCategoryService.getCategoryById(id);
      return ResponseEntity.ok(new ApiResponse("Get Category Success!", theCategory));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/category/{name}/category")
  public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
    try {
      Category theCategory = iCategoryService.getCategoryByName(name);
      return ResponseEntity.ok(new ApiResponse("Get Category Success!", theCategory));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/category/{id}/delete")
  public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long id) {
    try {
      iCategoryService.deleteCategoryById(id);
      return ResponseEntity.ok(new ApiResponse("Delete Category Success!", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PutMapping("/category/{id}/update")
  public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category category) {
    try {
      Category updatedCategory = iCategoryService.updateCategory(category, id);
      return ResponseEntity.ok(new ApiResponse("Update Category Success!", updatedCategory));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }
}