package ru.hpclab.hl.module1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.model.Product;
import ru.hpclab.hl.module1.service.ProductService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> findAll() {
        long start = System.currentTimeMillis();
        try {
            return productService.findAll();
        } finally {
            ru.hpclab.hl.module1.service.ObservabilityService.recordTiming("product.findAll", System.currentTimeMillis() - start);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        long start = System.currentTimeMillis();
        try {
            Optional<Product> product = productService.findById(id);
            return product.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } finally {
            ru.hpclab.hl.module1.service.ObservabilityService.recordTiming("product.findById", System.currentTimeMillis() - start);
        }
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        long start = System.currentTimeMillis();
        try {
            return productService.save(product);
        } finally {
            ru.hpclab.hl.module1.service.ObservabilityService.recordTiming("product.create", System.currentTimeMillis() - start);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        long start = System.currentTimeMillis();
        try {
            Optional<Product> product = productService.findById(id);
            if (product.isPresent()) {
                productService.deleteById(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } finally {
            ru.hpclab.hl.module1.service.ObservabilityService.recordTiming("product.delete", System.currentTimeMillis() - start);
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAll() {
        long start = System.currentTimeMillis();
        try {
            productService.deleteAll();
            return ResponseEntity.ok().build();
        } finally {
            ru.hpclab.hl.module1.service.ObservabilityService.recordTiming("product.deleteAll", System.currentTimeMillis() - start);
        }
    }
}