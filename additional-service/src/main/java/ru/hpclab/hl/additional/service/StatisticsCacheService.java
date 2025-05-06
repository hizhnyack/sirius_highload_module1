package ru.hpclab.hl.additional.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.hpclab.hl.additional.dto.AverageWeightResponse;
import ru.hpclab.hl.additional.dto.ProductDTO;
import ru.hpclab.hl.additional.dto.CustomerDTO;
import ru.hpclab.hl.additional.dto.SaleDTO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class StatisticsCacheService {
    private final Map<String, List<AverageWeightResponse>> averageWeightCache = new ConcurrentHashMap<>();
    private final Map<String, ProductDTO> productCache = new ConcurrentHashMap<>();
    private final Map<String, CustomerDTO> customerCache = new ConcurrentHashMap<>();
    private final Map<String, SaleDTO> salesCache = new ConcurrentHashMap<>();

    @Value("${statistics.cache.info:Cache Statistics}")
    private String infoString;

    public void putAverageWeight(String key, List<AverageWeightResponse> values) {
        averageWeightCache.put(key, values);
    }

    public List<AverageWeightResponse> getAverageWeight(String key) {
        return averageWeightCache.get(key);
    }

    public boolean hasAverageWeight(String key) {
        return averageWeightCache.containsKey(key);
    }

    public void putProduct(String id, ProductDTO product) {
        productCache.put(id, product);
    }

    public ProductDTO getProduct(String id) {
        return productCache.get(id);
    }

    public boolean hasProduct(String id) {
        return productCache.containsKey(id);
    }

    public void putCustomer(String id, CustomerDTO customer) {
        customerCache.put(id, customer);
    }

    public CustomerDTO getCustomer(String id) {
        return customerCache.get(id);
    }

    public boolean hasCustomer(String id) {
        return customerCache.containsKey(id);
    }

    public void putSale(String id, SaleDTO sale) {
        salesCache.put(id, sale);
    }

    public SaleDTO getSale(String id) {
        return salesCache.get(id);
    }

    public boolean hasSale(String id) {
        return salesCache.containsKey(id);
    }

    @Async
    @Scheduled(fixedRate = 120000)
    public void printCacheStatistics() {
        long start = System.currentTimeMillis();
        try {
            log.info("{} - Time: {} - AverageWeight Cache Entries: {}", 
                infoString,
                java.time.LocalDateTime.now(),
                averageWeightCache.size());
        } finally {
            log.info("[CACHE] printStatistics: {} ms", System.currentTimeMillis() - start);
        }
    }
} 