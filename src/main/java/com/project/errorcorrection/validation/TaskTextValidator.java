package com.project.errorcorrection.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Валидатор для аннотации ValidTaskText.
 * Реализует проверку минимальной длины текста (>= 3 символов) и наличия хотя бы одной буквы.
 */
public class TaskTextValidator implements ConstraintValidator<ValidTaskText, String> {

    private static final Pattern HAS_LETTER_PATTERN = Pattern.compile(".*\\p{L}.*");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        String trimmed = value.trim();
        if (trimmed.length() < 3) {
            return false;
        }

        return HAS_LETTER_PATTERN.matcher(trimmed).matches();
    }
}
