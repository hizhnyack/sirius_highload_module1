package ru.hpclab.hl.module1.dto;

import java.time.LocalDate;

public class SaleCreateDto {
    private Long productId;
    private Long customerId;
    private double weight;
    private LocalDate date;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
} 