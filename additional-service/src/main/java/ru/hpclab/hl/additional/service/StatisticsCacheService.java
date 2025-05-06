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

    @Value("${statistics.cache.fill.fixedRate:60000}")
    private long cacheFillRate;

    @Value("${statistics.cache.retention.period:1}")
    private int retentionPeriod;

    @Value("${statistics.cache.max.sales:1000}")
    private int maxSales;

    @Value("${statistics.cache.max.products:500}")
    private int maxProducts;

    @Value("${statistics.cache.max.customers:500}")
    private int maxCustomers;

    @Autowired
    public StatisticsCacheService(SaleClient saleClient) {
        this.saleClient = saleClient;
    }

    // --- Покупки ---
    public void putSales(String key, List<SaleDTO> sales) {
        if (sales != null && !sales.isEmpty()) {
            // Очищаем старые данные, если превышен лимит
            if (salesCache.size() >= retentionPeriod) {
                String oldestKey = salesCache.keySet().stream()
                    .min(String::compareTo)
                    .orElse(null);
                if (oldestKey != null) {
                    salesCache.remove(oldestKey);
                }
            }
            salesCache.put(key, sales);
        }
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
            // Очищаем старые данные, если превышен лимит
            if (productCache.size() >= maxProducts) {
                Long oldestKey = productCache.keySet().stream()
                    .min(Long::compareTo)
                    .orElse(null);
                if (oldestKey != null) {
                    productCache.remove(oldestKey);
                }
            }
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
            // Очищаем старые данные, если превышен лимит
            if (customerCache.size() >= maxCustomers) {
                Long oldestKey = customerCache.keySet().stream()
                    .min(Long::compareTo)
                    .orElse(null);
                if (oldestKey != null) {
                    customerCache.remove(oldestKey);
                }
            }
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
            log.info("Cache Statistics - Time: {} - Sales Cache Entries: {} | Product Cache: {} | Customer Cache: {}", 
                java.time.LocalDateTime.now(),
                salesCache.size(),
                productCache.size(),
                customerCache.size());
        } finally {
            log.info("[CACHE] printStatistics: {} ms", System.currentTimeMillis() - start);
        }
    }

    /**
     * Автоматически наполняет кэши продаж, товаров и покупателей за последние N месяцев
     */
    @Async
    @Scheduled(fixedRateString = "${statistics.cache.fill.fixedRate:60000}")
    public void fillCaches() {
        try {
            LocalDate endDate = LocalDate.now();
            // Загружаем данные за последние N месяцев
            for (int i = 0; i < retentionPeriod; i++) {
                LocalDate startDate = endDate.minusMonths(i + 1);
                String periodKey = String.format("%d-%02d", startDate.getYear(), startDate.getMonthValue());
                
                // Проверяем, есть ли уже данные в кэше
                if (!hasSales(periodKey)) {
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
                        log.info("[CACHE FILL] Loaded {} sales, {} products, {} customers for period {}", 
                            sales.size(), productIds.size(), customerIds.size(), periodKey);
                    } else {
                        log.warn("[CACHE FILL] No sales loaded for period {}", periodKey);
                    }
                }
            }
        } catch (Exception e) {
            log.error("[CACHE FILL] Error filling caches: {}", e.getMessage(), e);
        }
    }
} 