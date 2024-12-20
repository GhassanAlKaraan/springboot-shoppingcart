package com.example.dreamshops.services.order;

import com.example.dreamshops.dto.OrderDto;
import com.example.dreamshops.enums.OrderStatus;
import com.example.dreamshops.exceptions.ResourceNotFoundException;
import com.example.dreamshops.models.Cart;
import com.example.dreamshops.models.Order;
import com.example.dreamshops.models.OrderItem;
import com.example.dreamshops.models.Product;
import com.example.dreamshops.repositories.OrderRepository;
import com.example.dreamshops.repositories.ProductRepository;
import com.example.dreamshops.services.cart.ICartService;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final ICartService iCartService;
  private final ModelMapper modelMapper;

  @Transactional
  @Override
  public Order placeOrder(Long userId) {
    Cart cart = iCartService.getCartByUserId(userId);
    Order order = createOrder(cart);
    List<OrderItem> orderItemList = createOrderItems(order, cart);
    order.setItems(new HashSet<>(orderItemList));
    order.setTotalAmount(calculateTotalAmount(orderItemList));
    Order savedOrder = orderRepository.save(order);
    iCartService.clearCart(cart.getId());
    return savedOrder;
  }

  private Order createOrder(Cart cart) {
    Order order = new Order();
    order.setUser(cart.getUser());
    order.setOrderStatus(OrderStatus.PENDING);
    order.setOrderDate(LocalDate.now());
    return order;
  }

  private List<OrderItem> createOrderItems(Order order, Cart cart) {
    return cart.getItems().stream().map(cartItem -> {
      Product product = cartItem.getProduct();
      product.setInventory(product.getInventory() - cartItem.getQuantity());
      productRepository.save(product);
      return new OrderItem(
          order,
          product,
          cartItem.getQuantity(),
          cartItem.getUnitPrice());
    }).toList();

  }

  private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
    return orderItemList
        .stream()
        .map(item -> item.getPrice()
            .multiply(new BigDecimal(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  @Override
  public OrderDto getOrder(Long orderId) {
    return orderRepository.findById(orderId)
        .map(this::convertToDto)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
  }

  @Override
  public List<OrderDto> getUserOrders(Long userId) {
    List<Order> orders = orderRepository.findByUserId(userId);
    return orders.stream().map(this::convertToDto).toList();
  }

  private OrderDto convertToDto(Order order) {
    return modelMapper.map(order, OrderDto.class);
  }
}