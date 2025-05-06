import http from 'k6/http';
import { check, sleep } from 'k6';
import { randomIntBetween } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';
import { Trend } from 'k6/metrics';

// Базовые URL'ы для сервисов
const BASE_URL = 'http://10.60.3.22:8080'; // Основной сервис
const ADDITIONAL_URL = 'http://10.60.3.22:8081'; // Дополнительный сервис

// Конфигурация нагрузки
const TARGET_VUS = 30; // Уменьшаем количество виртуальных пользователей
const INIT_PRODUCTS = 30;
const INIT_CUSTOMERS = 30;
const SLEEP_TIME = 0.5; // Увеличиваем задержку между запросами

// Метрики задержек
const readHeavyLatency = new Trend('read_heavy_latency');
const balancedLatency = new Trend('balanced_latency');
const writeHeavyLatency = new Trend('write_heavy_latency');

// Категории продуктов из data_generator.py
const CATEGORIES = {
    'Овощи': ['огурцы', 'помидоры', 'картофель', 'морковь', 'лук', 'капуста', 'перец', 'баклажаны'],
    'Фрукты': ['яблоки', 'груши', 'бананы', 'апельсины', 'мандарины', 'лимон', 'виноград', 'персики'],
    'Вкусняшки': ['колбаса', 'сосиски', 'ветчина', 'курица', 'говядина', 'свинина', 'рыба', 'креветки'],
    'Бакалея': ['хлеб', 'булочки', 'батон', 'круассан', 'пирожки', 'печенье', 'торт', 'пирог'],
    'Напитки': ['молоко', 'кефир', 'творог', 'сметана', 'сыр', 'йогурт', 'чай', 'кофе']
};

const PRODUCT_ADJECTIVES = [
    'Дружба народов', 'Зелёный удав', 'Красный великан', 'Золотой урожай', 'Свежий ветер',
    'Весенний', 'Летний', 'Осенний', 'Зимний', 'Домашний', 'Деревенский', 'Фермерский'
];

// Сценарии нагрузки
const scenarios = {
    read_heavy: {
        name: '95% read / 5% write',
        read_ratio: 0.95,
        duration: '1m',
        target: TARGET_VUS,
        metric: readHeavyLatency
    },
    balanced: {
        name: '50% read / 50% write',
        read_ratio: 0.5,
        duration: '1m',
        target: TARGET_VUS,
        metric: balancedLatency
    },
    write_heavy: {
        name: '5% read / 95% write',
        read_ratio: 0.05,
        duration: '1m',
        target: TARGET_VUS,
        metric: writeHeavyLatency
    }
};

// Настройки k6
export const options = {
    scenarios: {
        read_heavy_test: {
            executor: 'ramping-vus',
            startVUs: TARGET_VUS,
            stages: [
                { duration: scenarios.read_heavy.duration, target: scenarios.read_heavy.target },
            ],
            exec: 'mixedTraffic',
            env: { TEST_SCENARIO: 'read_heavy' }
        },
        balanced_test: {
            executor: 'ramping-vus',
            startVUs: TARGET_VUS,
            startTime: '1m',
            stages: [
                { duration: scenarios.balanced.duration, target: scenarios.balanced.target },
            ],
            exec: 'mixedTraffic',
            env: { TEST_SCENARIO: 'balanced' }
        },
        write_heavy_test: {
            executor: 'ramping-vus',
            startVUs: TARGET_VUS,
            startTime: '2m',
            stages: [
                { duration: scenarios.write_heavy.duration, target: scenarios.write_heavy.target },
            ],
            exec: 'mixedTraffic',
            env: { TEST_SCENARIO: 'write_heavy' }
        }
    },
    thresholds: {
        'read_heavy_latency': ['avg<500'],
        'balanced_latency': ['avg<500'],
        'write_heavy_latency': ['avg<500']
    }
};

// Хранилище ID для связи между запросами
let productIds = [];
let customerIds = [];
let saleIds = [];

// Генерация случайного продукта
function generateProduct() {
    const category = Object.keys(CATEGORIES)[Math.floor(Math.random() * Object.keys(CATEGORIES).length)];
    const productType = CATEGORIES[category][Math.floor(Math.random() * CATEGORIES[category].length)];
    const adjective = PRODUCT_ADJECTIVES[Math.floor(Math.random() * PRODUCT_ADJECTIVES.length)];
    
    return {
        name: `${productType} ${adjective}`,
        category: category,
        pricePerKg: parseFloat((Math.random() * 450 + 50).toFixed(2))
    };
}

