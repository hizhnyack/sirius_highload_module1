package ru.hpclab.hl.module1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic}")
    private String topic;

    public void sendMessage(String message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
        
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Message sent successfully: {}", message);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Failed to send message: {}", message, ex);
            }
        });
    }

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.groupId}")
    public void listen(String message, Acknowledgment ack) {
        try {
            // Парсим JSON сообщение
            Map<String, Object> messageMap = objectMapper.readValue(message, Map.class);
            
            // Извлекаем данные
            Long id = ((Number) messageMap.get("id")).longValue();
            String messageText = (String) messageMap.get("message");
            Long timestamp = ((Number) messageMap.get("timestamp")).longValue();
            
            // Конвертируем timestamp в читаемую дату
            LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(timestamp), 
                ZoneId.systemDefault()
            );

            // Логируем полученное сообщение
            log.info("Received message: id={}, message={}, timestamp={}", 
                    id, messageText, dateTime);

            // Здесь можно добавить вашу бизнес-логику
            // Например, сохранить в базу данных, отправить уведомление и т.д.

            // Подтверждаем обработку сообщения
            ack.acknowledge();
            
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
            // В случае ошибки можно реализовать retry логику или отправить в dead letter queue
        }
    }
} 