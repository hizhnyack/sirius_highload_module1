package ru.hpclab.hl.additional.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hpclab.hl.additional.dto.AverageWeightResponse;
import ru.hpclab.hl.additional.dto.ProductDTO;
import ru.hpclab.hl.additional.dto.SaleDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AverageWeightService {
    private static final Logger logger = LoggerFactory.getLogger(AverageWeightService.class);

    private final StatisticsCacheService statisticsCacheService;

    @Value("${averageweightservice.infostring:AverageWeightService}")
    private String infoString;

    @Async
    @Scheduled(fixedRateString = "${averageweightservice.fixedRate.in.milliseconds:60000}")
    public void printCacheStatistics() {
        int salesSize = statisticsCacheService.getSalesCacheSize();
        int productSize = statisticsCacheService.getProductCacheSize();
        int customerSize = statisticsCacheService.getCustomerCacheSize();
        logger.info("[{}] Cache sizes: salesCache={}, productCache={}, customerCache={}",
                infoString, salesSize, productSize, customerSize);
    }

    public List<AverageWeightResponse> calculateAverageWeightLastMonth() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);
        return calculateAverageWeightForPeriod(startDate, endDate);
    }

    public List<AverageWeightResponse> calculateAverageWeightForMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        return calculateAverageWeightForPeriod(startDate, endDate);
    }

    private List<AverageWeightResponse> calculateAverageWeightForPeriod(LocalDate startDate, LocalDate endDate) {
        String cacheKey = getPeriodKey(startDate, endDate);
        List<SaleDTO> sales = statisticsCacheService.getSales(cacheKey);
        if (sales == null || sales.isEmpty()) {
            logger.info("No sales found in cache for period {} to {}", startDate, endDate);
            return new ArrayList<>();
        }

        // Группировка по productId
        Map<Long, List<SaleDTO>> salesByProduct = sales.stream()
                .filter(sale -> sale.getProduct() != null && sale.getProduct().getId() != null)
                .collect(Collectors.groupingBy(sale -> sale.getProduct().getId()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String period = String.format("с %s по %s", startDate.format(formatter), endDate.format(formatter));

        return salesByProduct.entrySet().stream()
                .map(entry -> {
                    Long productId = entry.getKey();
                    List<SaleDTO> productSales = entry.getValue();
                    String productName = getProductNameFromCache(productId);
                    double averageWeight = productSales.stream()
                            .mapToDouble(SaleDTO::getWeight)
                            .average()
                            .orElse(0.0);
                    return new AverageWeightResponse(productId, productName, averageWeight, period);
                })
                .collect(Collectors.toList());
    }

    private String getProductNameFromCache(Long productId) {
        ProductDTO product = statisticsCacheService.getProduct(productId);
        return Optional.ofNullable(product)
                .map(ProductDTO::getName)
                .orElse("Товар #" + productId);
    }

    private String getPeriodKey(LocalDate startDate, LocalDate endDate) {
        // Например, "2024-05" для месяца или "2024-04-01_2024-04-30" для произвольного периода
        if (startDate.getDayOfMonth() == 1 && endDate.equals(startDate.withDayOfMonth(startDate.lengthOfMonth()))) {
            return String.format("%d-%02d", startDate.getYear(), startDate.getMonthValue());
        } else {
            return startDate.toString() + "_" + endDate.toString();
        }
    }
} 