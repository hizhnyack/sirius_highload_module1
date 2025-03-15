package ru.hpclab.hl.module1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.hpclab.hl.module1.model.Customer;
import ru.hpclab.hl.module1.model.Product;
import ru.hpclab.hl.module1.model.Sale;
import ru.hpclab.hl.module1.repository.CustomerRepository;
import ru.hpclab.hl.module1.repository.ProductRepository;
import ru.hpclab.hl.module1.repository.SaleRepository;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;

    public DataInitializer(CustomerRepository customerRepository,
                           ProductRepository productRepository,
                           SaleRepository saleRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.saleRepository = saleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Добавляем покупателей
        Customer customer1 = new Customer(1L, "Иванов Иван", "123456789", true);
        Customer customer2 = new Customer(2L, "Петров Петр", "987654321", false);
        customerRepository.save(customer1);
        customerRepository.save(customer2);

        // Добавляем продукты
        Product product1 = new Product(1L, "Яблоки", "Фрукты", 100.0);
        Product product2 = new Product(2L, "Картофель", "Овощи", 50.0);
        Product product3 = new Product(3L, "Морковь", "Овощи", 70.0);
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        // Добавляем продажи
        Sale sale1 = new Sale(null, product1, customer1, LocalDate.now().minusDays(10), 2.5, 250.0);
        Sale sale2 = new Sale(null, product2, customer2, LocalDate.now().minusDays(5), 3.0, 150.0);
        Sale sale3 = new Sale(null, product1, customer1, LocalDate.now().minusDays(3), 1.5, 150.0);
        Sale sale4 = new Sale(null, product3, customer2, LocalDate.now().minusDays(1), 4.0, 280.0);
        saleRepository.save(sale1);
        saleRepository.save(sale2);
        saleRepository.save(sale3);
        saleRepository.save(sale4);

        System.out.println("Тестовые данные успешно добавлены!");
    }
}