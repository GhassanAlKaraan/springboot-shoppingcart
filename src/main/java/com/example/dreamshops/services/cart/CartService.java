package com.example.dreamshops.services.cart;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dreamshops.exceptions.ResourceNotFoundException;
import com.example.dreamshops.models.Cart;
import com.example.dreamshops.models.User;
import com.example.dreamshops.repositories.CartItemRepository;
import com.example.dreamshops.repositories.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  // private final AtomicLong cartIdGenerator = new AtomicLong(0);

  @Override
  public Cart getCart(Long id) {
    Cart cart = cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart Not Found."));

    BigDecimal totalAmount = cart.getTotalAmount();
    cart.setTotalAmount(totalAmount);
    return cartRepository.save(cart);
  }

  @Transactional
  @Override
  public void clearCart(Long id) {
    Cart cart = getCart(id);
    cartItemRepository.deleteAllByCartId(id);
    cart.clearItems();
    cartRepository.deleteById(id);

  }

  @Override
  public BigDecimal getTotalPrice(Long id) {
    Cart cart = getCart(id);
    return cart.getTotalAmount();
  }

  @Override
  public Cart initializeNewCart(User user) {

    return Optional.ofNullable(getCartByUserId(user.getId()))
        .orElseGet(() -> {
          Cart cart = new Cart();
          cart.setUser(user);
          return cartRepository.save(cart);
        });
  }

  @Override
  public Cart getCartByUserId(Long userId) {
    return cartRepository.findByUserId(userId);
  }
}
