package com.shrimohansalesandservices.controller;

import com.shrimohansalesandservices.entity.Cart;
import com.shrimohansalesandservices.entity.User;
import com.shrimohansalesandservices.service.CartService;
import com.shrimohansalesandservices.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    // GET Cart Items
    @GetMapping
    public ResponseEntity<List<Cart>> getCart(
            Authentication auth) {
        User user = userService
                .findByEmail(auth.getName())
                .orElseThrow();
        return ResponseEntity.ok(
                cartService.getCartItems(user));
    }

    // POST Add to Cart
    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(
            @RequestBody Map<String, Object> body,
            Authentication auth) {
        User user = userService
                .findByEmail(auth.getName())
                .orElseThrow();
        Long productId = Long.valueOf(
                body.get("productId").toString());
        Integer quantity = Integer.valueOf(
                body.get("quantity").toString());
        return ResponseEntity.ok(
                cartService.addToCart(
                        user, productId, quantity));
    }

    // PUT Update Quantity
    @PutMapping("/{cartId}")
    public ResponseEntity<Cart> updateQty(
            @PathVariable Long cartId,
            @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(
                cartService.updateQuantity(
                        cartId, body.get("quantity")));
    }

    // DELETE Remove Item
    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> removeItem(
            @PathVariable Long cartId) {
        cartService.removeItem(cartId);
        return ResponseEntity.noContent().build();
    }

    // DELETE Clear Cart
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(
            Authentication auth) {
        User user = userService
                .findByEmail(auth.getName())
                .orElseThrow();
        cartService.clearCart(user);
        return ResponseEntity.ok(
                "Cart cleared!");
    }

    // GET Cart Total
    @GetMapping("/total")
    public ResponseEntity<Double> getTotal(
            Authentication auth) {
        User user = userService
                .findByEmail(auth.getName())
                .orElseThrow();
        return ResponseEntity.ok(
                cartService.getTotal(user));
    }
}