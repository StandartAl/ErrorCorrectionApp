package com.project.errorcorrection.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты сервиса разбиения текста {@link TextChunker}.
 * Проверяют корректность разделения длинных текстов на фрагменты без превышения лимитов.
 */
class TextChunkerTest {

    private TextChunker chunker;

    @BeforeEach
    void setUp() {
        chunker = new TextChunker();
    }

    @Test
    @DisplayName("Должен возвращать один чанк, если длина текста не превышает лимит")
    void splitText_ShortText_ReturnsSingleChunk() {
        String input = "Короткий текст для проверки";
        List<String> chunks = chunker.splitText(input, 100);
        assertEquals(1, chunks.size());
        assertEquals(input, chunks.get(0));
    }

    @Test
    @DisplayName("Должен разбивать длинный текст по границам слов на фрагменты в рамках заданного лимита")
    void splitText_LongText_SplitsCleanly() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            sb.append("Слово").append(i).append(" ");
        }
        String text = sb.toString().trim();

        List<String> chunks = chunker.splitText(text, 50);

        assertTrue(chunks.size() > 1);
        for (String chunk : chunks) {
            assertTrue(chunk.length() <= 50, "Chunk length " + chunk.length() + " exceeds limit 50");
        }
        assertEquals(text, String.join("", chunks));
    }
}
