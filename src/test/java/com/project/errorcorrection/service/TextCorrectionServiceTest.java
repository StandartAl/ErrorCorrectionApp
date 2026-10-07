package com.project.errorcorrection.service;

import com.project.errorcorrection.client.YandexSpellerClient;
import com.project.errorcorrection.dto.YandexSpellError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Юнит-тесты сервиса корректировки текста {@link TextCorrectionService}.
 * Проверяют точное применение замен опечаток в исходной строке.
 */
class TextCorrectionServiceTest {

    @Mock
    private YandexSpellerClient spellerClient;

    private TextOptionCalculator optionCalculator;
    private TextChunker textChunker;
    private TextCorrectionService correctionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        optionCalculator = new TextOptionCalculator();
        textChunker = new TextChunker();
        correctionService = new TextCorrectionService(spellerClient, optionCalculator, textChunker);
    }

    @Test
    @DisplayName("Должен корректно заменять опечатки на первые предложенные варианты")
    void applyCorrections_WithTypos_ReturnsCorrectedString() {
        String original = "Превед медвед";
        // "Превед" на pos=0, len=6 -> вариант "Привет"
        // "медвед" на pos=7, len=6 -> вариант "медведь"
        YandexSpellError err1 = new YandexSpellError(0, 6, 1, "Превед", List.of("Привет"));
        YandexSpellError err2 = new YandexSpellError(7, 6, 1, "медвед", List.of("медведь"));

        String result = correctionService.applyCorrections(original, List.of(err1, err2));
        assertEquals("Привет медведь", result);
    }
}
