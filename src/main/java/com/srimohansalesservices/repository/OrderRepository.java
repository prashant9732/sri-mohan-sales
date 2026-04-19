package com.srimohansalesservices.repository;

import com.srimohansalesservices.entity.Order;
import com.srimohansalesservices.entity.OrderStatus;
import com.srimohansalesservices.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository
        extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByUserOrderByCreatedAtDesc(User user);
}