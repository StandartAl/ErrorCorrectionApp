package com.project.errorcorrection.service;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Компонент для вычисления значения битовой маски options при отправке запроса в Yandex Speller API.
 * Если в тексте есть цифры — включить опцию IGNORE_DIGITS (2).
 * Если текст содержит URL — включить опцию IGNORE_URLS (4).
 * Опции FIND_REPEAT_WORDS (8) и IGNORE_CAPITALIZATION (512) всегда выключены.
 */
@Component
public class TextOptionCalculator {

    public static final int IGNORE_DIGITS = 2;

    public static final int IGNORE_URLS = 4;

    private static final Pattern DIGITS_PATTERN = Pattern.compile(".*\\d.*");
    private static final Pattern URL_PATTERN = Pattern.compile("(?i).*(\\b(?:https?|ftp)://\\S+|\\bwww\\.\\S+|\\b[a-z0-9.-]+\\.[a-z]{2,4}\\b).*");

    /**
     * Рассчитывает итоговую битовую маску options на основе содержимого текста.
     *
     * @param text проверяемый текст
     * @return целое число битовой маски options
     */
    public int calculateOptions(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }

        int options = 0;

        if (DIGITS_PATTERN.matcher(text).matches()) {
            options |= IGNORE_DIGITS;
        }

        if (URL_PATTERN.matcher(text).matches()) {
            options |= IGNORE_URLS;
        }

        return options;
    }
}
