package com.srimohansalesservices.service;


import com.srimohansalesservices.dto.OrderRequest;
import com.srimohansalesservices.entity.*;
import com.srimohansalesservices.repository.OrderRepository;
import com.srimohansalesservices.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    // Order place karo
    @Transactional
    public Order placeOrder(User user, OrderRequest request) {

        List<OrderItem> items = new ArrayList<>();
        double total = 0;

        for (OrderRequest.OrderItemRequest itemReq
                : request.getItems()) {

            Product product = productRepository
                    .findById(itemReq.getProductId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Product not found!"));

            // Stock check karo
            if (product.getStock()
                    < itemReq.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for: " + product.getName());
            }

            // Stock kam karo

            product.setStock(
                    product.getStock()
                            - itemReq.getQuantity());
            productRepository.save(product);

            OrderItem item = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .price(product.getPrice())
                    .build();

            items.add(item);
            total += product.getPrice()
                    * itemReq.getQuantity();
        }

        // Order banao

        Order order = Order.builder()
                .user(user)
                .totalAmount(total)
                .deliveryAddress(
                        request.getDeliveryAddress())
                .status(OrderStatus.PENDING)
                .build();

        order = orderRepository.save(order);

        // Items ko order se link karo
        for (OrderItem item : items) {
            item.setOrder(order);
        }
        order.setItems(items);
        orderRepository.save(order);

        // Cart clear karo
        cartService.clearCart(user);

        return order;
    }

    // User ke orders

    public List<Order> getUserOrders(User user) {
        return orderRepository
                .findByUserOrderByCreatedAtDesc(user);
    }

    // Order by ID


    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found: " + id));
    }

    // Status update (Admin)

    public Order updateStatus(Long id, OrderStatus status) {
        Order order = getById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    // Sab orders (Admin)

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}