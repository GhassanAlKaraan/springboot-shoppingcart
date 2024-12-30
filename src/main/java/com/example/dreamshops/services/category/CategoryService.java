package com.example.dreamshops.services.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.dreamshops.exceptions.AlreadyExistException;
import com.example.dreamshops.exceptions.ResourceNotFoundException;
import com.example.dreamshops.models.Category;
import com.example.dreamshops.repositories.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

  private final CategoryRepository categoryRepository;

  @Override
  public Category getCategoryById(Long id) {
    return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category Not Found!"));
  }

  @Override
  public Category getCategoryByName(String name) {
    return categoryRepository.findByName(name);
    // .orElseThrow(() -> new ResourceNotFoundException("Category Not Found!"));
  }

  @Override
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  @Override
  public Category addCategory(Category category) {
    return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
        .map(categoryRepository::save)
        .orElseThrow(() -> new AlreadyExistException("Category " + category.getName() + " Already Exists!"));
  }

  @Override
  public Category updateCategory(Category category, Long id) {
    return Optional.ofNullable(getCategoryById(id)).map(existingCategory -> {
      existingCategory.setName(category.getName());
      return categoryRepository.save(existingCategory);
    }).orElseThrow(() -> new ResourceNotFoundException("Category Not Found!"));
  }

  @Override
  public void deleteCategoryById(Long id) {
    categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete,
        () -> new ResourceNotFoundException("Category Not Found!"));
  }

}
