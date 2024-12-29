package com.example.dreamshops.services.product;

import java.util.List;

import com.example.dreamshops.dto.ProductDto;
import com.example.dreamshops.models.Product;
import com.example.dreamshops.requests.AddProductRequest;
import com.example.dreamshops.requests.UpdateProductRequest;

public interface IProductService {
  Product addProduct(AddProductRequest product);

  Product getProductById(Long id);

  void deleteProductById(Long id);

  Product updateProduct(UpdateProductRequest request, Long productId);

  List<Product> getAllProducts();

  List<Product> getProductsByCategory(String category);

  List<Product> getProductsByBrand(String brand);

  List<Product> getProductsByCategoryAndBrand(String category, String brand);

  List<Product> getProductsByName(String name);

  List<Product> getProductsByBrandAndName(String brand, String name);

  Long countProductsByBrandAndName(String brand, String name);

  ProductDto convertToDto(Product product);

  List<ProductDto> getConvertedProducts(List<Product> products);

  List<Product> findDistinctProductsByName();

  List<String> getAllDistinctBrands();
}
