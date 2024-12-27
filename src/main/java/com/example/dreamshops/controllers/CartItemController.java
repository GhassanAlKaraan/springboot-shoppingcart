package com.example.dreamshops.controllers;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dreamshops.exceptions.ResourceNotFoundException;
import com.example.dreamshops.models.Cart;
import com.example.dreamshops.models.User;
import com.example.dreamshops.responses.ApiResponse;
import com.example.dreamshops.services.cart.ICartService;
import com.example.dreamshops.services.cartitem.ICartItemService;
import com.example.dreamshops.services.user.IUserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
  private final ICartItemService iCartItemService;
  private final ICartService iCartService;
  private final IUserService iUserService;

  @PostMapping("/item/add")
  public ResponseEntity<ApiResponse> addItemToCart(
      @RequestParam(name = "productId") Long productId,
      @RequestParam(name = "quantity") Integer quantity) {
    try {
      User user = iUserService.getUserById(2L); // !!!!!!!!!!! default user
      Cart cart = iCartService.initializeNewCart(user);

      iCartItemService.addItemToCart(cart.getId(), productId, quantity);
      return ResponseEntity.ok(new ApiResponse("Add Item Success", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
  public ResponseEntity<ApiResponse> removeItemFromCart(
      @PathVariable(name = "cartId") Long cartId,
      @PathVariable(name = "itemId") Long itemId) {
    try {
      iCartItemService.removeItemFromCart(cartId, itemId);
      return ResponseEntity.ok(new ApiResponse("Remove Item Success", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PutMapping("/cart/{cartId}/item/{itemId}/update")
  public ResponseEntity<ApiResponse> updateItemQuantity(
      @PathVariable(name = "cartId") Long cartId,
      @PathVariable(name = "itemId") Long itemId,
      @RequestParam(name = "quantity") Integer quantity) {
    try {
      iCartItemService.updateItemQuantity(cartId, itemId, quantity);
      return ResponseEntity.ok(new ApiResponse("Update Item Success", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }

  }
}
