package ru.hpclab.hl.additional.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.hpclab.hl.additional.dto.AverageWeightResponse;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class StatisticsCacheService {
    private final Map<String, List<AverageWeightResponse>> averageWeightCache = new ConcurrentHashMap<>();

    @Value("${statistics.cache.info:Cache Statistics}")
    private String infoString;

    public void putAverageWeight(String key, List<AverageWeightResponse> values) {
        // Кэширование отключено
    }

    public List<AverageWeightResponse> getAverageWeight(String key) {
        // Кэширование отключено
        return java.util.Collections.emptyList();
    }

    public boolean hasAverageWeight(String key) {
        // Кэширование отключено
        return false;
    }

    //@Async
    //@Scheduled(fixedRate = 120000)
    public void printCacheStatistics() {
        // Кэширование отключено
    }
} 