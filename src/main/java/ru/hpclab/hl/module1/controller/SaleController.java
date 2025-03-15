package ru.hpclab.hl.module1.controller;

import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.model.Sale;
import ru.hpclab.hl.module1.service.SaleService;

import java.util.List;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/sales")
public class SaleController {
    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public Sale createSale(@RequestParam Long productId,
                           @RequestParam Long customerId,
                           @RequestParam double weight,
                           @RequestParam String date) {
        LocalDate saleDate = LocalDate.parse(date);
        saleService.createSale(productId, customerId, weight, saleDate);
        return saleService.findAll().get(saleService.findAll().size() - 1);
    }

    @GetMapping // Добавляем метод для обработки GET-запросов
    public List<Sale> getAllSales() {
        return saleService.findAll();
    }

    @GetMapping("/average-weight/{productId}") // Endpoint для одного товара
    public double getAverageWeightLastMonth(@PathVariable Long productId) {
        return saleService.calculateAverageWeightLastMonth(productId);
    }

    @GetMapping("/average-weight") // Endpoint для всех товаров
    public Map<Long, Double> getAverageWeightLastMonth() {
        return saleService.calculateAverageWeightLastMonth();
    }
}