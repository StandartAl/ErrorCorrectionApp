package com.project.errorcorrection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Главный класс запуска приложения Error Correction.
 * Веб-приложение предназначено для автоматической асинхронной корректировки текста
 * с использованием Yandex Speller API и работы фонового обработчика.
 */
@SpringBootApplication
@EnableScheduling
public class ErrorCorrectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ErrorCorrectionApplication.class, args);
    }
}
