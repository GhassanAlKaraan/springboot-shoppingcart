package com.example.dreamshops.services.order;

import java.util.List;

import com.example.dreamshops.dto.OrderDto;
import com.example.dreamshops.models.Order;

public interface IOrderService {
  Order placeOrder(Long userId);

  OrderDto getOrder(Long orderId);

  List<OrderDto> getUserOrders(Long userId);
}
