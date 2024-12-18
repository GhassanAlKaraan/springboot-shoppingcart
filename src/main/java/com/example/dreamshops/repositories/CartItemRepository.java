package com.example.dreamshops.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dreamshops.models.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

  void deleteAllByCartId(Long id);

}
