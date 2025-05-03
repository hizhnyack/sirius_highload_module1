package ru.hpclab.hl.additional.dto;

import lombok.Data;

@Data
public class SaleDTO {
    private Long id;
    private ProductDTO product;
    private CustomerDTO customer;
    private String date;
    private double weight;
    private double totalCost;
} 