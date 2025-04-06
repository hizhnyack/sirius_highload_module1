import http from 'k6/http';
import { check, sleep } from 'k6';

// Настройки теста
export const options = {
  stages: [
    { duration: '10s', target: 10 },
    { duration: '20s', target: 20 },
    { duration: '30s', target: 50 },
    { duration: '20s', target: 10 },
    { duration: '10s', target: 0 },
  ],
};

export default function () {
  // Выполняем GET-запрос к эндпоинту
  let res = http.get('http://192.168.56.11:8080/sales/average-weight');

  // Проверяем, что статус ответа равен 200
  check(res, {
    'status was 200': (r) => r.status == 200,
    'response time under 500ms': (r) => r.timings.duration < 500, // Дополнительная проверка времени отклика
  });

  // Добавляем случайную паузу между запросами (от 0.5 до 1.5 секунд)
  sleep(Math.random() * 1 + 0.5);
}
