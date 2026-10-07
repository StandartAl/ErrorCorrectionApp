package com.project.errorcorrection.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO структуры ошибки опечатки, возвращаемой Yandex Speller API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class YandexSpellError {

    /** 0-based позиция опечатки в строке текста */
    private int pos;

    /** Длина слова с опечаткой */
    private int len;

    /** Код типа орфографической ошибки Yandex Speller */
    private int code;

    /** Исходное слово с опечаткой */
    private String word;

    /** Список вариантов исправления */
    private List<String> s;
}
