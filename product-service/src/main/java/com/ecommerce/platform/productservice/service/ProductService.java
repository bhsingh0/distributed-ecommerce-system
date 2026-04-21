package com.ecommerce.platform.productservice.service;

import com.ecommerce.platform.productservice.entity.Product;
import com.ecommerce.platform.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Product create(Product product) {
        return repo.save(product);
    }

    public Product getProductById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
}