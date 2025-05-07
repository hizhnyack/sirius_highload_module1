package ru.hpclab.hl.module1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hpclab.hl.module1.model.Sale;
import ru.hpclab.hl.module1.repository.SaleRepository;
import ru.hpclab.hl.module1.repository.ProductRepository;
import ru.hpclab.hl.module1.repository.CustomerRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService {
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public Sale createSale(Long productId, Long customerId, double weight) {
        // Проверяем существование продукта и клиента
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("Product not found with id: " + productId);
        }
        if (!customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException("Customer not found with id: " + customerId);
        }

        Sale sale = new Sale();
        sale.setProductId(productId);
        sale.setCustomerId(customerId);
        sale.setWeight(weight);
        sale.setDate(LocalDateTime.now());
        
        return saleRepository.save(sale);
    }

    public List<Sale> getSalesByDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        return saleRepository.findAll().stream()
                .filter(sale -> !sale.getDate().isBefore(startDateTime) && !sale.getDate().isAfter(endDateTime))
                .toList();
    }

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public Sale getSaleById(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found with id: " + id));
    }
}