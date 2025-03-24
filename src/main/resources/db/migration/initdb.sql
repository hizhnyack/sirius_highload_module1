CREATE TABLE IF NOT EXISTS products (
                                        id SERIAL PRIMARY KEY,
                                        name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price_per_kg NUMERIC(10, 2) NOT NULL
    );

CREATE TABLE IF NOT EXISTS customers (
                                         id SERIAL PRIMARY KEY,
                                         full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    has_discount_card BOOLEAN NOT NULL
    );

CREATE TABLE IF NOT EXISTS sales (
                                     id SERIAL PRIMARY KEY,
                                     product_id INT REFERENCES products(id),
    customer_id INT REFERENCES customers(id),
    date DATE NOT NULL,
    weight NUMERIC(10, 2) NOT NULL,
    total_cost NUMERIC(10, 2) NOT NULL
    );

-- Вставка продуктов
INSERT INTO products (name, category, price_per_kg) VALUES ('Яблоки', 'Фрукты', 100.0);
INSERT INTO products (name, category, price_per_kg) VALUES ('Картофель', 'Овощи', 50.0);
INSERT INTO products (name, category, price_per_kg) VALUES ('Морковь', 'Овощи', 70.0);
INSERT INTO products (name, category, price_per_kg) VALUES ('Бананы', 'Фрукты', 120.0);
INSERT INTO products (name, category, price_per_kg) VALUES ('Помидоры', 'Овощи', 90.0);

-- Вставка клиентов
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Alexis Bowman', '(230)661-7196x47065', True);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Christian Mccoy', '855-821-5088x4001', True);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Jose Stewart', '+1-754-647-2239x76610', False);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Larry Carter', '001-548-519-7896', False);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('William Simmons', '9102590265', False);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Shelley Dean', '635-770-5695x2524', False);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Barry Taylor', '519-269-0037', True);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Antonio Jackson', '001-219-378-0297', False);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Sean Hughes', '+1-642-644-3070x706', True);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Robert Wiley', '(766)859-7345', True);

-- Вставка продаж (даты за последний месяц - март 2025)
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 4, '2025-03-08', 10.0, 1000.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (3, 2, '2025-03-04', 6.21, 434.7);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (2, 3, '2025-03-14', 1.31, 65.5);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (5, 7, '2025-03-25', 4.57, 411.3);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (4, 3, '2025-03-06', 7.48, 897.6);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 5, '2025-03-16', 6.99, 699.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (4, 1, '2025-03-14', 9.72, 1166.4);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (3, 5, '2025-03-07', 5.64, 394.8);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 10, '2025-03-18', 4.13, 413.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 2, '2025-03-20', 9.46, 946.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (3, 10, '2025-03-08', 5.76, 403.2);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 7, '2025-03-13', 1.79, 179.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 1, '2025-03-10', 7.46, 746.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (3, 10, '2025-03-06', 7.85, 549.5);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (2, 2, '2025-03-10', 5.38, 269.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 8, '2025-03-23', 8.34, 834.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (5, 5, '2025-03-18', 3.37, 303.3);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 5, '2025-03-07', 9.48, 948.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (2, 7, '2025-03-22', 3.22, 161.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 9, '2025-03-18', 1.77, 177.0);