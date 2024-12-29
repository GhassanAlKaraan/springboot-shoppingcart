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

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
  private final ICartItemService iCartItemService;
  private final ICartService iCartService;
  private final IUserService userService;

  @PostMapping("/item/add")
  public ResponseEntity<ApiResponse> addItemToCart(
      @RequestParam Long productId,
      @RequestParam Integer quantity) {
    try {
      User user = userService.getAuthenticatedUser();
      Cart cart = iCartService.initializeNewCart(user);
      iCartItemService.addItemToCart(cart.getId(), productId, quantity);
      return ResponseEntity.ok(new ApiResponse("Item added to cart successfully", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    } catch (JwtException e) {
      return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
  public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
    try {
      iCartItemService.removeItemFromCart(cartId, itemId);
      return ResponseEntity.ok(new ApiResponse("Remove Item Success", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
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