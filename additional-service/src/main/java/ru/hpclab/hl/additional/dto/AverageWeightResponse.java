package ru.hpclab.hl.additional.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с информацией о среднем весе закупки")
public class AverageWeightResponse {
    @Schema(description = "ID товара", example = "1")
    private Long productId;

    @Schema(description = "Название товара", example = "Яблоки")
    private String productName;

    @Schema(description = "Средний вес закупки в кг", example = "2.5")
    private double averageWeight;

    @Schema(description = "Период расчета", example = "с 01.03.2024 по 01.04.2024")
    private String period;
} 