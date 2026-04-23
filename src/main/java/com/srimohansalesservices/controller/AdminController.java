package com.srimohansalesservices.controller;

import com.srimohansalesservices.entity.Order;
import com.srimohansalesservices.entity.OrderStatus;
import com.srimohansalesservices.entity.Product;
import com.srimohansalesservices.entity.User;
import com.srimohansalesservices.service.OrderService;
import com.srimohansalesservices.service.ProductService;
import com.srimohansalesservices.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // ─── DASHBOARD ───────────────────────────

    // GET Dashboard Stats
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>>
    getDashboard() {

        Map<String, Object> stats = new HashMap<>();

        List<Order> allOrders =
                orderService.getAllOrders();

        // Total orders

        stats.put("totalOrders", allOrders.size());

        // Total revenue

        double revenue = allOrders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();
        stats.put("totalRevenue", revenue);

        // Pending orders

        long pending = allOrders.stream()
                .filter(o -> o.getStatus()
                        == OrderStatus.PENDING)
                .count();
        stats.put("pendingOrders", pending);

        // Total products
        stats.put("totalProducts",
                productService.getAllProducts().size());

        // Total users
        stats.put("totalUsers",
                userService.getAllUsers().size());

        return ResponseEntity.ok(stats);
    }

    // ─── PRODUCT MANAGEMENT ──────────────────

    // GET All Products
    @GetMapping("/products")
    public ResponseEntity<List<Product>>
    getAllProducts() {
        return ResponseEntity.ok(
                productService.getAllProducts());
    }

    // POST Add Product
    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(
            @RequestBody Product product) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.save(product));
    }

    // PUT Update Product
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {
        return ResponseEntity.ok(
                productService.update(id, product));
    }

    // DELETE Product
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(
                "Product deleted successfully!");
    }

    // ─── ORDER MANAGEMENT ────────────────────

    // GET All Orders
    @GetMapping("/orders")
    public ResponseEntity<List<Order>>
    getAllOrders() {
        return ResponseEntity.ok(
                orderService.getAllOrders());
    }

    // GET Order by ID
    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrder(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                orderService.getById(id));
    }

    // PUT Update Order Status
    @PutMapping("/orders/{id}/status")
    public ResponseEntity<Order> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(
                orderService.updateStatus(id, status));
    }

    // GET Orders by Status
    @GetMapping("/orders/status/{status}")
    public ResponseEntity<List<Order>>
    getByStatus(
            @PathVariable OrderStatus status) {
        return ResponseEntity.ok(
                orderService.getByStatus(status));
    }

    // ─── USER MANAGEMENT ─────────────────────

    // GET All Users
    @GetMapping("/users")
    public ResponseEntity<List<User>>
    getAllUsers() {
        return
                ResponseEntity.ok(
                userService.getAllUsers());
    }

    // GET User by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                userService.findById(id));
    }

    // DELETE User
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(
                "User deleted successfully!");
    }
}