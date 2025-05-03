#!/usr/bin/env python3

import argparse
import requests
from tqdm import tqdm
from faker import Faker
import random
import time

fake = Faker('ru_RU')
session = requests.Session()

# Словари для генерации названий продуктов
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
        'яйца', 'майонез', 'кетчуп', 'горчица', 'соль', 'сахар', 'мука', 'рис',
        'макароны', 'гречка', 'овсянка', 'манка', 'пшено', 'перловка', 'кукуруза', 'горох',
        'консервы', 'тушенка', 'сгущенка', 'масло', 'маргарин', 'дрожжи', 'уксус', 'специи'
    ],
    'Напитки': [
        'молоко', 'кефир', 'творог', 'сметана', 'сыр', 'йогурт',
        'чай', 'кофе', 'какао', 'сок', 'вода', 'лимонад', 'компот', 'морс',
        'пиво', 'минеральная вода', 'газировка', 'энергетик', 'квас', 'ром', 'виски', 'джин'
    ]
}

PRODUCT_ADJECTIVES = [
    'Дружба народов', 'Зелёный удав', 'Красный великан', 'Золотой урожай', 'Свежий ветер',
    'Весенний', 'Летний', 'Осенний', 'Зимний', 'Домашний', 'Деревенский', 'Фермерский',
    'Экстра', 'Премиум', 'Люкс', 'Классический', 'Традиционный', 'Особый', 'Отборный',
    'Сочный', 'Сладкий', 'Ароматный', 'Нежный', 'Свежий', 'Хрустящий', 'Мягкий', 'Твердый',
    'Быстрый', 'Мгновенный', 'Растворимый', 'Натуральный', 'Органический', 'Экологичный',
    'Диетический', 'Фитнес', 'Лайт', 'Без сахара', 'Без глютена', 'Без лактозы',
    'Сливочный', 'Шоколадный', 'Ванильный', 'Карамельный', 'Фруктовый', 'Ягодный',
    'Ореховый', 'Медовый', 'Кленовый', 'Кокосовый', 'Мятный', 'Имбирный'
]

def clear_data(base_url):
    """Очистка всех данных"""
    endpoints = ['/api/sales/clear','/api/products/clear', '/api/customers/clear']
    for endpoint in endpoints:
        try:
            response = session.delete(f"{base_url}{endpoint}")
            response.raise_for_status()
            print(f"Очистка {endpoint} выполнена успешно")
        except requests.exceptions.RequestException as e:
            print(f"Ошибка при очистке {endpoint}: {e}")

def generate_phone_number():
    """Генерация телефонного номера в формате +7 (XXX) XXX-XX-XX"""
    area_code = random.randint(900, 999)
    first_part = random.randint(100, 999)
    second_part = random.randint(10, 99)
    third_part = random.randint(10, 99)
    return f"+7 ({area_code}) {first_part}-{second_part}-{third_part}"

from tqdm import tqdm  # Импортируем tqdm для прогресс-бара

def generate_customers(base_url, count):
    """Генерация клиентов с прогресс-баром"""
    print("Генерация клиентов...")
    for _ in tqdm(range(count), desc="Создание клиентов", unit="клиент"):
        customer = {
            'fullName': fake.name(),
            'phone': generate_phone_number(),
            'hasDiscountCard': random.choice([True, False])
        }
        try:
            url = base_url.rstrip('/') + '/api/customers'
            response = session.post(url, json=customer)
            response.raise_for_status()
            customer_data = response.json()
            discount_status = "Есть" if customer_data['hasDiscountCard'] else "Нет"
            # Выводим информацию только о первых нескольких клиентах (опционально)
            if _ < 7:
                tqdm.write(f"Создан клиент: ID: {customer_data['id']}, ФИО: {customer_data['fullName']}, "
                           f"Телефон: {customer_data['phone']}, Скидочная карта: {discount_status}")
        except requests.exceptions.RequestException as e:
            tqdm.write(f"Ошибка при создании клиента: {e}")

