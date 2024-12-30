package com.example.dreamshops.controllers;

import com.example.dreamshops.dto.ProductDto;
import com.example.dreamshops.exceptions.AlreadyExistException;
import com.example.dreamshops.exceptions.ResourceNotFoundException;
import com.example.dreamshops.models.Product;
import com.example.dreamshops.requests.AddProductRequest;
import com.example.dreamshops.requests.UpdateProductRequest;
import com.example.dreamshops.responses.ApiResponse;
import com.example.dreamshops.services.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
  private final IProductService iProductService;

  @GetMapping("/all")
  public ResponseEntity<ApiResponse> getAllProducts() {
    List<Product> products = iProductService.getAllProducts();
    List<ProductDto> convertedProducts = iProductService.getConvertedProducts(products);
    return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
  }

  @GetMapping("/product/{productId}/product")
  public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
    try {
      Product product = iProductService.getProductById(productId);
      ProductDto productDto = iProductService.convertToDto(product);
      return ResponseEntity.ok(new ApiResponse("success", productDto));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
    try {
      Product theProduct = iProductService.addProduct(product);
      ProductDto productDto = iProductService.convertToDto(theProduct);
      return ResponseEntity.ok(new ApiResponse("Add product success!", productDto));
    } catch (AlreadyExistException e) {
      return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PutMapping("/product/{productId}/update")
  public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest request,
      @PathVariable(name = "productId") Long productId) {
    try {
      Product theProduct = iProductService.updateProduct(request, productId);
      ProductDto productDto = iProductService.convertToDto(theProduct);
      return ResponseEntity.ok(new ApiResponse("Update product success!", productDto));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @DeleteMapping("/product/{productId}/delete")
  public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
    try {
      iProductService.deleteProductById(productId);
      return ResponseEntity.ok(new ApiResponse("Delete product success!", productId));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/products/by/brand-and-name")
  public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam(name = "brandName") String brandName,
      @RequestParam(name = "productName") String productName) {
    try {
      List<Product> products = iProductService.getProductsByBrandAndName(brandName, productName);
      if (products.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found ", null));
      }
      List<ProductDto> convertedProducts = iProductService.getConvertedProducts(products);
      return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/products/by/category-and-brand")
  public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam(name = "category") String category,
      @RequestParam(name = "brand") String brand) {
    try {
      List<Product> products = iProductService.getProductsByCategoryAndBrand(category, brand);
      if (products.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found ", null));
      }
      List<ProductDto> convertedProducts = iProductService.getConvertedProducts(products);
      return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("error", e.getMessage()));
    }
  }

  @GetMapping("/products/{name}/products")
  public ResponseEntity<ApiResponse> getProductByName(@PathVariable(name = "name") String name) {
    try {
      List<Product> products = iProductService.getProductsByName(name);
      if (products.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found ", null));
      }
      List<ProductDto> convertedProducts = iProductService.getConvertedProducts(products);
      return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("error", e.getMessage()));
    }
  }

  @GetMapping("/product/by-brand")
  public ResponseEntity<ApiResponse> findProductByBrand(@RequestParam(name = "brand") String brand) {
    try {
      List<Product> products = iProductService.getProductsByBrand(brand);
      if (products.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found ", null));
      }
      List<ProductDto> convertedProducts = iProductService.getConvertedProducts(products);
      return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
    } catch (Exception e) {
      return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/product/{category}/all/products")
  public ResponseEntity<ApiResponse> findProductsByCategory(@PathVariable(name = "category") String category) {
    try {
      List<Product> products = iProductService.getProductsByCategory(category);
      if (products.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found ", null));
      }
      List<ProductDto> convertedProducts = iProductService.getConvertedProducts(products);
      return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
    } catch (Exception e) {
      return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/distinct/products")
  public ResponseEntity<ApiResponse> getDistinctProductsByCategory() {
    try {
      List<Product> products = iProductService.findDistinctProductsByName();
      if (products.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found ", null));
      }
      List<ProductDto> convertedProducts = iProductService.getConvertedProducts(products);
      return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
    } catch (Exception e) {
      return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/distinct/brands")
  public ResponseEntity<ApiResponse> getAllDistinctBrands() {
    try {
      List<String> brands = iProductService.getAllDistinctBrands();
      return ResponseEntity.ok(new ApiResponse("success", brands));
    } catch (Exception e) {
      return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/product/count/by-brand/and-name")
  public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam(name = "brand") String brand,
      @RequestParam(name = "name") String name) {
    try {
      var productCount = iProductService.countProductsByBrandAndName(brand, name);
      return ResponseEntity.ok(new ApiResponse("Product count!", productCount));
    } catch (Exception e) {
      return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
    }
  }

}