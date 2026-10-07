package com.project.errorcorrection.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Пользовательская аннотация валидации входного текста.
 * Проверяет, что текст содержит не менее 3 символов и включает хотя бы одну букву
 * запрещает тексты, состоящие только из цифр и спецсимволов.
 */
@Documented
@Constraint(validatedBy = TaskTextValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTaskText {
    String message() default "Text must contain at least 3 characters and cannot consist only of digits and special characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
