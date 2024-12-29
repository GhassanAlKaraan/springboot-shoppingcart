package com.example.dreamshops.services.product;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.dreamshops.dto.ImageDto;
import com.example.dreamshops.dto.ProductDto;
import com.example.dreamshops.exceptions.AlreadyExistException;
import com.example.dreamshops.exceptions.ResourceNotFoundException;
import com.example.dreamshops.models.Category;
import com.example.dreamshops.models.Image;
import com.example.dreamshops.models.Product;
import com.example.dreamshops.repositories.CategoryRepository;
import com.example.dreamshops.repositories.ImageRepository;
import com.example.dreamshops.repositories.ProductRepository;
import com.example.dreamshops.requests.AddProductRequest;
import com.example.dreamshops.requests.UpdateProductRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ProductService implements IProductService {

  private final ProductRepository productRepository; // should be final to be required by the service constructor
  private final CategoryRepository categoryRepository;
  private final ImageRepository imageRepository;
  private final ModelMapper modelMapper;

  @Override
  public Product addProduct(AddProductRequest request) {
    // check if category is found in db
    // if yes, set it as the new product category
    // if not, save it as a new category, and set as the new product category

    if (productExists(request.getName(), request.getBrand())) {
      throw new AlreadyExistException(
          request.getBrand() + " " + request.getName() + " Already Exists, You May Update This Product Instead.");
    }

    Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
        .orElseGet(() -> {
          Category newCategory = new Category(request.getCategory().getName());
          return categoryRepository.save(newCategory);
        });
    request.setCategory(category);
    return productRepository.save(createProduct(request, category));
  }

  private Boolean productExists(String name, String brand) {
    return productRepository.existsByBrandAndName(brand, name);
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

  @Override
  public List<ProductDto> getConvertedProducts(List<Product> products) {
    return products.stream().map(this::convertToDto).toList();
  }

  @Override
  public ProductDto convertToDto(Product product) {
    ProductDto productDto = modelMapper.map(product, ProductDto.class);
    List<Image> images = imageRepository.findByProductId(product.getId());
    List<ImageDto> imageDtos = images.stream()
        .map(image -> modelMapper.map(image, ImageDto.class))
        .toList();
    productDto.setImages(imageDtos);
    return productDto;
  }

  @Override
  public List<Product> findDistinctProductsByName() {
    List<Product> products = productRepository.findAll();
    Map<String, Product> distinctProductsMap = products.stream()
        .collect(Collectors.toMap(
            Product::getName,
            product -> product,
            (existing, replacement) -> existing));
    return new ArrayList<>(distinctProductsMap.values());
  }

  @Override
  public List<String> getAllDistinctBrands() {
    return productRepository.findAll().stream()
        .map(Product::getBrand)
        .distinct()
        .collect(Collectors.toList());
  }

}
