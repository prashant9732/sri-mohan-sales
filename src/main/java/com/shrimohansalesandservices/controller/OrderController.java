package com.shrimohansalesandservices.controller;

import com.shrimohansalesandservices.dto.OrderRequest;
import com.shrimohansalesandservices.entity.Order;
import com.shrimohansalesandservices.entity.OrderStatus;
import com.shrimohansalesandservices.entity.User;
import com.shrimohansalesandservices.service.OrderService;
import com.shrimohansalesandservices.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // POST Place Order
    @PostMapping
    public ResponseEntity<Order> placeOrder(
            @RequestBody OrderRequest request,
            Authentication auth) {
        User user = userService
                .findByEmail(auth.getName())
                .orElseThrow();
        return ResponseEntity.ok(
                orderService.placeOrder(user, request));
    }

    // GET My Orders
    @GetMapping("/my")
    public ResponseEntity<List<Order>> myOrders(
            Authentication auth) {
        User user = userService
                .findByEmail(auth.getName())
                .orElseThrow();
        return ResponseEntity.ok(
                orderService.getUserOrders(user));
    }

    // GET Order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                orderService.getById(id));
    }

    // GET All Orders - Admin
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> allOrders() {
        return ResponseEntity.ok(
                orderService.getAllOrders());
    }

    // PUT Update Status - Admin
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(
                orderService.updateStatus(id, status));
    }
}