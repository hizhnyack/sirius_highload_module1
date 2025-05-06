package ru.hpclab.hl.additional.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.hpclab.hl.additional.dto.SaleDTO;
import ru.hpclab.hl.additional.dto.ProductDTO;
import ru.hpclab.hl.additional.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hpclab.hl.additional.client.SaleClient;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class StatisticsCacheService {
    // Кэш покупок: ключ — период (например, "2024-05"), значение — список продаж
    private final Map<String, List<SaleDTO>> salesCache = new ConcurrentHashMap<>();
    // Кэш товаров: ключ — productId, значение — ProductDTO
    private final Map<Long, ProductDTO> productCache = new ConcurrentHashMap<>();
    // Кэш покупателей: ключ — customerId, значение — CustomerDTO
    private final Map<Long, CustomerDTO> customerCache = new ConcurrentHashMap<>();

    private final SaleClient saleClient;

    @Autowired
    public StatisticsCacheService(SaleClient saleClient) {
        this.saleClient = saleClient;
    }

    @Value("${statistics.cache.info:Cache Statistics}")
    private String infoString;

    // --- Покупки ---
    public void putSales(String key, List<SaleDTO> sales) {
        salesCache.put(key, sales);
    }
    public List<SaleDTO> getSales(String key) {
        return salesCache.get(key);
    }
    public boolean hasSales(String key) {
        return salesCache.containsKey(key);
    }
    public int getSalesCacheSize() {
        return salesCache.size();
    }

    // --- Товары ---
    public void putProduct(ProductDTO product) {
        if (product != null && product.getId() != null) {
            productCache.put(product.getId(), product);
        }
    }
    public ProductDTO getProduct(Long productId) {
        return productCache.get(productId);
    }
    public boolean hasProduct(Long productId) {
        return productCache.containsKey(productId);
    }
    public int getProductCacheSize() {
        return productCache.size();
    }

    // --- Покупатели ---
    public void putCustomer(CustomerDTO customer) {
        if (customer != null && customer.getId() != null) {
            customerCache.put(customer.getId(), customer);
        }
    }
    public CustomerDTO getCustomer(Long customerId) {
        return customerCache.get(customerId);
    }
    public boolean hasCustomer(Long customerId) {
        return customerCache.containsKey(customerId);
    }
    public int getCustomerCacheSize() {
        return customerCache.size();
    }

    @Async
    @Scheduled(fixedRate = 120000)
    public void printCacheStatistics() {
        long start = System.currentTimeMillis();
        try {
            log.info("{} - Time: {} - Sales Cache Entries: {} | Product Cache: {} | Customer Cache: {}", 
                infoString,
                java.time.LocalDateTime.now(),
                salesCache.size(),
                productCache.size(),
                customerCache.size());
        } finally {
            log.info("[CACHE] printStatistics: {} ms", System.currentTimeMillis() - start);
        }
    }

    /**
     * Автоматически наполняет кэши продаж, товаров и покупателей за последний месяц
     */
    @Async
    @Scheduled(fixedRateString = "${statistics.cache.fill.fixedRate:60000}")
    public void fillCaches() {
        try {
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusMonths(1);
            String periodKey = String.format("%d-%02d", startDate.getYear(), startDate.getMonthValue());
            List<SaleDTO> sales = saleClient.getSalesByDateRange(startDate, endDate);
            if (sales != null && !sales.isEmpty()) {
                putSales(periodKey, sales);
                Set<Long> productIds = new HashSet<>();
                Set<Long> customerIds = new HashSet<>();
                for (SaleDTO sale : sales) {
                    if (sale.getProduct() != null && sale.getProduct().getId() != null) {
                        productIds.add(sale.getProduct().getId());
                        putProduct(sale.getProduct());
                    }
                    if (sale.getCustomer() != null && sale.getCustomer().getId() != null) {
                        customerIds.add(sale.getCustomer().getId());
                        putCustomer(sale.getCustomer());
                    }
                }
                log.info("[CACHE FILL] Loaded {} sales, {} products, {} customers for period {}", sales.size(), productIds.size(), customerIds.size(), periodKey);
            } else {
                log.warn("[CACHE FILL] No sales loaded for period {}", periodKey);
            }
        } catch (Exception e) {
            log.error("[CACHE FILL] Error filling caches: {}", e.getMessage(), e);
        }
    }
} 