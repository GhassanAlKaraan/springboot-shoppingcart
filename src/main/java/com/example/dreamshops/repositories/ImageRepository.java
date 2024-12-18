package com.example.dreamshops.repositories;

import com.example.dreamshops.models.Image;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
  List<Image> findByProductId(Long id);
}
