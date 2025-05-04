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
        averageWeightCache.put(key, values);
    }

    public List<AverageWeightResponse> getAverageWeight(String key) {
        return averageWeightCache.get(key);
    }

    public boolean hasAverageWeight(String key) {
        return averageWeightCache.containsKey(key);
    }

    @Async
    @Scheduled(fixedRate = 120000)
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