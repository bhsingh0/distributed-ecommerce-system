package com.ecommerce.platform.productservice.controller;

import com.ecommerce.platform.productservice.entity.Product;
import com.ecommerce.platform.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Product saved = service.create(product);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // PLP - Product Listing
    @GetMapping
    public List<Product> getAllProducts() {
        return service.getAllProducts();
    }

    // PDP - Product Detail Page
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return service.getProductById(id);
    }
}