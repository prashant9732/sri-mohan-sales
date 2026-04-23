package com.shrimohansalesandservices.service;

import com.shrimohansalesandservices.entity.Cart;
import com.shrimohansalesandservices.entity.Product;
import com.shrimohansalesandservices.entity.User;
import com.shrimohansalesandservices.repository.CartRepository;
import com.shrimohansalesandservices.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    // Cart items get karo
    public List<Cart> getCartItems(User user) {
        return cartRepository.findByUser(user);
    }

    // Cart mein add karo
    public Cart addToCart(User user,
                          Long productId,
                          Integer quantity) {

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found!"));

        // Already cart mein hai?
        Optional<Cart> existing =
                cartRepository.findByUserAndProductId(
                        user, productId);

        if (existing.isPresent()) {
            Cart cart = existing.get();
            cart.setQuantity(
                    cart.getQuantity() + quantity);
            return cartRepository.save(cart);
        }

        // Naya cart item

        Cart cart = Cart.builder()
                .user(user)
                .product(product)
                .quantity(quantity)
                .build();

        return cartRepository.save(cart);
    }

    // Quantity update karo

    public Cart updateQuantity(Long cartId,
                               Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cart item not found!"));
        cart.setQuantity(quantity);
        return cartRepository.save(cart);
    }

    // Item remove karo

    public void removeItem(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    // Cart clear karo

    public void clearCart(User user) {
        cartRepository.deleteByUser(user);
    }

    // Total calculate karo

    public Double getTotal(User user) {
        List<Cart> items = getCartItems(user);
        return items.stream()
                .mapToDouble(item ->
                        item.getProduct().getPrice()
                                * item.getQuantity())
                .sum();
    }
}