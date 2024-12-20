package com.example.dreamshops.repositories;

import com.example.dreamshops.models.Cart;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
  Cart findByUserId(Long userId);
}
