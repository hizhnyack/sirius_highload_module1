package ru.hpclab.hl.additional.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.hpclab.hl.additional.dto.ProductDTO;
import ru.hpclab.hl.additional.dto.SaleDTO;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "main-service", url = "${main.service.url}")
public interface SaleClient {
    @GetMapping("/api/sales")
    List<SaleDTO> getSalesByDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);

    @GetMapping("/api/products/{id}")
    ProductDTO getProductById(@PathVariable("id") Long id);
} 