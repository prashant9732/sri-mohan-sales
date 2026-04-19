package com.srimohansalesservices.repository;


import com.srimohansalesservices.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContaining(String name);
    List<Product> findByAvailableTrue();
    List<Product> findByBrand(String brand);
    List<Product> findByPriceLessThan(Double price);
    List<Product> findByPriceBetween(Double min, Double max);
}