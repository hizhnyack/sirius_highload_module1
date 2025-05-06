package ru.hpclab.hl.additional.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StatisticsCacheService {
    @Value("${statistics.cache.info:Cache Statistics}")
    private String infoString;

    // Кэширование отключено

    @Async
    @Scheduled(fixedRate = 120000)
    public void printCacheStatistics() {
        long start = System.currentTimeMillis();
        try {
            log.info("{} - Time: {} - Cache is disabled", 
                infoString,
                java.time.LocalDateTime.now());
        } finally {
            log.info("[CACHE] printStatistics: {} ms", System.currentTimeMillis() - start);
        }
    }
} 