package com.project.errorcorrection.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Юнит-тесты компонента {@link TextOptionCalculator}.
 * Проверяют правила формирования битовой маски options (2 для цифр, 4 для URL, 6 для обоих).
 */
class TextOptionCalculatorTest {

    private TextOptionCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new TextOptionCalculator();
    }

    @Test
    @DisplayName("Должен возвращать 0 для обычного текста без цифр и URL")
    void calculateOptions_PlainText_ReturnsZero() {
        int options = calculator.calculateOptions("Обычный простой текст без цифр");
        assertEquals(0, options);
    }

    @Test
    @DisplayName("Должен включать флаг IGNORE_DIGITS (2), если текст содержит цифры")
    void calculateOptions_WithDigits_ReturnsIgnoreDigitsFlag() {
        int options = calculator.calculateOptions("Текст с числом 2026 внутри");
        assertEquals(TextOptionCalculator.IGNORE_DIGITS, options);
    }

    @Test
    @DisplayName("Должен включать флаг IGNORE_URLS (4), если текст содержит URL")
    void calculateOptions_WithUrl_ReturnsIgnoreUrlsFlag() {
        int options = calculator.calculateOptions("Посети сайт https://yandex.ru для информации");
        assertEquals(TextOptionCalculator.IGNORE_URLS, options);
    }

    @Test
    @DisplayName("Должен комбинировать IGNORE_DIGITS (2) и IGNORE_URLS (4) в значение 6, если текст содержит и цифры, и URL")
    void calculateOptions_WithDigitsAndUrl_ReturnsCombinedFlags() {
        int options = calculator.calculateOptions("Пример 123 на http://example.com/test");
        assertEquals(TextOptionCalculator.IGNORE_DIGITS | TextOptionCalculator.IGNORE_URLS, options);
        assertEquals(6, options);
    }
}
