import http from 'k6/http';
import { check, sleep } from 'k6';

// Настройки теста
export const options = {
  stages: [
    { duration: '30s', target: 100 },
    { duration: '60s', target: 300 },
    { duration: '60s', target: 620 },
    { duration: '60s', target: 620 },
    { duration: '30s', target: 300 },
    { duration: '30s', target: 100 },
    { duration: '30s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'], // 95% запросов должны выполняться быстрее 500 мс
    http_req_failed: ['rate<0.01'], // Менее 1% запросов могут завершиться ошибкой
  },
};

// Основной сценарий теста
export default function () {
  // Отправляем GET-запрос к эндпоинту
  const response = http.get('http://192.168.56.11:8080/sales/average-weight');

  // Проверяем, что статус ответа равен 200
  check(response, {
    'status is 200': (r) => r.status === 200,
    'response time is less than 500ms': (r) => r.timings.duration < 500,
  });

  // Добавляем паузу между запросами (0.5–1 секунды)
  sleep(Math.random() * 0.5 + 0.5);
}
