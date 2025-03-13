18. Фермерский рынок
Задание: Реализовать учет продаж продуктов.
Продукт: ID, название, категория (овощи/фрукты), цена за кг.
Покупатель: ФИО, телефон, скидочная карта.
Продажа: связь продукта и покупателя, дата, вес, стоимость.
Дополнительно: Расчет среднего веса закупки по каждому наименованию товара за последний месяц


LAB1
Использовать в качестве шаблона код из ветки feature/spring-boot-test https://bitbucket.org/zil-courses/hl-module1/src/main/ 
Переделать проект в IntelliJ IDEA под своего бизнес-постановку https://docs.google.com/document/d/1J4uhP4-q98WRGWdvPSACE9XhxGEI68ie/edit  (в качестве реализации компонентов репозиториев использовать статические поля-коллекции Java)
Закоммитить в свой git и показать преподавателю


LAB2
Развернуть PostgreSQL (в docker compose (https://proghunter.ru/articles/running-postgresql-and-pgadmin-in-docker ) или локально)
Переделать компоненты-репозитории на работу с таблицами БД
Наполнить БД данными для тестирования
Закоммитить изменения в git и показать преподавателю

LAB3
Написать Dockerfile для контейнеризации приложения
Написать генератор create/insert для initdb (https://habr.com/ru/articles/578744/ ), например на python, запускать генератор перед запуском DB
Развернуть приложение и DB на одной из виртуальных машин в docker compose
Продемонстрировать развернутый стенд преподавателю

LAB4
Развернуть K6 (https://k6.io/ ) на соседней виртуальной машине
Подготовить простейший (получение данных одной из таблиц) нагрузочный тест
Построить график зависимости времени отклика от нагрузки
