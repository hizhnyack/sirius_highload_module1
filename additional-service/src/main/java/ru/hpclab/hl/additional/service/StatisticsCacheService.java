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
        long start = System.currentTimeMillis();
        try {
            averageWeightCache.put(key, values);
        } finally {
            log.info("[CACHE] putAverageWeight: {} ms", System.currentTimeMillis() - start);
        }
    }

    public List<AverageWeightResponse> getAverageWeight(String key) {
        long start = System.currentTimeMillis();
        try {
            return averageWeightCache.get(key);
        } finally {
            log.info("[CACHE] getAverageWeight: {} ms", System.currentTimeMillis() - start);
        }
    }

    public boolean hasAverageWeight(String key) {
        long start = System.currentTimeMillis();
        try {
            return averageWeightCache.containsKey(key);
        } finally {
            log.info("[CACHE] hasAverageWeight: {} ms", System.currentTimeMillis() - start);
        }
    }

    @Async
    @Scheduled(fixedRateString = "${statistics.cache.print.rate:300000}")
    public void printCacheStatistics() {
        long start = System.currentTimeMillis();
        try {
            log.info("{} - Time: {} - AverageWeight Cache Entries: {}", 
                infoString,
                java.time.LocalDateTime.now(),
                averageWeightCache.size());
        } finally {
            log.info("[CACHE] printStatistics: {} ms", System.currentTimeMillis() - start);
        }
    }
} 