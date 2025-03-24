//package ru.hpclab.hl.module1;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import ru.hpclab.hl.module1.model.Customer;
//import ru.hpclab.hl.module1.model.Product;
//import ru.hpclab.hl.module1.model.Sale;
//import ru.hpclab.hl.module1.repository.CustomerRepository;
//import ru.hpclab.hl.module1.repository.ProductRepository;
//import ru.hpclab.hl.module1.repository.SaleRepository;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class DataInitializer implements CommandLineRunner {
//
//    private final CustomerRepository customerRepository;
//    private final ProductRepository productRepository;
//    private final SaleRepository saleRepository;
//
//    public DataInitializer(CustomerRepository customerRepository,
//                           ProductRepository productRepository,
//                           SaleRepository saleRepository) {
//        this.customerRepository = customerRepository;
//        this.productRepository = productRepository;
//        this.saleRepository = saleRepository;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        // Создаем список покупателей
//        List<Customer> customers = new ArrayList<>();
//        customers.add(new Customer(1L, "Иванов Иван", "123456789", true));
//        customers.add(new Customer(2L, "Петров Петр", "987654321", false));
//        customers.add(new Customer(3L, "Сидоров Алексей", "111222333", true));
//        customers.add(new Customer(4L, "Козлова Анна", "444555666", false));
//        customers.add(new Customer(5L, "Морозов Дмитрий", "777888999", true));
//        customers.add(new Customer(6L, "Никитина Елена", "000111222", false));
//        customers.add(new Customer(7L, "Волков Сергей", "333444555", true));
//        customers.add(new Customer(8L, "Зайцева Ольга", "666777888", false));
//        customers.add(new Customer(9L, "Павлов Игорь", "999000111", true));
//        customers.add(new Customer(10L, "Семенова Мария", "222333444", false));
//        customers.add(new Customer(11L, "Григорьев Андрей", "555666777", true));
//        customers.add(new Customer(12L, "Белова Татьяна", "888999000", false));
//        customers.add(new Customer(13L, "Комаров Виктор", "123123123", true));
//        customers.add(new Customer(14L, "Орлова Людмила", "456456456", false));
//        customers.add(new Customer(15L, "Соколов Николай", "789789789", true));
//        customers.add(new Customer(16L, "Михайлова Екатерина", "321321321", false));
//        customers.add(new Customer(17L, "Новиков Павел", "654654654", true));
//        customers.add(new Customer(18L, "Федорова Ирина", "987987987", false));
//        customers.add(new Customer(19L, "Кузнецов Артем", "135135135", true));
//        customers.add(new Customer(20L, "Антонова Светлана", "246246246", false));
//
//        // Сохраняем покупателей через цикл
//        for (Customer customer : customers) {
//            customerRepository.save(customer);
//        }
//
//        // Создаем список продуктов
//        List<Product> products = new ArrayList<>();
//        products.add(new Product(1L, "Яблоки", "Фрукты", 100.0));
//        products.add(new Product(2L, "Картофель", "Овощи", 50.0));
//        products.add(new Product(3L, "Морковь", "Овощи", 70.0));
//        products.add(new Product(4L, "Бананы", "Фрукты", 120.0));
//        products.add(new Product(5L, "Помидоры", "Овощи", 90.0));
//        products.add(new Product(6L, "Огурцы", "Овощи", 80.0));
//        products.add(new Product(7L, "Груши", "Фрукты", 110.0));
//        products.add(new Product(8L, "Лук", "Овощи", 40.0));
//        products.add(new Product(9L, "Чеснок", "Овощи", 60.0));
//        products.add(new Product(10L, "Апельсины", "Фрукты", 130.0));
//        products.add(new Product(11L, "Капуста", "Овощи", 30.0));
//        products.add(new Product(12L, "Свекла", "Овощи", 55.0));
//        products.add(new Product(13L, "Персики", "Фрукты", 140.0));
//        products.add(new Product(14L, "Сливы", "Фрукты", 95.0));
//        products.add(new Product(15L, "Кабачки", "Овощи", 65.0));
//        products.add(new Product(16L, "Баклажаны", "Овощи", 85.0));
//        products.add(new Product(17L, "Виноград", "Фрукты", 150.0));
//        products.add(new Product(18L, "Клубника", "Фрукты", 200.0));
//        products.add(new Product(19L, "Малина", "Фрукты", 180.0));
//        products.add(new Product(20L, "Арбуз", "Фрукты", 300.0));
//
//        // Сохраняем продукты через цикл
//        for (Product product : products) {
//            productRepository.save(product);
//        }
//
//        // Создаем список продаж
//        List<Sale> sales = new ArrayList<>();
//        sales.add(new Sale(null, products.get(0), customers.get(0), LocalDate.now().minusDays(10), 2.5, 250.0));
//        sales.add(new Sale(null, products.get(1), customers.get(1), LocalDate.now().minusDays(5), 3.0, 150.0));
//        sales.add(new Sale(null, products.get(0), customers.get(0), LocalDate.now().minusDays(3), 1.5, 150.0));
//        sales.add(new Sale(null, products.get(2), customers.get(1), LocalDate.now().minusDays(1), 4.0, 280.0));
//        sales.add(new Sale(null, products.get(3), customers.get(2), LocalDate.now().minusDays(15), 5.0, 600.0));
//        sales.add(new Sale(null, products.get(4), customers.get(3), LocalDate.now().minusDays(12), 2.0, 180.0));
//        sales.add(new Sale(null, products.get(5), customers.get(4), LocalDate.now().minusDays(8), 3.5, 280.0));
//        sales.add(new Sale(null, products.get(6), customers.get(5), LocalDate.now().minusDays(7), 4.0, 440.0));
//        sales.add(new Sale(null, products.get(7), customers.get(6), LocalDate.now().minusDays(6), 1.0, 40.0));
//        sales.add(new Sale(null, products.get(8), customers.get(7), LocalDate.now().minusDays(4), 2.0, 120.0));
//        sales.add(new Sale(null, products.get(9), customers.get(8), LocalDate.now().minusDays(9), 3.0, 390.0));
//        sales.add(new Sale(null, products.get(10), customers.get(9), LocalDate.now().minusDays(11), 4.0, 120.0));
//        sales.add(new Sale(null, products.get(11), customers.get(10), LocalDate.now().minusDays(13), 2.5, 137.5));
//        sales.add(new Sale(null, products.get(12), customers.get(11), LocalDate.now().minusDays(14), 1.0, 140.0));
//        sales.add(new Sale(null, products.get(13), customers.get(12), LocalDate.now().minusDays(16), 3.0, 285.0));
//        sales.add(new Sale(null, products.get(14), customers.get(13), LocalDate.now().minusDays(17), 2.0, 130.0));
//        sales.add(new Sale(null, products.get(15), customers.get(14), LocalDate.now().minusDays(18), 1.5, 127.5));
//        sales.add(new Sale(null, products.get(16), customers.get(15), LocalDate.now().minusDays(19), 2.0, 300.0));
//        sales.add(new Sale(null, products.get(17), customers.get(16), LocalDate.now().minusDays(20), 1.0, 200.0));
//        sales.add(new Sale(null, products.get(18), customers.get(17), LocalDate.now().minusDays(21), 3.0, 540.0));
//        sales.add(new Sale(null, products.get(19), customers.get(18), LocalDate.now().minusDays(22), 1.0, 300.0));
//        sales.add(new Sale(null, products.get(0), customers.get(19), LocalDate.now().minusDays(23), 2.0, 200.0));
//        sales.add(new Sale(null, products.get(1), customers.get(0), LocalDate.now().minusDays(24), 3.0, 150.0));
//        sales.add(new Sale(null, products.get(2), customers.get(1), LocalDate.now().minusDays(25), 4.0, 280.0));
//        sales.add(new Sale(null, products.get(3), customers.get(2), LocalDate.now().minusDays(26), 5.0, 600.0));
//        sales.add(new Sale(null, products.get(4), customers.get(3), LocalDate.now().minusDays(27), 2.0, 180.0));
//        sales.add(new Sale(null, products.get(5), customers.get(4), LocalDate.now().minusDays(28), 3.5, 280.0));
//        sales.add(new Sale(null, products.get(6), customers.get(5), LocalDate.now().minusDays(29), 4.0, 440.0));
//        sales.add(new Sale(null, products.get(7), customers.get(6), LocalDate.now().minusDays(30), 1.0, 40.0));
//        sales.add(new Sale(null, products.get(8), customers.get(7), LocalDate.now().minusDays(31), 2.0, 120.0));
//
//        // Сохраняем продажи через цикл
//        for (Sale sale : sales) {
//            saleRepository.save(sale);
//        }
//
//        System.out.println("Тестовые данные успешно добавлены!");
//    }
//}