package ru.hpclab.hl.additional.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hpclab.hl.additional.client.SaleClient;
import ru.hpclab.hl.additional.dto.AverageWeightResponse;
import ru.hpclab.hl.additional.dto.ProductDTO;
import ru.hpclab.hl.additional.dto.SaleDTO;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AverageWeightService {
    private static final Logger logger = LoggerFactory.getLogger(AverageWeightService.class);
    
    private final SaleClient saleClient;
    private final StatisticsCacheService statisticsCacheService;

    public List<AverageWeightResponse> calculateAverageWeightLastMonth() {
        String cacheKey = "last-month";
        if (statisticsCacheService.hasAverageWeight(cacheKey)) {
            return statisticsCacheService.getAverageWeight(cacheKey);
        }
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);
        List<AverageWeightResponse> result = calculateAverageWeightForPeriod(startDate, endDate);
        statisticsCacheService.putAverageWeight(cacheKey, result);
        return result;
    }

    public List<AverageWeightResponse> calculateAverageWeightForMonth(int year, int month) {
        String cacheKey = year + "-" + (month < 10 ? ("0" + month) : month);
        if (statisticsCacheService.hasAverageWeight(cacheKey)) {
            return statisticsCacheService.getAverageWeight(cacheKey);
        }
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        List<AverageWeightResponse> result = calculateAverageWeightForPeriod(startDate, endDate);
        statisticsCacheService.putAverageWeight(cacheKey, result);
        return result;
    }

    private List<AverageWeightResponse> calculateAverageWeightForPeriod(LocalDate startDate, LocalDate endDate) {
        try {
            List<SaleDTO> sales = saleClient.getSalesByDateRange(startDate, endDate);
            if (sales.isEmpty()) {
                logger.info("No sales found for period {} to {}", startDate, endDate);
                return new ArrayList<>();
            }
    
            Map<String, List<SaleDTO>> salesByProduct = sales.stream()
                    .filter(sale -> sale.getProduct() != null && sale.getProduct().getName() != null)
                    .collect(Collectors.groupingBy(sale -> sale.getProduct().getName()));
    
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String period = String.format("с %s по %s", startDate.format(formatter), endDate.format(formatter));
    
            return salesByProduct.entrySet().stream()
                    .map(entry -> {
                        String productName = entry.getKey();
                        List<SaleDTO> productSales = entry.getValue();
                        double averageWeight = productSales.stream()
                                .mapToDouble(SaleDTO::getWeight)
                                .average()
                                .orElse(0.0);
    
                        return new AverageWeightResponse(
                                productSales.get(0).getProduct().getId(),
                                productName,
                                averageWeight,
                                period
                        );
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error calculating average weight for period {} to {}: {}", startDate, endDate, e.getMessage());
            return new ArrayList<>();
        }
    }

    private String getProductName(Long productId) {
        try {
            ProductDTO product = saleClient.getProductById(productId);
            return product != null ? product.getName() : "Товар #" + productId;
        } catch (Exception e) {
            logger.error("Error getting product name for ID {}: {}", productId, e.getMessage());
            return "Товар #" + productId;
        }
    }
} 