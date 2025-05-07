package ru.hpclab.hl.module1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.hpclab.hl.module1.model.Product;
import ru.hpclab.hl.module1.model.Customer;
import ru.hpclab.hl.module1.model.Sale;
import ru.hpclab.hl.module1.repository.ProductRepository;
import ru.hpclab.hl.module1.repository.CustomerRepository;
import ru.hpclab.hl.module1.repository.SaleRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final SaleRepository saleRepository;

    @Value("${kafka.topic}")
    private String topic;

    public void sendMessage(String message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
        
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Message sent successfully: {}", message);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Failed to send message: {}", message, ex);
            }
        });
    }

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.groupId}")
    public void listen(String message, Acknowledgment ack) {
        try {
            // Парсим JSON сообщение
            Map<String, Object> messageMap = objectMapper.readValue(message, Map.class);
            String type = (String) messageMap.get("type");
            Map<String, Object> data = (Map<String, Object>) messageMap.get("data");

            log.info("Received message: {}", message);

            switch (type) {
                case "product":
                    handleProduct(data);
                    break;
                case "customer":
                    handleCustomer(data);
                    break;
                case "sale":
                    handleSale(data);
                    break;
                default:
                    log.warn("Unknown message type: {}", type);
            }

            // Подтверждаем обработку сообщения
            ack.acknowledge();
            
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }

    private void handleProduct(Map<String, Object> data) {
        Product product = new Product();
        product.setName((String) data.get("name"));
        product.setCategory((String) data.get("category"));
        product.setPricePerKg(((Number) data.get("pricePerKg")).doubleValue());
        productRepository.save(product);
        log.info("Saved product: {}", product.getName());
    }

    private void handleCustomer(Map<String, Object> data) {
        Customer customer = new Customer();
        customer.setFullName((String) data.get("fullName"));
        customer.setPhone((String) data.get("phone"));
        customer.setHasDiscountCard((Boolean) data.get("hasDiscountCard"));
        customerRepository.save(customer);
        log.info("Saved customer: {}", customer.getFullName());
    }

    private void handleSale(Map<String, Object> data) {
        Sale sale = new Sale();
        sale.setProductId(((Number) data.get("productId")).longValue());
        sale.setCustomerId(((Number) data.get("customerId")).longValue());
        sale.setWeight(((Number) data.get("weight")).doubleValue());
        sale.setDate(LocalDateTime.parse((String) data.get("date")));
        saleRepository.save(sale);
        log.info("Saved sale: productId={}, customerId={}", sale.getProductId(), sale.getCustomerId());
    }
} 