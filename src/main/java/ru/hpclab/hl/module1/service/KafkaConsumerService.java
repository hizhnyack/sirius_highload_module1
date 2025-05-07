package ru.hpclab.hl.module1.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumerService {

    @Value("${kafka.topic}")
    private String topic;

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.groupId}")
    public void listen(String message) {
        log.info("Received message from topic {}: {}", topic, message);
        // Здесь можно добавить логику обработки сообщения
        // Например, сохранение в базу данных или другие операции
    }
} 