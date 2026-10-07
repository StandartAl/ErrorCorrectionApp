package com.project.errorcorrection.domain;

/**
 * Перечисление статусов жизненного цикла задачи по корректировке текста:
 * NEW - Задача создана и ожидает обработки шедулером;
 * IN_PROGRESS - Задача находится в процессе отправки и обработки в Yandex Speller API;
 * COMPLETED - Текст успешно проверен и исправлен;
 * FAILED - При обработке задачи произошла ошибка (сеть, API, таймаут и т.д.).
 */
public enum TaskStatus {
    NEW,
    IN_PROGRESS,
    COMPLETED,
    FAILED
}
