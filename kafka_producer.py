from kafka import KafkaProducer
import json
import time

# Конфигурация
KAFKA_SERVERS = ['hl22.zil:9094', 'hl23.zil:9094']
TOPIC = 'hizhnyak'

# Создаем продюсер
producer = KafkaProducer(
    bootstrap_servers=KAFKA_SERVERS,
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)

def send_message(message):
    try:
        # Отправляем сообщение
        future = producer.send(TOPIC, message)
        # Ждем подтверждения
        record_metadata = future.get(timeout=10)
        print(f"Message sent successfully to topic {record_metadata.topic}")
        print(f"Partition: {record_metadata.partition}")
        print(f"Offset: {record_metadata.offset}")
    except Exception as e:
        print(f"Error sending message: {e}")

# Пример сообщения
test_message = {
    "id": 1,
    "message": "Test message",
    "timestamp": time.time()
}

# Отправляем сообщение
send_message(test_message)

# Закрываем продюсер
producer.close() 