package ru.hpclab.hl.module1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.service.CustomerService;
import ru.hpclab.hl.module1.service.ProductService;
import ru.hpclab.hl.module1.service.SaleService;

@RestController
@RequestMapping("/api/data")
public class DataManagementController {

    private final ProductService productService;
    private final CustomerService customerService;
    private final SaleService saleService;

    @Autowired
    public DataManagementController(ProductService productService,
                                  CustomerService customerService,
                                  SaleService saleService) {
        this.productService = productService;
        this.customerService = customerService;
        this.saleService = saleService;
    }

    @PostMapping("/clear/products")
    public ResponseEntity<String> clearProducts() {
        long start = System.currentTimeMillis();
        try {
            productService.deleteAll();
            return ResponseEntity.ok("All products have been deleted");
        } finally {
            ru.hpclab.hl.module1.service.ObservabilityService.recordTiming("data.clearProducts", System.currentTimeMillis() - start);
        }
    }

    @PostMapping("/clear/customers")
    public ResponseEntity<String> clearCustomers() {
        long start = System.currentTimeMillis();
        try {
            customerService.deleteAll();
            return ResponseEntity.ok("All customers have been deleted");
        } finally {
            ru.hpclab.hl.module1.service.ObservabilityService.recordTiming("data.clearCustomers", System.currentTimeMillis() - start);
        }
    }

    @PostMapping("/clear/sales")
    public ResponseEntity<String> clearSales() {
        long start = System.currentTimeMillis();
        try {
            saleService.deleteAll();
            return ResponseEntity.ok("All sales have been deleted");
        } finally {
            ru.hpclab.hl.module1.service.ObservabilityService.recordTiming("data.clearSales", System.currentTimeMillis() - start);
        }
    }

    @PostMapping("/clear/all")
    public ResponseEntity<String> clearAll() {
        long start = System.currentTimeMillis();
        try {
            saleService.deleteAll();
            productService.deleteAll();
            customerService.deleteAll();
            return ResponseEntity.ok("All data has been deleted");
        } finally {
            ru.hpclab.hl.module1.service.ObservabilityService.recordTiming("data.clearAll", System.currentTimeMillis() - start);
        }
    }
} 