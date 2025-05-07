#!/usr/bin/env python3

import argparse
from kafka import KafkaProducer
from faker import Faker
import random
import json
import time
from tqdm import tqdm
import logging

# Настройка логирования
logging.basicConfig(level=logging.DEBUG)
logger = logging.getLogger(__name__)

# Инициализация Faker для русских данных
fake = Faker('ru_RU')

# Конфигурация Kafka
KAFKA_SERVERS = ['hl22.zil:9094', 'hl23.zil:9094']
TOPIC = 'hizhnyak'  # Проверьте, что топик называется именно так

# Словари для генерации данных
CATEGORIES = {
    'Овощи': [
        'огурцы', 'помидоры', 'картофель', 'морковь', 'лук', 'капуста', 'перец', 'баклажаны',
        'тыква', 'свекла', 'чеснок', 'зелень', 'шпинат', 'салат', 'редис', 'кабачки'
    ],
    'Фрукты': [
        'яблоки', 'груши', 'бананы', 'апельсины', 'мандарины', 'лимон', 'виноград', 'персики',
        'абрикосы', 'сливы', 'клубника', 'малина', 'черника', 'ежевика', 'земляника', 'ананасы'
    ],
    'Вкусняшки': [
        'колбаса', 'сосиски', 'ветчина', 'курица', 'говядина', 'свинина', 'рыба', 'креветки',
        'икра', 'паштет', 'буженина', 'сардельки', 'шашлык', 'пельмени', 'вареники', 'котлеты'
    ],
    'Бакалея': [
        'хлеб', 'булочки', 'батон', 'круассан', 'пирожки', 'печенье', 'торт', 'пирог',
        'яйца', 'майонез', 'кетчуп', 'горчица', 'соль', 'сахар', 'мука', 'рис'
    ],
    'Напитки': [
        'молоко', 'кефир', 'творог', 'сметана', 'сыр', 'йогурт',
        'чай', 'кофе', 'какао', 'сок', 'вода', 'лимонад', 'компот', 'морс'
    ]
}

PRODUCT_ADJECTIVES = [
    'Дружба народов', 'Зелёный удав', 'Красный великан', 'Золотой урожай', 'Свежий ветер',
    'Весенний', 'Летний', 'Осенний', 'Зимний', 'Домашний', 'Деревенский', 'Фермерский',
    'Экстра', 'Премиум', 'Люкс', 'Классический', 'Традиционный', 'Особый', 'Отборный'
]

def create_producer():
    """Создание Kafka producer с дополнительными настройками"""
    try:
        producer = KafkaProducer(
            bootstrap_servers=KAFKA_SERVERS,
            value_serializer=lambda v: json.dumps(v).encode('utf-8'),
            acks='all',  # Ждем подтверждения от всех реплик
            retries=3,   # Количество попыток отправки
            request_timeout_ms=30000,  # Таймаут запроса
            api_version_auto_timeout_ms=30000  # Таймаут для определения версии API
        )
        logger.info(f"Producer created successfully, connected to {KAFKA_SERVERS}")
        return producer
    except Exception as e:
        logger.error(f"Failed to create producer: {e}")
        raise

def generate_phone_number():
    """Генерация телефонного номера в формате +7 (XXX) XXX-XX-XX"""
    area_code = random.randint(900, 999)
    first_part = random.randint(100, 999)
    second_part = random.randint(10, 99)
    third_part = random.randint(10, 99)
    return f"+7 ({area_code}) {first_part}-{second_part}-{third_part}"

def generate_customer():
    """Генерация данных клиента"""
    return {
        "type": "customer",
        "data": {
            "fullName": fake.name(),
            "phone": generate_phone_number(),
            "hasDiscountCard": random.choice([True, False])
        }
    }

def generate_product():
    """Генерация данных продукта"""
    category = random.choice(list(CATEGORIES.keys()))
    product_type = random.choice(CATEGORIES[category])
    adjective = random.choice(PRODUCT_ADJECTIVES)
    
    return {
        "type": "product",
        "data": {
            "name": f"{product_type} {adjective}",
            "category": category,
            "pricePerKg": round(random.uniform(50, 500), 2)
        }
    }

def generate_sale(product_id, customer_id):
    """Генерация данных продажи"""
    return {
        "type": "sale",
        "data": {
            "productId": product_id,
            "customerId": customer_id,
            "weight": round(random.uniform(0.1, 10.0), 2),
            "date": fake.date_between(start_date='-1y', end_date='today').isoformat()
        }
    }

def send_message(producer, message):
    """Отправка сообщения в Kafka с расширенным логированием"""
    try:
        logger.debug(f"Attempting to send message: {message}")
        future = producer.send(TOPIC, message)
        # Ждем подтверждения отправки
        record_metadata = future.get(timeout=10)
        logger.info(f"Message sent successfully to topic {record_metadata.topic}")
        logger.info(f"Partition: {record_metadata.partition}")
        logger.info(f"Offset: {record_metadata.offset}")
        return True
    except Exception as e:
        logger.error(f"Error sending message: {e}")
        return False

def generate_data(count, data_type):
    """Генерация и отправка данных в Kafka"""
    logger.info(f"Starting data generation for type: {data_type}, count: {count}")
    
    try:
        producer = create_producer()
        
        # Проверяем подключение
        logger.info("Testing connection...")
        producer.metrics()
        logger.info("Connection test successful")

        if data_type == 'customers':
            print("Генерация клиентов...")
            for _ in tqdm(range(count), desc="Создание клиентов"):
                message = generate_customer()
                if send_message(producer, message):
                    if _ < 5:  # Показываем только первые 5 сообщений
                        print(f"Отправлен клиент: {message['data']['fullName']}")
                time.sleep(0.1)  # Небольшая задержка между сообщениями

        elif data_type == 'products':
            print("Генерация продуктов...")
            for _ in tqdm(range(count), desc="Создание продуктов"):
                message = generate_product()
                if send_message(producer, message):
                    if _ < 5:  # Показываем только первые 5 сообщений
                        print(f"Отправлен продукт: {message['data']['name']}")
                time.sleep(0.1)

        elif data_type == 'sales':
            print("Генерация продаж...")
            for _ in tqdm(range(count), desc="Создание продаж"):
                message = generate_sale(
                    random.randint(1, 1000),
                    random.randint(1, 1000)
                )
                if send_message(producer, message):
                    if _ < 5:  # Показываем только первые 5 сообщений
                        print(f"Отправлена продажа: {message['data']}")
                time.sleep(0.1)

        elif data_type == 'all':
            print("Генерация всех типов данных...")
            for data_type in ['customers', 'products', 'sales']:
                generate_data(count, data_type)

    except Exception as e:
        logger.error(f"Error in generate_data: {e}")
    finally:
        if 'producer' in locals():
            logger.info("Closing producer...")
            producer.close()
            logger.info("Producer closed")

def main():
    parser = argparse.ArgumentParser(description='Генератор тестовых данных для Kafka')
    parser.add_argument('--count', type=int, default=100, help='Количество создаваемых объектов')
    parser.add_argument('--type', type=str, required=True, 
                      choices=['customers', 'products', 'sales', 'all'],
                      help='Тип генерируемых данных')

    args = parser.parse_args()
    
    # Проверяем подключение к Kafka перед началом работы
    try:
        test_producer = create_producer()
        test_producer.close()
        logger.info("Initial connection test successful")
    except Exception as e:
        logger.error(f"Failed to connect to Kafka: {e}")
        return

    generate_data(args.count, args.type)

if __name__ == '__main__':
    main() 