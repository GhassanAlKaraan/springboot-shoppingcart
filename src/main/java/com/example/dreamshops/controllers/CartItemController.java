package com.example.dreamshops.controllers;

import com.example.dreamshops.exceptions.ResourceNotFoundException;
import com.example.dreamshops.models.Cart;
import com.example.dreamshops.models.User;
import com.example.dreamshops.responses.ApiResponse;
import com.example.dreamshops.services.cartitem.ICartItemService;
import com.example.dreamshops.services.cart.ICartService;
import com.example.dreamshops.services.user.IUserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
  private final ICartItemService iCartItemService;
  private final ICartService iCartService;
  private final IUserService iUserService;

  @PostMapping("/item/add")
  public ResponseEntity<ApiResponse> addItemToCart(
      @RequestParam Long productId,
      @RequestParam Integer quantity) {
    try {
      User user = iUserService.getAuthenticatedUser();
      Cart cart = iCartService.initializeNewCart(user);
      iCartItemService.addItemToCart(cart.getId(), productId, quantity);
      return ResponseEntity.ok(new ApiResponse("Item added to cart successfully", Map.of("cartId", cart.getId())));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    } catch (JwtException e) {
      return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/cart/{cartId}/item/{productId}/remove")
  public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable(name = "cartId") Long cartId,
      @PathVariable(name = "productId") Long productId) {
    try {
      iUserService.getAuthenticatedUser();
      iCartItemService.removeItemFromCart(cartId, productId);
      return ResponseEntity.ok(new ApiResponse("Remove Item Success", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    } catch (JwtException e) {
      return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PutMapping("/cart/{cartId}/item/{itemId}/update")
  public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
      @PathVariable Long itemId,
      @RequestParam Integer quantity) {
    try {
      iCartItemService.updateItemQuantity(cartId, itemId, quantity);
      return ResponseEntity.ok(new ApiResponse("Update Item Success", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }

  }
}