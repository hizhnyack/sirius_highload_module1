package ru.hpclab.hl.additional.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String category;
    private double pricePerKg;
} 