// Генерация случайного покупателя
function generateCustomer() {
    const names = [
        'Иван', 'Петр', 'Алексей', 'Сергей', 'Дмитрий', 'Андрей', 'Николай',
        'Владимир', 'Александр', 'Максим', 'Артем', 'Михаил', 'Даниил', 'Егор',
        'Кирилл', 'Илья', 'Матвей', 'Тимофей', 'Роман', 'Владислав', 'Ярослав',
        'Федор', 'Георгий', 'Константин', 'Лев', 'Павел', 'Арсений', 'Денис',
        'Степан', 'Захар', 'Глеб', 'Давид', 'Марк', 'Семен', 'Виктор'
    ];
    
    const surnames = [
        'Иванов', 'Петров', 'Сидоров', 'Смирнов', 'Кузнецов', 'Попов', 'Васильев',
        'Соколов', 'Михайлов', 'Новиков', 'Федоров', 'Морозов', 'Волков', 'Алексеев',
        'Лебедев', 'Семенов', 'Егоров', 'Павлов', 'Козлов', 'Степанов', 'Николаев',
        'Орлов', 'Андреев', 'Макаров', 'Никитин', 'Захаров', 'Зайцев', 'Соловьев',
        'Борисов', 'Яковлев', 'Григорьев', 'Романов', 'Воробьев', 'Сергеев', 'Кузьмин',
        'Фролов', 'Александров', 'Дмитриев', 'Королев', 'Гусев', 'Киселев', 'Ильин'
    ];
    
    const patronymics = [
        'Иванович', 'Петрович', 'Александрович', 'Дмитриевич', 'Сергеевич',
        'Андреевич', 'Алексеевич', 'Максимович', 'Владимирович', 'Николаевич',
        'Артемович', 'Михайлович', 'Данилович', 'Егорович', 'Кириллович',
        'Ильич', 'Матвеевич', 'Тимофеевич', 'Романович', 'Владиславович',
        'Ярославович', 'Федорович', 'Георгиевич', 'Константинович', 'Львович',
        'Павлович', 'Арсеньевич', 'Денисович', 'Степанович', 'Захарович'
    ];
    
    const name = names[Math.floor(Math.random() * names.length)];
    const surname = surnames[Math.floor(Math.random() * surnames.length)];
    const patronymic = patronymics[Math.floor(Math.random() * patronymics.length)];
    
    return {
        fullName: `${surname} ${name} ${patronymic}`,
        phone: `+7 (${randomIntBetween(900, 999)}) ${randomIntBetween(100, 999)}-${randomIntBetween(10, 99)}-${randomIntBetween(10, 99)}`,
        hasDiscountCard: Math.random() > 0.5
    };
}

// Генерация случайной даты от начала 2024 года до текущей даты
function randomDate() {
    const now = new Date();
    const end = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const start = new Date(2024, 0, 1); // 1 января 2024
    const date = new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()));
    return date.toISOString().split('T')[0];
}

// Генерация даты за последний месяц
function generateLastMonthDate() {
    const now = new Date();
    const end = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const start = new Date(end);
    start.setMonth(end.getMonth() - 1);
    const date = new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()));
    return date.toISOString().split('T')[0];
}

// Генерация случайной продажи
function generateSale() {
    if (productIds.length === 0 || customerIds.length === 0) return null;
    const productId = productIds[randomIntBetween(0, productIds.length - 1)];
    const customerId = customerIds[randomIntBetween(0, customerIds.length - 1)];
    
    // Генерируем вес в диапазоне 0.1-10.0 кг с двумя знаками после запятой
    const weight = parseFloat((Math.random() * 9.9 + 0.1).toFixed(2));
    
    return {
        productId: productId,
        customerId: customerId,
        weight: weight,
        date: randomDate()
    };
}

