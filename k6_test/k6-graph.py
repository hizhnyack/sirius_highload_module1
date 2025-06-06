import json
import matplotlib.pyplot as plt
from datetime import datetime
import numpy as np

# Чтение данных из results.json
timestamps = []
response_times = []
active_vus_timestamps = []
active_vus_values = []

try:
    with open('results_hard.json', 'r') as f:
        for line in f:
            try:
                record = json.loads(line)
                metric = record.get("metric")
                data = record.get("data", {})

                # Обработка метрики времени отклика
                if metric == "http_req_duration":
                    if "time" in data and "value" in data:
                        timestamp = datetime.fromisoformat(data["time"].replace("Z", "+00:00"))
                        timestamps.append(timestamp)
                        response_times.append(data["value"])

                # Обработка метрики активных пользователей (VUs)
                elif metric == "vus":
                    if "time" in data and "value" in data:
                        active_vus_timestamps.append(datetime.fromisoformat(data["time"].replace("Z", "+00:00")))
                        active_vus_values.append(data["value"])

            except json.JSONDecodeError as e:
                print(f"Ошибка при декодировании JSON: {e}")
                continue

except FileNotFoundError:
    print("Файл отчёта *.json не найден.")
    exit(1)

# Проверка наличия данных
if not timestamps or not response_times:
    print("Нет данных для построения графика.")
    exit(1)

# Интерполяция значений Active VUs
active_vus_aligned = []
last_known_value = None  # Для хранения последнего известного значения Active VUs

for timestamp in timestamps:
    # Находим значение Active VUs для текущей временной метки
    if timestamp in active_vus_timestamps:
        index = active_vus_timestamps.index(timestamp)
        last_known_value = active_vus_values[index]
        active_vus_aligned.append(last_known_value)
    else:
        # Если нет значения Active VUs для данной метки времени, используем NaN
        active_vus_aligned.append(last_known_value if last_known_value is not None else np.nan)

# Построение графика
plt.figure(figsize=(10, 6))

# График времени отклика
plt.plot(timestamps, response_times, label='Время отклика (ms)', color='blue')

# График нагрузки (VUs)
plt.twinx()
plt.plot(active_vus_timestamps, active_vus_values, label='Нагрузка(Active VUs)', color='orange', linestyle='', marker='.')

# Настройка осей и заголовков
plt.title('Время отклика vs Нагрузка')
plt.xlabel('Время')
plt.ylabel('Время отклика (ms)')
plt.legend(loc='upper left')

# Отображение графика
plt.show()
