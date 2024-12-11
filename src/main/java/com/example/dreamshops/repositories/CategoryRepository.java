package com.example.dreamshops.repositories;

import com.example.dreamshops.models.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Boolean existsByName(String name);

  Category findByName(String name);
}
