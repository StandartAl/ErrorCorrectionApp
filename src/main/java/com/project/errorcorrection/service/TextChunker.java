package com.project.errorcorrection.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Компонент для разделения длинного текста (> 10 000 символов) на фрагменты
 * с соблюдением ограничений Yandex Speller API без разрыва слов и предложений.
 */
@Component
public class TextChunker {

    /** Максимально допустимая длина одного запроса по документации Yandex Speller API */
    public static final int DEFAULT_MAX_CHUNK_LENGTH = 10000;

    public List<String> splitText(String text) {
        return splitText(text, DEFAULT_MAX_CHUNK_LENGTH);
    }

    /**
     * Разделяет исходный текст на чанки длиной не более maxChunkLength.
     *
     * @param text           исходный текст
     * @param maxChunkLength максимальная длина одного фрагмента
     * @return список фрагментов текста
     */
    public List<String> splitText(String text, int maxChunkLength) {
        List<String> chunks = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return chunks;
        }

        int length = text.length();
        int start = 0;

        while (start < length) {
            if (length - start <= maxChunkLength) {
                chunks.add(text.substring(start));
                break;
            }

            int candidateEnd = start + maxChunkLength;
            int splitPoint = findSplitPoint(text, start, candidateEnd);

            chunks.add(text.substring(start, splitPoint));
            start = splitPoint;
        }

        return chunks;
    }

    /**
     * Находит подходящую позицию для разделения строки назад от границы,
     * отдавая приоритет пробельным символам, концам предложений и переходам строк.
     */
    private int findSplitPoint(String text, int start, int end) {
        for (int i = end - 1; i > start; i--) {
            char c = text.charAt(i);
            if (Character.isWhitespace(c) || c == '.' || c == '!' || c == '?' || c == '\n') {
                return i + 1;
            }
        }
        return end;
    }
}
