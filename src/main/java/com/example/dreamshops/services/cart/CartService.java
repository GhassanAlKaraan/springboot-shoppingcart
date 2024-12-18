package com.example.dreamshops.services.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.dreamshops.models.Cart;
import com.example.dreamshops.repositories.CartItemRepository;
import com.example.dreamshops.repositories.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;

  @Override
  public Cart getCart(Long id) {
    Cart cart = cartRepository.findById(id).orElseThrow(() -> new RuntimeException("Cart Not Found."));

    BigDecimal totalAmount = cart.getTotalAmount();
    cart.setTotalAmount(totalAmount);
    return cartRepository.save(cart);
  }

  @Override
  public void clearCart(Long id) {
    Cart cart = getCart(id);
    cartItemRepository.deleteAllByCartId(id);
    cart.getItems().clear();
    cartRepository.deleteById(id);

  }

  @Override
  public BigDecimal getTotalPrice(Long id) {
    Cart cart = getCart(id);
    return cart.getTotalAmount();
  }
}
