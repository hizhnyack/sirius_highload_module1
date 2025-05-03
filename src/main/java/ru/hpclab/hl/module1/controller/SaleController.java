package ru.hpclab.hl.module1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.model.Sale;
import ru.hpclab.hl.module1.service.SaleService;

import java.time.LocalDate;
import java.util.List;
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
    public Sale create(@RequestBody Sale sale) {
        return saleService.save(sale);
    }

    @GetMapping
    public List<Sale> findAll() {
        return saleService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> findById(@PathVariable Long id) {
        Optional<Sale> sale = saleService.findById(id);
        return sale.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/by-date")
    public List<Sale> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return saleService.findByDateBetween(startDate, endDate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Sale> sale = saleService.findById(id);
        if (sale.isPresent()) {
            saleService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAll() {
        saleService.deleteAll();
        return ResponseEntity.ok().build();
    }
}