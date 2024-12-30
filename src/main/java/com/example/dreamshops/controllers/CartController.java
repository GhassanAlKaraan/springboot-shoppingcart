package com.example.dreamshops.controllers;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dreamshops.exceptions.ResourceNotFoundException;
import com.example.dreamshops.models.Cart;
import com.example.dreamshops.responses.ApiResponse;
import com.example.dreamshops.services.cart.ICartService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {
  private final ICartService iCartService;

  @GetMapping("/{cartId}/my-cart")
  public ResponseEntity<ApiResponse> getCart(@PathVariable(name = "cartId") Long cartId) {
    try {
      Cart cart = iCartService.getCart(cartId);
      return ResponseEntity.ok(new ApiResponse("Get Cart Success!", cart));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),
          null));
    }
  }

  @DeleteMapping("/{cartId}/clear")
  public ResponseEntity<ApiResponse> clearCart(@PathVariable(name = "cartId") Long cartId) {
    try {
      iCartService.clearCart(cartId);
      return ResponseEntity.ok(new ApiResponse("Clear Cart Success!", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/{cartId}/cart/total-amount")
  public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable(name = "cartId") Long cartId) {
    try {
      BigDecimal totalAmount = iCartService.getTotalPrice(cartId);
      return ResponseEntity.ok(new ApiResponse("Get Total Amount Success!", totalAmount));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

}
