package ru.hpclab.hl.module1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hpclab.hl.module1.model.Customer;
import ru.hpclab.hl.module1.model.Product;
import ru.hpclab.hl.module1.model.Sale;
import ru.hpclab.hl.module1.repository.CustomerRepository;
import ru.hpclab.hl.module1.repository.ProductRepository;
import ru.hpclab.hl.module1.repository.SaleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final SaleRepository saleRepository;

    @Autowired
    public SaleService(ProductRepository productRepository, CustomerRepository customerRepository, SaleRepository saleRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.saleRepository = saleRepository;
    }

    public Sale createSale(Long productId, Long customerId, double weight, LocalDate date) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
                
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + customerId));

        double totalCost = Math.round(product.getPricePerKg() * weight * 100.0) / 100.0;
        Sale sale = new Sale(null, product, customer, date, weight, totalCost);

        return saleRepository.save(sale);
    }

    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    public List<Sale> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return saleRepository.findByDateBetween(startDate, endDate);
    }

    public Optional<Sale> findById(Long id) {
        return saleRepository.findById(id);
    }

    public Sale save(Sale sale) {
        return saleRepository.save(sale);
    }

    public void deleteById(Long id) {
        saleRepository.deleteById(id);
    }

    public void deleteAll() {
        saleRepository.deleteAll();
    }
}