// Основная функция генерации трафика
export function mixedTraffic(initData) {
    // Инициализируем id из setup
    if (initData && initData.productIds && productIds.length === 0) productIds = initData.productIds;
    if (initData && initData.customerIds && customerIds.length === 0) customerIds = initData.customerIds;

    const scenario = scenarios[__ENV.TEST_SCENARIO];
    const random = Math.random();
    let res;

    if (random < scenario.read_ratio) {
        // READ: случайно выбираем GET-ручку
        const readEndpoints = [
            () => http.get(`${BASE_URL}/api/products`),
            () => http.get(`${BASE_URL}/api/customers`),
            () => http.get(`${BASE_URL}/api/sales`),
            () => {
                // Добавляем дополнительную задержку перед запросом к дополнительному сервису
                sleep(0.1);
                return http.get(`${ADDITIONAL_URL}/api/sales/average-weight/last-month`);
            },
            () => {
                if (productIds.length > 0) {
                    const id = productIds[randomIntBetween(0, productIds.length - 1)];
                    return http.get(`${BASE_URL}/api/products/${id}`);
                }
                return { status: 404 };
            },
            () => {
                if (customerIds.length > 0) {
                    const id = customerIds[randomIntBetween(0, customerIds.length - 1)];
                    return http.get(`${BASE_URL}/api/customers/${id}`);
                }
                return { status: 404 };
            },
            () => {
                if (saleIds.length > 0) {
                    const id = saleIds[randomIntBetween(0, saleIds.length - 1)];
                    return http.get(`${BASE_URL}/api/sales/${id}`);
                }
                return { status: 404 };
            }
        ];
        const idx = randomIntBetween(0, readEndpoints.length - 1);
        res = readEndpoints[idx]();
        check(res, { 
            'GET статус 200/204/404': (r) => r.status === 200 || r.status === 204 || r.status === 404 
        });
    } else {
        // WRITE: случайно выбираем POST/PUT/DELETE-ручку
        const writeEndpoints = [
            // POST sale
            () => {
                const sale = generateSale();
                if (!sale) return { status: 400 };
                const r = http.post(`${BASE_URL}/api/sales/create`, JSON.stringify(sale), {
                    headers: { 'Content-Type': 'application/json' }
                });
                if (r.status === 200 && r.json().id) saleIds.push(r.json().id);
                return r;
            },
            // POST product (с вероятностью 1/30)
            () => {
                if (Math.random() < 0.033) { // примерно 1/30
                    const product = generateProduct();
                    const r = http.post(`${BASE_URL}/api/products`, JSON.stringify(product), {
                        headers: { 'Content-Type': 'application/json' }
                    });
                    if (r.status === 200 && r.json().id) {
                        productIds.push(r.json().id);
                    }
                    return r;
                }
                return { status: 200 }; // пропускаем создание продукта
            },
            // POST customer (с вероятностью 1/30)
            () => {
                if (Math.random() < 0.033) { // примерно 1/30
                    const customer = generateCustomer();
                    const r = http.post(`${BASE_URL}/api/customers`, JSON.stringify(customer), {
                        headers: { 'Content-Type': 'application/json' }
                    });
                    if (r.status === 200 && r.json().id) {
                        customerIds.push(r.json().id);
                    }
                    return r;
                }
                return { status: 200 }; // пропускаем создание клиента
            }
        ];
        const idx = randomIntBetween(0, writeEndpoints.length - 1);
        res = writeEndpoints[idx]();
        check(res, {
            'POST / статус 200 или 400': (r) => r.status === 200 || r.status === 400,
        });
    }

    // Добавляем метрику
    if (res?.timings?.duration) {
        scenario.metric.add(res.timings.duration);
    }

    sleep(SLEEP_TIME); // Используем константу для задержки
}

// Инициализация продуктов и клиентов перед тестом
export function setup() {
    // Создаём начальные продукты
    for (let i = 0; i < INIT_PRODUCTS; i++) {
        const product = generateProduct();
        const r = http.post(`${BASE_URL}/api/products`, JSON.stringify(product), {
            headers: { 'Content-Type': 'application/json' }
        });
        if (r.status === 200 && r.json().id) {
            productIds.push(r.json().id);
        }
    }
    
    // Создаём начальных клиентов
    for (let i = 0; i < INIT_CUSTOMERS; i++) {
        const customer = generateCustomer();
        const r = http.post(`${BASE_URL}/api/customers`, JSON.stringify(customer), {
            headers: { 'Content-Type': 'application/json' }
        });
        if (r.status === 200 && r.json().id) {
            customerIds.push(r.json().id);
        }
    }
    
    // Создаём начальные продажи
    for (let i = 0; i < INIT_PRODUCTS; i++) {
        const sale = generateSale();
        if (sale) {
            const r = http.post(`${BASE_URL}/api/sales/create`, JSON.stringify(sale), {
                headers: { 'Content-Type': 'application/json' }
            });
            if (r.status === 200 && r.json().id) {
                saleIds.push(r.json().id);
            }
        }
    }

    // Гарантируем создание хотя бы одной продажи за последний месяц
    if (productIds.length > 0 && customerIds.length > 0) {
        const lastMonthSale = {
            productId: productIds[0],
            customerId: customerIds[0],
            weight: parseFloat((Math.random() * 9.9 + 0.1).toFixed(2)),
            date: generateLastMonthDate()
        };
        const r = http.post(`${BASE_URL}/api/sales/create`, JSON.stringify(lastMonthSale), {
            headers: { 'Content-Type': 'application/json' }
        });
        if (r.status === 200 && r.json().id) {
            saleIds.push(r.json().id);
        }
    }
    
    console.log('Инициализационные данные добавлены в базу данных');
    return { productIds, customerIds };
}

// Точка входа по умолчанию
export default function (initData) {
    return mixedTraffic(initData);
}