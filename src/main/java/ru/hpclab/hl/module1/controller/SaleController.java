package ru.hpclab.hl.module1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.model.Sale;
import ru.hpclab.hl.module1.service.SaleService;

import java.util.List;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    private final SaleService saleService;

    @Autowired
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSale(@RequestBody Map<String, Object> request) {
        try {
            Long productId = Long.valueOf(request.get("productId").toString());
            Long customerId = Long.valueOf(request.get("customerId").toString());
            double weight = Double.parseDouble(request.get("weight").toString());
            LocalDate date = LocalDate.parse(request.get("date").toString());
            
            Sale sale = saleService.createSale(productId, customerId, weight, date);
            return ResponseEntity.ok(sale);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Произошла ошибка при создании продажи");
        }
    }

    @GetMapping
    public List<Sale> findAll() {
        return saleService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> findById(@PathVariable Long id) {
        Optional<Sale> sale = saleService.findById(id);
        return sale.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        Optional<Sale> sale = saleService.findById(id);
        if (sale.isPresent()) {
            saleService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearAll() {
        saleService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/average-weight/{productId}")
    public double getAverageWeightLastMonth(@PathVariable Long productId) {
        return saleService.calculateAverageWeightLastMonth(productId);
    }

    @GetMapping("/average-weight")
    public Map<Long, Double> getAverageWeightLastMonth() {
        return saleService.calculateAverageWeightLastMonth();
    }

    @GetMapping("/average-weight-all-time/{productId}")
    public double getAverageWeightAllTime(@PathVariable Long productId) {
        return saleService.calculateAverageWeightAllTime(productId);
    }

    @GetMapping("/average-weight-all-time")
    public Map<Long, Double> getAverageWeightAllTime() {
        return saleService.calculateAverageWeightAllTime();
    }
}