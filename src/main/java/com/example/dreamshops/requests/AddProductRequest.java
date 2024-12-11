package com.example.dreamshops.requests;

import java.math.BigDecimal;
import com.example.dreamshops.models.Category;

import lombok.Data;

@Data // used for requests, unlike Getters and Setters used on models for safety
public class AddProductRequest {
  private Long id;
  private String name;
  private String brand;
  private BigDecimal price;
  private int inventory;
  private String description;
  private Category category;
}
