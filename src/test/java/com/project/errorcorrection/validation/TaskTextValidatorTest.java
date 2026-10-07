package com.project.errorcorrection.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты валидатора входного текста {@link TaskTextValidator}.
 * Проверяют правила валидации длины и наличия буквенных символов.
 */
class TaskTextValidatorTest {

    private TaskTextValidator validator;

    @BeforeEach
    void setUp() {
        validator = new TaskTextValidator();
    }

    @Test
    @DisplayName("Должен принимать валидный текст с буквами и длиной >= 3")
    void isValid_ValidText_ReturnsTrue() {
        assertTrue(validator.isValid("Привет мир", null));
        assertTrue(validator.isValid("Hello 123", null));
        assertTrue(validator.isValid("abc", null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "a", "  ab ", "   "})
    @DisplayName("Должен отклонять текст короче 3 символов")
    void isValid_ShortText_ReturnsFalse(String input) {
        assertFalse(validator.isValid(input, null));
    }

    @Test
    @DisplayName("Должен отклонять null текст")
    void isValid_NullText_ReturnsFalse() {
        assertFalse(validator.isValid(null, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456", "!!!???", "123 !@#$%^&*()", "   45678  "})
    @DisplayName("Должен отклонять текст, состоящий только из цифр и спецсимволов")
    void isValid_OnlyDigitsAndSpecialChars_ReturnsFalse(String input) {
        assertFalse(validator.isValid(input, null));
    }
}
