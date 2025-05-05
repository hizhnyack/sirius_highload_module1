package ru.hpclab.hl.additional.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.hpclab.hl.additional.client.SaleClient;
import ru.hpclab.hl.additional.dto.ProductDTO;
import ru.hpclab.hl.additional.dto.SaleDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class SaleClientFallback implements SaleClient {
    private static final Logger logger = LoggerFactory.getLogger(SaleClientFallback.class);

    @Override
    public List<SaleDTO> getSalesByDateRange(LocalDate startDate, LocalDate endDate) {
        logger.warn("Fallback triggered for getSalesByDateRange. Main service unavailable. Returning empty list.");
        return new ArrayList<>();
    }

    @Override
    public ProductDTO getProductById(Long id) {
        logger.warn("Fallback triggered for getProductById({}). Main service unavailable. Returning generic product.", id);
        ProductDTO fallbackProduct = new ProductDTO();
        fallbackProduct.setId(id);
        fallbackProduct.setName("Unknown Product (Service Unavailable)");
        fallbackProduct.setCategory("Unknown");
        fallbackProduct.setPricePerKg(0.0);
        return fallbackProduct;
    }
} 