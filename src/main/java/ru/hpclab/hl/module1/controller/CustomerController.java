package ru.hpclab.hl.module1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.model.Customer;
import ru.hpclab.hl.module1.service.CustomerService;
import ru.hpclab.hl.module1.service.ObservabilityService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> findAll() {
        long start = System.currentTimeMillis();
        try {
        return customerService.findAll();
        } finally {
            ObservabilityService.recordTiming("customer.findAll", System.currentTimeMillis() - start);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> findById(@PathVariable Long id) {
        long start = System.currentTimeMillis();
        try {
        Optional<Customer> customer = customerService.findById(id);
        return customer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
        } finally {
            ObservabilityService.recordTiming("customer.findById", System.currentTimeMillis() - start);
        }
    }

    @PostMapping
    public Customer create(@RequestBody Customer customer) {
        long start = System.currentTimeMillis();
        try {
        return customerService.save(customer);
        } finally {
            ObservabilityService.recordTiming("customer.create", System.currentTimeMillis() - start);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        long start = System.currentTimeMillis();
        try {
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isPresent()) {
            customerService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
        } finally {
            ObservabilityService.recordTiming("customer.delete", System.currentTimeMillis() - start);
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAll() {
        long start = System.currentTimeMillis();
        try {
        customerService.deleteAll();
        return ResponseEntity.ok().build();
        } finally {
            ObservabilityService.recordTiming("customer.deleteAll", System.currentTimeMillis() - start);
        }
    }
}