package com.example.dreamshops.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.dreamshops.dto.OrderDto;
import com.example.dreamshops.exceptions.ResourceNotFoundException;
import com.example.dreamshops.models.Order;
import com.example.dreamshops.models.User;
import com.example.dreamshops.responses.ApiResponse;
import com.example.dreamshops.services.order.IOrderService;
import com.example.dreamshops.services.user.IUserService;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

  private final IOrderService iOrderService;
  private final IUserService iUserService;

  // @PostMapping("/order")
  // public ResponseEntity<ApiResponse> createOrder(@RequestParam(name = "userId")
  // Long userId) {
  // try {
  // Order order = iOrderService.placeOrder(userId);
  // OrderDto orderDto = iOrderService.convertToDto(order);
  // return ResponseEntity.ok(new ApiResponse("Item Order Success!", orderDto));
  // } catch (Exception e) {
  // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
  // .body(new ApiResponse("Error Occured!", e.getMessage()));
  // }
  // }

  @PostMapping("/order")
  public ResponseEntity<ApiResponse> createOrder() {
    try {
      User user = iUserService.getAuthenticatedUser();
      if (user == null) {
        return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse("User not authenticated", null));
      }
      Order order = iOrderService.placeOrder(user.getId());
      OrderDto orderDto = iOrderService.convertToDto(order);
      return ResponseEntity.ok(new ApiResponse("Item Order Success!", orderDto));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    } catch (JwtException e) {
      return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse("Unauthorized", null));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Error Occurred!", e.getMessage()));
    }
  }

  @GetMapping("/{orderId}/order")
  public ResponseEntity<ApiResponse> getOrderById(@PathVariable(name = "orderId") Long orderId) {
    try {
      OrderDto order = iOrderService.getOrder(orderId);
      return ResponseEntity.ok(new ApiResponse("Get Order Success!", order));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Oops!", e.getMessage()));
    }
  }

  @GetMapping("/user/{userId}/order")
  public ResponseEntity<ApiResponse> getUserOrders(@PathVariable(name = "userId") Long userId) {
    try {
      List<OrderDto> orders = iOrderService.getUserOrders(userId);
      return ResponseEntity.ok(new ApiResponse("Get Order Success!", orders));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Oops!", e.getMessage()));
    }
  }

}
