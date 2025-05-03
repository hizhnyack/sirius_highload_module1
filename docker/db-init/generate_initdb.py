import random
from faker import Faker
from datetime import datetime, timedelta

# Инициализация Faker для генерации случайных данных
fake = Faker('ru_RU')  # Используем русскую локаль для имен

def generate_phone():
    """Генерация телефонного номера длиной не более 12 символов"""
    return f"+7{random.randint(900, 999)}{random.randint(1000000, 9999999)}"

def validate_unique_names(customers, products):
    """Проверка на уникальность имен в customers и products"""
    unique_names = set()
    unique_products = set()

    # Проверка products
    for product in products:
        if product[0] in unique_products:
            raise ValueError(f"Дубликат названия продукта: {product[0]}")
        unique_products.add(product[0])

    # Проверка customers
    for customer in customers:
        if customer[0] in unique_names:
            raise ValueError(f"Дубликат имени клиента: {customer[0]}")
        unique_names.add(customer[0])

    return True

# Функция для генерации SQL-файла
def generate_initdb_sql(filename):
    with open(filename, "w") as f:
        # Создание таблиц с проверкой на существование
        f.write("""
-- Очистка таблиц (если существуют)
DROP TABLE IF EXISTS sales;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS customers;

-- Создание таблиц
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(50) NOT NULL,
    price_per_kg NUMERIC(10, 2) NOT NULL
);

CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(12) NOT NULL,
    has_discount_card BOOLEAN NOT NULL
);

CREATE TABLE sales (
    id SERIAL PRIMARY KEY,
    product_id INT REFERENCES products(id),
    customer_id INT REFERENCES customers(id),
    date DATE NOT NULL,
    weight NUMERIC(10, 2) NOT NULL,
    total_cost NUMERIC(10, 2) NOT NULL
);
""")

        # Генерация данных для таблицы products
        products = [
            ("Яблоки", "Фрукты", 100.0),
            ("Картофель", "Овощи", 50.0),
            ("Морковь", "Овощи", 70.0),
            ("Бананы", "Фрукты", 120.0),
            ("Помидоры", "Овощи", 90.0),
        ]

        # Генерация данных для таблицы customers
        customers = []
        for _ in range(10):
            full_name = fake.name()
            phone = generate_phone()
            has_discount_card = random.choice([True, False])
            customers.append((full_name, phone, has_discount_card))

        # Проверка уникальности имен
        validate_unique_names(customers, products)

        # Вставка продуктов
        for product in products:
            f.write(f"INSERT INTO products (name, category, price_per_kg) VALUES ('{product[0]}', '{product[1]}', {product[2]});\n")

        # Вставка клиентов
        for customer in customers:
            f.write(f"INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('{customer[0]}', '{customer[1]}', {str(customer[2]).lower()});\n")

        # Генерация данных для таблицы sales (не более 1.5 месяцев назад)
        end_date = datetime.now()
        start_date = end_date - timedelta(days=45)  # 1.5 месяца назад

        for _ in range(20):
            product_id = random.randint(1, len(products))
            customer_id = random.randint(1, 10)
            date = fake.date_between(start_date=start_date, end_date=end_date).strftime('%Y-%m-%d')
            weight = round(random.uniform(1.0, 10.0), 2)
            total_cost = round(weight * products[product_id - 1][2], 2)
            f.write(f"INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES ({product_id}, {customer_id}, '{date}', {weight}, {total_cost});\n")

# Запуск генерации
if __name__ == "__main__":
    try:
        generate_initdb_sql("initdb.sql")
        print("Файл initdb.sql успешно создан!")
    except ValueError as e:
        print(f"Ошибка при генерации данных: {e}")