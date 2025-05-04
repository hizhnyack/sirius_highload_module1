package ru.hpclab.hl.additional.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.additional.dto.AverageWeightResponse;
import ru.hpclab.hl.additional.service.AverageWeightService;
import ru.hpclab.hl.additional.service.ObservabilityService;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    private final AverageWeightService averageWeightService;

    public StatisticsController(AverageWeightService averageWeightService) {
        this.averageWeightService = averageWeightService;
    }

    @GetMapping("/average-weight/last-month")
    public ResponseEntity<List<AverageWeightResponse>> getAverageWeightLastMonth() {
        long start = System.currentTimeMillis();
        try {
            return ResponseEntity.ok(averageWeightService.calculateAverageWeightLastMonth());
        } finally {
            ObservabilityService.recordTiming("statistics.average-weight.last-month", System.currentTimeMillis() - start);
        }
    }

    @GetMapping("/average-weight/month")
    public ResponseEntity<List<AverageWeightResponse>> getAverageWeightForMonth(
            @RequestParam int year,
            @RequestParam int month) {
        long start = System.currentTimeMillis();
        try {
            return ResponseEntity.ok(averageWeightService.calculateAverageWeightForMonth(year, month));
        } finally {
            ObservabilityService.recordTiming("statistics.average-weight.month", System.currentTimeMillis() - start);
        }
    }
} 