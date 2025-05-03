
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
INSERT INTO products (name, category, price_per_kg) VALUES ('Яблоки', 'Фрукты', 100.0);
INSERT INTO products (name, category, price_per_kg) VALUES ('Картофель', 'Овощи', 50.0);
INSERT INTO products (name, category, price_per_kg) VALUES ('Морковь', 'Овощи', 70.0);
INSERT INTO products (name, category, price_per_kg) VALUES ('Бананы', 'Фрукты', 120.0);
INSERT INTO products (name, category, price_per_kg) VALUES ('Помидоры', 'Овощи', 90.0);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Анастасия Феликсовна Ефремова', '+79909311321', false);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Марфа Никифоровна Яковлева', '+79524658439', false);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Павлов Зосима Иларионович', '+79059156222', false);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Жданова Анжела Владимировна', '+79129379894', true);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Любосмысл Гордеевич Сергеев', '+79919359360', true);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Кононов Ефим Федотович', '+79013880932', true);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Мартынова Лукия Андреевна', '+79276012369', true);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Беляев Творимир Харламович', '+79367599669', false);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Карпова Мария Игоревна', '+79084456578', false);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Захарова Анна Робертовна', '+79243120532', true);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 6, '2025-04-02', 7.72, 772.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (3, 8, '2025-04-03', 4.17, 291.9);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (4, 4, '2025-02-25', 4.67, 560.4);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (3, 9, '2025-03-20', 4.49, 314.3);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 9, '2025-03-29', 9.74, 974.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (5, 10, '2025-03-06', 4.79, 431.1);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 4, '2025-03-06', 7.38, 738.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 10, '2025-02-26', 9.04, 904.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (5, 1, '2025-03-31', 1.98, 178.2);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (5, 6, '2025-03-16', 1.31, 117.9);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (4, 6, '2025-03-24', 3.96, 475.2);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (5, 7, '2025-03-29', 2.06, 185.4);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (4, 10, '2025-02-22', 5.14, 616.8);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 9, '2025-03-27', 9.2, 920.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (4, 9, '2025-03-25', 4.39, 526.8);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (2, 6, '2025-03-20', 7.31, 365.5);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (5, 6, '2025-03-04', 5.17, 465.3);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (5, 8, '2025-03-19', 8.88, 799.2);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (4, 5, '2025-02-28', 5.16, 619.2);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (5, 2, '2025-02-26', 3.4, 306.0);
