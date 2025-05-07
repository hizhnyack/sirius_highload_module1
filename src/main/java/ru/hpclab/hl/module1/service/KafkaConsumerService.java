package ru.hpclab.hl.module1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.hpclab.hl.module1.model.Customer;
import ru.hpclab.hl.module1.model.Product;
import ru.hpclab.hl.module1.model.Sale;
import ru.hpclab.hl.module1.repository.CustomerRepository;
import ru.hpclab.hl.module1.repository.ProductRepository;
import ru.hpclab.hl.module1.repository.SaleRepository;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class KafkaConsumerService {

    @Value("${kafka.topic}")
    private String topic;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.groupId}")
    public void listen(String message) {
        try {
            log.info("Received message from topic {}: {}", topic, message);
            
            Map<String, Object> messageMap = objectMapper.readValue(message, Map.class);
            String type = (String) messageMap.get("type");
            Map<String, Object> data = (Map<String, Object>) messageMap.get("data");

            switch (type) {
                case "customer":
                    saveCustomer(data);
                    break;
                case "product":
                    saveProduct(data);
                    break;
                case "sale":
                    saveSale(data);
                    break;
                default:
                    log.warn("Unknown message type: {}", type);
            }
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }

    private void saveCustomer(Map<String, Object> data) {
        try {
            Customer customer = new Customer();
            customer.setFullName((String) data.get("fullName"));
            customer.setPhone((String) data.get("phone"));
            customer.setHasDiscountCard((Boolean) data.get("hasDiscountCard"));
            customerRepository.save(customer);
            log.info("Saved customer: {}", customer.getFullName());
        } catch (Exception e) {
            log.error("Error saving customer: {}", data, e);
        }
    }

    private void saveProduct(Map<String, Object> data) {
        try {
            Product product = new Product();
            product.setName((String) data.get("name"));
            product.setCategory((String) data.get("category"));
            product.setPricePerKg(((Number) data.get("pricePerKg")).doubleValue());
            productRepository.save(product);
            log.info("Saved product: {}", product.getName());
        } catch (Exception e) {
            log.error("Error saving product: {}", data, e);
        }
    }

    private void saveSale(Map<String, Object> data) {
        try {
            Long productId = ((Number) data.get("productId")).longValue();
            Long customerId = ((Number) data.get("customerId")).longValue();
            
            Optional<Product> productOpt = productRepository.findById(productId);
            Optional<Customer> customerOpt = customerRepository.findById(customerId);
            
            if (productOpt.isPresent() && customerOpt.isPresent()) {
                Sale sale = new Sale();
                sale.setProduct(productOpt.get());
                sale.setCustomer(customerOpt.get());
                sale.setWeight(((Number) data.get("weight")).doubleValue());
                sale.setDate(java.time.LocalDate.parse((String) data.get("date")));
                
                // Рассчитываем общую стоимость
                double totalCost = sale.getWeight() * productOpt.get().getPricePerKg();
                sale.setTotalCost(totalCost);
                
                saleRepository.save(sale);
                log.info("Saved sale: product={}, customer={}, totalCost={}", 
                    productOpt.get().getName(), 
                    customerOpt.get().getFullName(),
                    totalCost);
            } else {
                log.error("Product or Customer not found for sale: productId={}, customerId={}", 
                    productId, customerId);
            }
        } catch (Exception e) {
            log.error("Error saving sale: {}", data, e);
        }
    }
} 