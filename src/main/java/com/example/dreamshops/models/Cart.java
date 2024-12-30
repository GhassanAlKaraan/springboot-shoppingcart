package com.example.dreamshops.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private BigDecimal totalAmount = BigDecimal.ZERO;

  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<CartItem> items = new HashSet<>();

  @JsonIgnore
  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  public void addItem(CartItem item) {
    this.items.add(item);
    item.setCart(this);
    updateTotalAmount();
  }

  public void removeItem(CartItem item) {
    this.items.remove(item);
    item.setCart(null);
    updateTotalAmount();
  }

  public void clearItems() {
    this.items.clear();
    updateTotalAmount();
  }

  private void updateTotalAmount() {
    this.totalAmount = items.stream().map(item -> {
      BigDecimal unitPrice = item.getUnitPrice();
      if (unitPrice == null) {
        return BigDecimal.ZERO;
      }
      return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
    }).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

}