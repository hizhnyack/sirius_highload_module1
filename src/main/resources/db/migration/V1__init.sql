-- Создание таблицы клиентов
CREATE TABLE IF NOT EXISTS customers (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(32) NOT NULL,
    has_discount_card BOOLEAN NOT NULL
);

-- Создание таблицы продуктов
CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(64) NOT NULL,
    price_per_kg NUMERIC(10,2) NOT NULL
);

-- Создание таблицы продаж
CREATE TABLE IF NOT EXISTS sales (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id),
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    weight NUMERIC(8,2) NOT NULL,
    date DATE NOT NULL,
    total_cost NUMERIC(12,2) NOT NULL
);
