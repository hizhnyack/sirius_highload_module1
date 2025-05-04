package ru.hpclab.hl.module1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.model.Sale;
import ru.hpclab.hl.module1.service.SaleService;
import ru.hpclab.hl.module1.dto.SaleCreateDto;
import ru.hpclab.hl.module1.service.ObservabilityService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    private final SaleService saleService;
    private final ObservabilityService observabilityService;

    @Autowired
    public SaleController(SaleService saleService, ObservabilityService observabilityService) {
        this.saleService = saleService;
        this.observabilityService = observabilityService;
    }

    @PostMapping("/create")
    public Sale create(@RequestBody SaleCreateDto saleDto) {
        long start = System.currentTimeMillis();
        try {
            return saleService.createSale(
                saleDto.getProductId(),
                saleDto.getCustomerId(),
                saleDto.getWeight(),
                saleDto.getDate()
            );
        } finally {
            observabilityService.recordTiming("sale.create", System.currentTimeMillis() - start);
        }
    }

    @GetMapping
    public List<Sale> findAll() {
        long start = System.currentTimeMillis();
        try {
            return saleService.findAll();
        } finally {
            observabilityService.recordTiming("sale.findAll", System.currentTimeMillis() - start);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> findById(@PathVariable Long id) {
        long start = System.currentTimeMillis();
        try {
            Optional<Sale> sale = saleService.findById(id);
            return sale.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } finally {
            observabilityService.recordTiming("sale.findById", System.currentTimeMillis() - start);
        }
    }

    @GetMapping("/by-date")
    public List<Sale> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        long start = System.currentTimeMillis();
        try {
            return saleService.findByDateBetween(startDate, endDate);
        } finally {
            observabilityService.recordTiming("sale.byDate", System.currentTimeMillis() - start);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        long start = System.currentTimeMillis();
        try {
            Optional<Sale> sale = saleService.findById(id);
            if (sale.isPresent()) {
                saleService.deleteById(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } finally {
            observabilityService.recordTiming("sale.delete", System.currentTimeMillis() - start);
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAll() {
        long start = System.currentTimeMillis();
        try {
            saleService.deleteAll();
            return ResponseEntity.ok().build();
        } finally {
            observabilityService.recordTiming("sale.deleteAll", System.currentTimeMillis() - start);
        }
    }
}