package com.example.dreamshops.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dreamshops.dto.OrderDto;
import com.example.dreamshops.exceptions.ResourceNotFoundException;
import com.example.dreamshops.models.Order;
import com.example.dreamshops.responses.ApiResponse;
import com.example.dreamshops.services.order.IOrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

  private final IOrderService iOrderService;

  @PostMapping("/order")
  public ResponseEntity<ApiResponse> createOrder(@RequestParam(name = "userId") Long userId) {
    try {
      Order order = iOrderService.placeOrder(userId);
      return ResponseEntity.ok(new ApiResponse("Item Order Success!", order));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Error Occured!", e.getMessage()));
    }
  }

  @GetMapping("/{orderId}/order")
  public ResponseEntity<ApiResponse> getOrderById(@PathVariable(name = "orderId") Long orderId) {
    try {
      OrderDto order = iOrderService.getOrder(orderId);
      return ResponseEntity.ok(new ApiResponse("Item Order Success!", order));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Oops!", e.getMessage()));
    }
  }

  @GetMapping("/{userId}/order")
  public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
    try {
      List<OrderDto> order = iOrderService.getUserOrders(userId);
      return ResponseEntity.ok(new ApiResponse("Item Order Success!", order));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Oops!", e.getMessage()));
    }
  }

}
