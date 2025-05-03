package ru.hpclab.hl.additional.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.additional.dto.AverageWeightResponse;
import ru.hpclab.hl.additional.service.AverageWeightService;

import java.util.List;

@RestController
@RequestMapping("/api/sales/average-weight")
@RequiredArgsConstructor
@Tag(name = "Average Weight", description = "API для расчета среднего веса закупки")
public class AverageWeightController {
    private final AverageWeightService averageWeightService;

    @Operation(
        summary = "Получить средний вес закупки за последний месяц",
        description = "Возвращает средний вес закупки для каждого товара за последний месяц"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Успешное получение данных",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = AverageWeightResponse.class)
        )
    )
    @ApiResponse(
        responseCode = "204",
        description = "Нет данных за последний месяц"
    )
    @GetMapping("/last-month")
    public ResponseEntity<List<AverageWeightResponse>> getAverageWeightLastMonth() {
        var result = averageWeightService.calculateAverageWeightLastMonth();
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "Получить средний вес закупки за конкретный месяц",
        description = "Возвращает средний вес закупки для каждого товара за указанный месяц и год"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Успешное получение данных",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = AverageWeightResponse.class)
        )
    )
    @ApiResponse(
        responseCode = "204",
        description = "Нет данных за указанный месяц"
    )
    @GetMapping("/{year}/{month}")
    public ResponseEntity<List<AverageWeightResponse>> getAverageWeightForMonth(
            @PathVariable int year,
            @PathVariable int month) {
        var result = averageWeightService.calculateAverageWeightForMonth(year, month);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }
} 