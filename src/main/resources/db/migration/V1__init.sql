-- Таблица клиентов
CREATE TABLE IF NOT EXISTS customer (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(32) NOT NULL,
    has_discount_card BOOLEAN NOT NULL
);

-- Таблица продуктов
CREATE TABLE IF NOT EXISTS product (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(64) NOT NULL,
    price_per_kg NUMERIC(10,2) NOT NULL
);

-- Таблица продаж
CREATE TABLE IF NOT EXISTS sale (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES product(id),
    customer_id BIGINT NOT NULL REFERENCES customer(id),
    weight NUMERIC(8,2) NOT NULL,
    date DATE NOT NULL
);
