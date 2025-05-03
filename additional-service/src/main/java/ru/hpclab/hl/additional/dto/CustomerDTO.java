package ru.hpclab.hl.additional.dto;

import lombok.Data;

@Data
public class CustomerDTO {
    private Long id;
    private String fullName;
    private String phone;
    private boolean hasDiscountCard;
} 