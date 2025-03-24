
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
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Анжела Валентиновна Титова', '+79529364218', true);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Егоров Аркадий Теймуразович', '+79889740896', true);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Корнилова Тамара Антоновна', '+79962276228', true);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Анисим Демидович Шилов', '+79628519764', false);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Радован Дорофеевич Панов', '+79579408294', false);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Семенова Людмила Андреевна', '+79539902797', false);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Григорьева Евфросиния Сергеевна', '+79414007307', true);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Александр Валерьевич Медведев', '+79372115110', true);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Леонтий Ильясович Рожков', '+79532063347', false);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Ирина Петровна Смирнова', '+79307489788', false);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (3, 10, '2025-03-15', 9.52, 666.4);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (2, 2, '2025-02-15', 8.38, 419.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (3, 4, '2025-03-22', 1.3, 91.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (4, 4, '2025-02-08', 1.56, 187.2);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (4, 8, '2025-03-08', 3.78, 453.6);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 9, '2025-03-13', 5.05, 505.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (4, 5, '2025-02-10', 7.23, 867.6);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 9, '2025-03-16', 1.39, 139.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (3, 2, '2025-03-11', 1.25, 87.5);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (5, 7, '2025-03-15', 5.24, 471.6);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (4, 8, '2025-03-16', 8.58, 1029.6);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (4, 2, '2025-02-16', 1.35, 162.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (5, 4, '2025-03-20', 1.69, 152.1);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (5, 8, '2025-02-10', 4.53, 407.7);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (3, 3, '2025-03-03', 3.73, 261.1);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 6, '2025-03-03', 6.3, 630.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (4, 2, '2025-02-12', 1.91, 229.2);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (4, 10, '2025-02-11', 4.89, 586.8);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 6, '2025-02-25', 7.32, 732.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (3, 5, '2025-03-04', 3.58, 250.6);
