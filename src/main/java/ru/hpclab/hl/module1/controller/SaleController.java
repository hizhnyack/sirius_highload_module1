package ru.hpclab.hl.module1.controller;

import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.model.Sale;
import ru.hpclab.hl.module1.service.SaleService;

import java.time.LocalDate;
import java.util.List;

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

        // Возвращаем последнюю добавленную продажу
        List<Sale> sales = saleService.findAll();
        return sales.get(sales.size() - 1);
    }

    @GetMapping
    public List<Sale> getAllSales() {
        return saleService.findAll();
    }
}