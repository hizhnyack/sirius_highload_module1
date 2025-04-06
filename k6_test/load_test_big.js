import http from 'k6/http';
import { check, sleep } from 'k6';

// Настройки теста
export const options = {
  stages: [
    { duration: '10s', target: 5 },
    { duration: '20s', target: 20 },
    { duration: '15s', target: 50 },
    { duration: '30s', target: 50 },
    { duration: '20s', target: 30 },
    { duration: '40s', target: 30 },
    { duration: '10s', target: 70 },
    { duration: '30s', target: 70 },
    { duration: '15s', target: 100 },
    { duration: '20s', target: 100 },
    { duration: '10s', target: 150 },
    { duration: '15s', target: 150 },
    { duration: '20s', target: 100 },
    { duration: '15s', target: 50 },
    { duration: '10s', target: 20 },
    { duration: '10s', target: 10 },
    { duration: '5s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'], // 95% запросов должны выполняться быстрее 500 мс
    http_req_failed: ['rate<0.01'],   // Менее 1% запросов могут завершиться ошибкой
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

  // Добавляем случайную паузу между запросами (от 0.5 до 1.5 секунд)
  sleep(Math.random() * 1 + 0.5);
}
