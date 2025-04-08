#!/usr/bin/env python3

import argparse
import requests
from faker import Faker
import random
import time

fake = Faker('ru_RU')
session = requests.Session()

def clear_data(base_url):
    """Очистка всех данных"""
    endpoints = ['/api/products/clear', '/api/customers/clear', '/api/sales/clear']
    for endpoint in endpoints:
        try:
            response = session.delete(f"{base_url}{endpoint}")
            response.raise_for_status()
            print(f"Очистка {endpoint} выполнена успешно")
        except requests.exceptions.RequestException as e:
            print(f"Ошибка при очистке {endpoint}: {e}")

def generate_products(base_url, count):
    """Генерация продуктов"""
    categories = ['Овощи', 'Фрукты']
    for _ in range(count):
        product = {
            'name': fake.word().capitalize(),
            'category': random.choice(categories),
            'pricePerKg': round(random.uniform(50, 500), 2)
        }
        try:
            response = session.post(f"{base_url}/api/products", json=product)
            response.raise_for_status()
        except requests.exceptions.RequestException as e:
            print(f"Ошибка при создании продукта: {e}")

def generate_customers(base_url, count):
    """Генерация клиентов"""
    for _ in range(count):
        customer = {
            'name': fake.name(),
            'email': fake.email(),
            'phone': fake.phone_number()
        }
        try:
            response = session.post(f"{base_url}/api/customers", json=customer)
            response.raise_for_status()
        except requests.exceptions.RequestException as e:
            print(f"Ошибка при создании клиента: {e}")

def generate_sales(base_url, count):
    """Генерация продаж"""
    print("Начинаю генерацию продаж...")
    # Получаем список продуктов и клиентов
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

    for i in range(count):
        print(f"\nСоздаю продажу {i+1} из {count}...")
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
            url = f"{base_url}/api/sales/create"
            print(f"Отправляю запрос на URL: {url}")
            print(f"Данные запроса: {sale}")
            response = session.post(url, json=sale)
            print(f"URL запроса: {response.request.url}")
            print(f"Метод запроса: {response.request.method}")
            print(f"Заголовки запроса: {response.request.headers}")
            print(f"Тело запроса: {response.request.body}")
            response.raise_for_status()
            print(f"Успешно создана продажа: {response.json()}")
        except requests.exceptions.RequestException as e:
            print(f"Ошибка при создании продажи: {e}")
            print(f"Ответ сервера: {response.text}")

def main():
    parser = argparse.ArgumentParser(description='Генератор тестовых данных для REST-сервиса')
    parser.add_argument('--count', type=int, default=500, help='Количество создаваемых объектов')
    parser.add_argument('--endpoint', type=str, required=True, help='API-эндпоинт (products, customers, sales)')
    parser.add_argument('--base-url', type=str, default='http://localhost:8080/', help='Базовый URL API')
    
    args = parser.parse_args()

    # Очищаем данные
    clear_data(args.base_url)
    time.sleep(1)  # Даем время на очистку

    # Генерируем данные в зависимости от эндпоинта
    if args.endpoint == 'products':
        generate_products(args.base_url, args.count)
    elif args.endpoint == 'customers':
        generate_customers(args.base_url, args.count)
    elif args.endpoint == 'sales':
        generate_sales(args.base_url, args.count)
    else:
        print(f"Неизвестный эндпоинт: {args.endpoint}")

if __name__ == '__main__':
    main() 