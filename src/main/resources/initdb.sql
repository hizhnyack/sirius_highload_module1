
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
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Мартьян Харлампович Хохлов', '+79665341599', false);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Агата Руслановна Князева', '+79434871189', false);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Колесникова Анна Сергеевна', '+79391496197', false);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Фокин Николай Денисович', '+79185617675', false);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Ермакова Ирина Тарасовна', '+79422995590', false);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Фомина Лидия Николаевна', '+79525431858', true);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Георгий Феликсович Громов', '+79358754668', true);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Устинова Татьяна Макаровна', '+79596973503', false);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Новиков Остромир Егорович', '+79385091524', false);
INSERT INTO customers (full_name, phone, has_discount_card) VALUES ('Кудряшова Евпраксия Анатольевна', '+79227242143', false);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 3, '2025-03-22', 2.06, 206.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (3, 8, '2025-03-28', 9.15, 640.5);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (3, 8, '2025-02-25', 2.36, 165.2);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (5, 7, '2025-03-16', 4.67, 420.3);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (4, 9, '2025-03-13', 3.6, 432.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 10, '2025-03-24', 3.26, 326.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 8, '2025-03-13', 2.67, 267.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (3, 9, '2025-03-20', 2.63, 184.1);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (2, 4, '2025-04-03', 2.68, 134.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (3, 8, '2025-03-24', 8.43, 590.1);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (4, 2, '2025-03-21', 2.7, 324.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (4, 1, '2025-03-22', 4.6, 552.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (4, 10, '2025-03-25', 2.82, 338.4);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (5, 8, '2025-03-18', 2.08, 187.2);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (3, 10, '2025-02-23', 5.04, 352.8);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 4, '2025-02-28', 5.55, 555.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (2, 3, '2025-04-02', 6.92, 346.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (3, 3, '2025-03-28', 5.44, 380.8);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (1, 4, '2025-03-07', 9.17, 917.0);
INSERT INTO sales (product_id, customer_id, date, weight, total_cost) VALUES (5, 8, '2025-03-17', 3.9, 351.0);
