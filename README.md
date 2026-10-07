# Error Correction Service (Error Free Text)

Веб-сервис автоматической асинхронной корректировки текста с использованием **Yandex Speller API**. 

Приложение принимает текст от пользователя через REST API, сохраняет задачу в PostgreSQL со статусом `NEW` и моментально возвращает `UUID`. Фоновый планировщик обрабатывает новые задачи, разделяет длинные тексты (> 10 000 символов), рассчитывает битовую маску параметров проверки, отправляет запросы в Яндекс и сохраняет исправленный результат.


## Запуск


Запустите команду в корне проекта:

```bash
docker compose up -d --build
```

Сервис будет доступен по адресу: `http://localhost:8080`

Остановить контейнеры:
```bash
docker compose down
```

---

## REST API Эндпоинты

**`POST /tasks`**

#### Request Body:
```json
{
  "text": "Текст с опечяткой и ошипкой 123 http://example.com",
  "language": "RU"
}
```

#### Response (201 Created):
```json
{
  "id": "b97cfb6e-96d9-4103-99c5-1b209ce918d4"
}
```

---

**`GET /tasks/{id}`**

#### Response (200 OK — В процессе):
```json
{
  "id": "b97cfb6e-96d9-4103-99c5-1b209ce918d4",
  "status": "IN_PROGRESS"
}
```

#### Response (200 OK — Завершена успешно):
```json
{
  "id": "b97cfb6e-96d9-4103-99c5-1b209ce918d4",
  "status": "COMPLETED",
  "correctedText": "Текст с опечаткой и ошибкой 123 http://example.com"
}
```

#### Response (200 OK — Завершена с ошибкой):
```json
{
  "id": "b97cfb6e-96d9-4103-99c5-1b209ce918d4",
  "status": "FAILED",
  "errorMessage": "Yandex Speller API request timeout"
}
```

#### Response (404 Not Found — Задача не найдена):
```json
{
  "errorMessage": "Task with id: 00000000-0000-0000-0000-000000000000 not found",
  "errorCode": 40401,
  "timestamp": "2026-10-07T10:06:21.680",
  "path": "/tasks/00000000-0000-0000-0000-000000000000"
}
```

#### Response (400 Bad Request — Ошибка валидации):
```json
{
  "errorMessage": "text: Text must contain at least 3 characters and cannot consist only of digits and special characters",
  "errorCode": 40001,
  "timestamp": "2026-10-07T10:06:21.669",
  "path": "/tasks"
}
```