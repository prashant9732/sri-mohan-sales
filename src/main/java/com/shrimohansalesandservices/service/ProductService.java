package com.shrimohansalesandservices.service;

import com.shrimohansalesandservices.entity.Product;
import com.shrimohansalesandservices.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Sab products

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ID se product

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found: " + id));
    }

    // Search

    public List<Product> search(String keyword) {
        return productRepository
                .findByNameContaining(keyword);
    }

    // Available products

    public List<Product> getAvailable() {
        return productRepository
                .findByAvailableTrue();
    }

    // Save product (Admin)

    public Product save(Product product) {
        return productRepository.save(product);
    }

    // Update product (Admin)

    public Product update(Long id, Product product) {
        Product existing = getById(id);
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        existing.setBrand(product.getBrand());
        existing.setImageUrl(product.getImageUrl());
        existing.setAvailable(product.getAvailable());
        return productRepository.save(existing);
    }

    // Delete product (Admin)

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}