package ru.hpclab.hl.module1.service;

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

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;

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
            log.info("Received message: {}", message);
            // Здесь ваша логика обработки сообщения
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
            // В случае ошибки можно реализовать retry логику или отправить в dead letter queue
        }
    }
} 