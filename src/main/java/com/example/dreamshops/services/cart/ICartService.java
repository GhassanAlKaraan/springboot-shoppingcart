package com.example.dreamshops.services.cart;

import java.math.BigDecimal;

import com.example.dreamshops.models.Cart;

public interface ICartService {
  Cart getCart(Long id);

  void clearCart(Long id);

  BigDecimal getTotalPrice(Long id);

}
