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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dreamshops.exceptions.AlreadyExistException;
import com.example.dreamshops.exceptions.ResourceNotFoundException;
import com.example.dreamshops.models.Product;
import com.example.dreamshops.requests.AddProductRequest;
import com.example.dreamshops.requests.UpdateProductRequest;
import com.example.dreamshops.responses.ApiResponse;
import com.example.dreamshops.services.product.IProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
  private final IProductService iProductService;

  @GetMapping("/all")
  public ResponseEntity<ApiResponse> getAllProducts() {
    try {
      List<Product> products = iProductService.getAllProducts();
      return ResponseEntity.ok(new ApiResponse("Get Products Success!", products));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error!", INTERNAL_SERVER_ERROR));
    }
  }

  @GetMapping("/product/{productId}/product")
  public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
    try {
      Product product = iProductService.getProductById(productId);
      return ResponseEntity.ok(new ApiResponse("Get Product Success!", product));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addCategory(@RequestBody AddProductRequest product) {
    try {
      Product theProduct = iProductService.addProduct(product);
      return ResponseEntity.ok(new ApiResponse("Add Product Success!", theProduct));
    } catch (AlreadyExistException e) {
      return ResponseEntity.status(CONFLICT)
          .body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PutMapping("/product/{productId}/update")
  public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long productId,
      @RequestBody UpdateProductRequest product) {
    try {
      Product updatedProduct = iProductService.updateProduct(product, productId);
      return ResponseEntity.ok(new ApiResponse("Update Product Success!", updatedProduct));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/product/{productId}/delete")
  public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long productId) {
    try {
      iProductService.deleteProductById(productId);
      return ResponseEntity.ok(new ApiResponse("Delete Product Success!", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/products/by/brand-and-name")
  public ResponseEntity<ApiResponse> getProductByBrandAndName(
      @RequestParam String brandName,
      @RequestParam String productName) {
    try {
      List<Product> products = iProductService.getProductsByBrandAndName(brandName, productName);
      if (products.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products Found.", null));
      }
      return ResponseEntity.ok(new ApiResponse("Get Products Success!", products));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/products/{productName}/products")
  public ResponseEntity<ApiResponse> getProductByName(
      @RequestParam String productName) {
    try {
      List<Product> products = iProductService.getProductsByName(productName);
      if (products.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products Found.", null));
      }
      return ResponseEntity.ok(new ApiResponse("Get Products Success!", products));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/product/by-brand")
  public ResponseEntity<ApiResponse> findProductByBrand(
      @RequestParam String brand) {
    try {
      List<Product> products = iProductService.getProductsByBrand(brand);
      if (products.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products Found.", null));
      }
      return ResponseEntity.ok(new ApiResponse("Get Products Success!", products));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

}
