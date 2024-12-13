package com.example.dreamshops.services.product;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.dreamshops.exceptions.ResourceNotFoundException;
import com.example.dreamshops.models.Category;
import com.example.dreamshops.models.Product;
import com.example.dreamshops.repositories.CategoryRepository;
import com.example.dreamshops.repositories.ProductRepository;
import com.example.dreamshops.requests.AddProductRequest;
import com.example.dreamshops.requests.UpdateProductRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ProductService implements IProductService {

  private final ProductRepository productRepository; // should be final to be required by the service constructor
  private final CategoryRepository categoryRepository;

  @Override
  public Product addProduct(AddProductRequest request) {
    // check if category is found in db
    // if yes, set it as the new product category
    // if not, save it as a new category, and set as the new product category
    Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
        .orElseGet(() -> {
          Category newCategory = new Category(request.getCategory().getName());
          return categoryRepository.save(newCategory);
        });
    request.setCategory(category);
    return productRepository.save(createProduct(request, category));
  }

  private Product createProduct(AddProductRequest request, Category category) {
    return new Product(request.getName(), request.getBrand(), request.getPrice(), request.getInventory(),
        request.getDescription(), category);
  }

  @Override
  public Product getProductById(Long id) {
    return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
  }

  @Override
  public void deleteProductById(Long id) {
    productRepository.findById(id).ifPresentOrElse(
        productRepository::delete,
        () -> {
          throw new ResourceNotFoundException("Product Not Found");
        });
  }

  @Override
  public Product updateProduct(UpdateProductRequest request, Long productId) {
    return productRepository.findById(productId)
        .map(existingProduct -> updateExistingProduct(existingProduct, request))
        .map(productRepository::save)
        .orElseThrow(() -> new ResourceNotFoundException("Product Not Found!"));
  }

  private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
    existingProduct.setName(request.getName());
    existingProduct.setBrand(request.getBrand());
    existingProduct.setPrice(request.getPrice());
    existingProduct.setInventory(request.getInventory());
    existingProduct.setDescription(request.getDescription());

    Category category = categoryRepository.findByName(request.getCategory().getName());
    existingProduct.setCategory(category);

    return existingProduct;
  }

  @Override
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  @Override
  public List<Product> getProductsByCategory(String category) {
    return productRepository.findByCategoryName(category);
  }

  @Override
  public List<Product> getProductsByBrand(String brand) {
    return productRepository.findByBrand(brand);
  }

  @Override
  public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
    return productRepository.findByCategoryNameAndBrand(category, brand);
  }

  @Override
  public List<Product> getProductsByName(String name) {
    return productRepository.findByName(name);
  }

  @Override
  public List<Product> getProductsByBrandAndName(String brand, String name) {
    return productRepository.findByBrandAndName(brand, name);
  }

  @Override
  public Long countProductsByBrandAndName(String brand, String name) {
    return productRepository.countByBrandAndName(brand, name);
  }

}
