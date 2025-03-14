package ru.hpclab.hl.module1.repository;

import ru.hpclab.hl.module1.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository {
    private static final List<Product> products = new ArrayList<>();

    public List<Product> findAll() {
        return products;
    }

    public void save(Product product) {
        products.add(product);
    }

    public Optional<Product> findById(Long id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }
}