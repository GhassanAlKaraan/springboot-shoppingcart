package com.example.dreamshops.repositories;

import com.example.dreamshops.models.Order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
