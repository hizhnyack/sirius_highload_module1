package ru.hpclab.hl.module1.service;

import org.springframework.stereotype.Service;
import ru.hpclab.hl.module1.model.Customer;
import ru.hpclab.hl.module1.model.Product;
import ru.hpclab.hl.module1.model.Sale;
import ru.hpclab.hl.module1.repository.CustomerRepository;
import ru.hpclab.hl.module1.repository.ProductRepository;
import ru.hpclab.hl.module1.repository.SaleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class SaleService {
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final SaleRepository saleRepository;

    public SaleService(ProductRepository productRepository, CustomerRepository customerRepository, SaleRepository saleRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.saleRepository = saleRepository;
    }

    public void createSale(Long productId, Long customerId, double weight, LocalDate date) {
        Optional<Product> productOpt = productRepository.findById(productId);
        Optional<Customer> customerOpt = customerRepository.findById(customerId);

        if (productOpt.isEmpty() || customerOpt.isEmpty()) {
            throw new IllegalArgumentException("Product or Customer not found");
        }

        Product product = productOpt.get();
        Customer customer = customerOpt.get();

        double totalCost = product.getPricePerKg() * weight;
        Sale sale = new Sale(null, product, customer, date, weight, totalCost);

        saleRepository.save(sale);
    }

    public double calculateAverageWeightLastMonth(Long productId) {
        LocalDate now = LocalDate.now();
        LocalDate lastMonth = now.minusMonths(1);

        List<Sale> salesLastMonth = saleRepository.findByDateBetween(lastMonth, now).stream()
                .filter(sale -> sale.getProduct().getId().equals(productId))
                .toList();

        if (salesLastMonth.isEmpty()) {
            return 0;
        }

        double totalWeight = salesLastMonth.stream()
                .mapToDouble(Sale::getWeight)
                .sum();

        double average = totalWeight / salesLastMonth.size();
        return Math.round(average * 100.0) / 100.0;  // Округление до сотых
    }

    public Map<Long, Double> calculateAverageWeightLastMonth() {
        LocalDate now = LocalDate.now();
        LocalDate lastMonth = now.minusMonths(1);

        List<Sale> salesLastMonth = saleRepository.findByDateBetween(lastMonth, now);

        return salesLastMonth.stream()
                .collect(Collectors.groupingBy(
                        sale -> sale.getProduct().getId(),
                        Collectors.collectingAndThen(
                                Collectors.averagingDouble(Sale::getWeight),
                                avg -> Math.round(avg * 100.0) / 100.0  // Округление до сотых
                        )
                ));
    }

    public List<Sale> findAll() {
        return saleRepository.findAll();
    }
}