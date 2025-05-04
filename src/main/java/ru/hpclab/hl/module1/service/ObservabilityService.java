package ru.hpclab.hl.module1.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;

@Slf4j
@Service
public final class ObservabilityService {
    private static final ConcurrentMap<String, TimingStats> metrics = new ConcurrentHashMap<>();
    private static final long CLEANUP_THRESHOLD_MS = TimeUnit.MINUTES.toMillis(40);

    public static void recordTiming(String metricName, long durationMs) {
        metrics.computeIfAbsent(metricName, k -> new TimingStats()).addRecord(durationMs);
    }

    public static synchronized MetricsSnapshot getMetricsAndClean() {
        MetricsSnapshot snapshot = new MetricsSnapshot(
                collectStats(10_000),
                collectStats(30_000),
                collectStats(60_000)
        );
        cleanOldData();
        return snapshot;
    }

    @Scheduled(fixedRate = 10000)
    public void logMetrics() {
        MetricsSnapshot snapshot = getMetricsAndClean();
        log.info("=== Metrics Report === {}", snapshot.timestamp);
        log.info("Last 10 seconds:");
        logMetricsMap(snapshot.last10s);
        log.info("Last 30 seconds:");
        logMetricsMap(snapshot.last30s);
        log.info("Last 1 minute:");
        logMetricsMap(snapshot.last1m);
        log.info("=== End Metrics Report ===");
    }

    private void logMetricsMap(Map<String, MetricStats> metricsMap) {
        if (metricsMap.isEmpty()) {
            log.info("  No metrics recorded");
            return;
        }
        metricsMap.entrySet().stream()
            .filter(entry -> entry.getValue().count > 0)
            .forEach(entry -> {
                String name = entry.getKey();
                MetricStats stats = entry.getValue();
                log.info("  {} - count: {}, avg: {}s, min: {}s, max: {}s",
                        name,
                        stats.count,
                        String.format("%.5f", stats.avgMs / 1000.0),
                        String.format("%.5f", stats.minMs / 1000.0),
                        String.format("%.5f", stats.maxMs / 1000.0));
            });
    }

    private static Map<String, MetricStats> collectStats(long periodMs) {
        long cutoff = System.currentTimeMillis() - periodMs;
        return metrics.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().calculateStats(cutoff)
                ));
    }

    private static void cleanOldData() {
        long oldestAllowed = System.currentTimeMillis() - CLEANUP_THRESHOLD_MS;
        metrics.values().forEach(stats -> stats.removeOlderThan(oldestAllowed));
        metrics.entrySet().removeIf(e -> e.getValue().isEmpty());
    }

    public static final class MetricsSnapshot {
        public final Map<String, MetricStats> last10s;
        public final Map<String, MetricStats> last30s;
        public final Map<String, MetricStats> last1m;
        public final String timestamp;

        public MetricsSnapshot(Map<String, MetricStats> last10s,
                               Map<String, MetricStats> last30s,
                               Map<String, MetricStats> last1m) {
            this(last10s, last30s, last1m, Instant.now());
        }

        public MetricsSnapshot(Map<String, MetricStats> last10s,
                               Map<String, MetricStats> last30s,
                               Map<String, MetricStats> last1m,
                               Instant instant) {
            this.last10s = Collections.unmodifiableMap(last10s);
            this.last30s = Collections.unmodifiableMap(last30s);
            this.last1m = Collections.unmodifiableMap(last1m);
            this.timestamp = DateTimeFormatter.ISO_INSTANT.format(instant);
        }
    }

    private static final class TimingStats {
        private final ConcurrentLinkedDeque<TimingRecord> records = new ConcurrentLinkedDeque<>();

        void addRecord(long durationMs) {
            records.add(new TimingRecord(System.currentTimeMillis(), durationMs));
        }

        MetricStats calculateStats(long cutoff) {
            LongSummaryStatistics stats = records.stream()
                    .filter(r -> r.timestamp >= cutoff)
                    .collect(Collectors.summarizingLong(r -> r.duration));
            return new MetricStats(stats.getCount(), stats.getAverage(), stats.getMin(), stats.getMax());
        }

        void removeOlderThan(long timestamp) {
            records.removeIf(r -> r.timestamp < timestamp);
        }

        boolean isEmpty() {
            return records.isEmpty();
        }
    }

    private static final class TimingRecord {
        final long timestamp;
        final long duration;

        TimingRecord(long timestamp, long duration) {
            this.timestamp = timestamp;
            this.duration = duration;
        }
    }

    public static final class MetricStats {
        public final long count;
        public final double avgMs;
        public final long minMs;
        public final long maxMs;

        public MetricStats(long count, double avgMs, long minMs, long maxMs) {
            this.count = count;
            this.avgMs = avgMs;
            this.minMs = minMs;
            this.maxMs = maxMs;
        }
    }
} 