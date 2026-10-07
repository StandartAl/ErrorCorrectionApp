package com.project.errorcorrection.exception;

/**
 * Исключение, выбрасываемое при ошибках обращения к Yandex Speller API.
 */
public class YandexSpellerException extends RuntimeException {
    public YandexSpellerException(String message) {
        super(message);
    }

    public YandexSpellerException(String message, Throwable cause) {
        super(message, cause);
    }
}
