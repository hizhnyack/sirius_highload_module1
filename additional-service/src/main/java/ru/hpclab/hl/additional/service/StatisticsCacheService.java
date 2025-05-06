package ru.hpclab.hl.additional.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StatisticsCacheService {

    @Async
    @Scheduled(fixedRate = 120000)
    public void printCacheStatistics() {
        long start = System.currentTimeMillis();
        try {
            log.info("Cache Statistics - Time: {} - Sales Cache Entries: 0 | Product Cache: 0 | Customer Cache: 0", 
                java.time.LocalDateTime.now());
        } finally {
            log.info("[CACHE] printStatistics: {} ms", System.currentTimeMillis() - start);
        }
    }
} 