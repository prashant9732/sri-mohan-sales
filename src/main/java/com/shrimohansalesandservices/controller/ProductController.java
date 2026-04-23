package com.shrimohansalesandservices.controller;

import com.shrimohansalesandservices.entity.Product;
import com.shrimohansalesandservices.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    // GET All - Public
    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(
                productService.getAllProducts());
    }

    // GET By ID - Public
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                productService.getById(id));
    }

    // GET Search - Public
    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(
            @RequestParam String keyword) {
        return ResponseEntity.ok(
                productService.search(keyword));
    }

    // POST Add - Admin Only
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> add(
            @RequestBody Product product) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.save(product));
    }

    // PUT Update - Admin Only
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> update(
            @PathVariable Long id,
            @RequestBody Product product) {
        return ResponseEntity.ok(
                productService.update(id, product));
    }

    // DELETE - Admin Only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(
            @PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}