def generate_products(base_url, count):
    """Генерация продуктов с прогресс-баром"""
    print("Генерация продуктов...")
    for _ in tqdm(range(count), desc="Создание продуктов", unit="продукт"):
        # Выбираем случайную категорию
        category = random.choice(list(CATEGORIES.keys()))
        # Выбираем случайный продукт из этой категории
        product_type = random.choice(CATEGORIES[category])
        adjective = random.choice(PRODUCT_ADJECTIVES)
        product = {
            'name': f"{product_type} {adjective}",
            'category': category,
            'pricePerKg': round(random.uniform(50, 500), 2)
        }
        try:
            response = session.post(f"{base_url}/api/products", json=product)
            response.raise_for_status()
            product_data = response.json()
            # Выводим информацию только о первых нескольких продуктах (опционально)
            if _ < 7:
                tqdm.write(f"Создан продукт: ID: {product_data['id']}, Название: {product_data['name']}, "
                      f"Категория: {product_data['category']}, Цена за кг: {product_data['pricePerKg']} руб.")
        except requests.exceptions.RequestException as e:
            tqdm.write(f"Ошибка при создании продукта: {e}")

def generate_sales(base_url, count):
    """Генерация продаж с прогресс-баром"""
    print("Начинаю генерацию продаж...")
    try:
        print("Получаю список продуктов...")
        products = session.get(f"{base_url}/api/products").json()
        print(f"Получено {len(products)} продуктов")
        print("Получаю список клиентов...")
        customers = session.get(f"{base_url}/api/customers").json()
        print(f"Получено {len(customers)} клиентов")
    except requests.exceptions.RequestException as e:
        print(f"Ошибка при получении данных: {e}")
        return

    if not products or not customers:
        print("Нет доступных продуктов или клиентов")
        return

    print("Генерация продаж...")
    for i in tqdm(range(count), desc="Создание продаж", unit="продажа"):
        product = random.choice(products)
        customer = random.choice(customers)
        weight = round(random.uniform(0.1, 10.0), 2)
        date = fake.date_between(start_date='-1y', end_date='today').isoformat()

        sale = {
            'productId': product['id'],
            'customerId': customer['id'],
            'weight': weight,
            'date': date
        }
        try:
            response = session.post(f"{base_url}/api/sales/create", json=sale)
            response.raise_for_status()
            sale_data = response.json()
            total_price = round(weight * product['pricePerKg'], 2)
            # Выводим информацию только о первых нескольких продажах (опционально)
            if i < 7:
                tqdm.write(f"Создана продажа: ID: {sale_data['id']}, ID продукта: {sale['productId']}, "
                      f"ID клиента: {sale['customerId']}, Дата: {sale['date']}, Вес: {weight} кг, "
                      f"Цена за кг: {product['pricePerKg']} руб., Итого: {total_price} руб.")
        except requests.exceptions.RequestException as e:
            tqdm.write(f"Ошибка при создании продажи: {e}")

def generate_all(base_url, count):
    """Генерация данных во всех таблицах"""
    print("\n1. Генерация клиентов...")
    generate_customers(base_url, count)
    
    print("\n2. Генерация продуктов...")
    generate_products(base_url, count)
    
    print("\n3. Генерация продаж...")
    generate_sales(base_url, count)

def main():
    parser = argparse.ArgumentParser(description='Генератор тестовых данных для REST-сервиса')
    parser.add_argument('--count', type=int, default=500, help='Количество создаваемых объектов')
    parser.add_argument('--endpoint', type=str, help='API-эндпоинт (products, customers, sales, all)')
    parser.add_argument('--url', type=str, default='http://localhost:8080/', help='URL API')
    parser.add_argument('--clear', type=str, help='Очистить данные (customers, products, sales или all)')

    args = parser.parse_args()

    # Если указан параметр clear, очищаем данные и завершаем работу
    if args.clear and not args.endpoint:
        clear_data(args.url)
        return

    # Проверяем, что endpoint указан для генерации данных
    if not args.endpoint:
        print("Ошибка: необходимо указать --endpoint для генерации данных")
        return

    # Очищаем данные если указан флаг --clear вместе с endpoint
    if args.clear:
        clear_data(args.url)
        time.sleep(1)  # Даем время на очистку

    # Генерируем данные в зависимости от эндпоинта
    if args.endpoint == 'products':
        generate_products(args.url, args.count)
    elif args.endpoint == 'customers':
        generate_customers(args.url, args.count)
    elif args.endpoint == 'sales':
        generate_sales(args.url, args.count)
    elif args.endpoint == 'all':
        generate_all(args.url, args.count)
    else:
        print(f"Неизвестный эндпоинт: {args.endpoint}")

if __name__ == '__main__':
    main() 
