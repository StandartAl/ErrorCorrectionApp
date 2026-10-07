package com.project.errorcorrection.service;

import com.project.errorcorrection.client.YandexSpellerClient;
import com.project.errorcorrection.domain.Language;
import com.project.errorcorrection.dto.YandexSpellError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Сервис выполнения орфографической проверки и корректировки текста.
 * Производит вычисление маски options, разбиение текста на чанки,
 * вызовы Yandex Speller API и подстановку предложенных исправлений.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TextCorrectionService {

    private final YandexSpellerClient spellerClient;
    private final TextOptionCalculator optionCalculator;
    private final TextChunker textChunker;

    /**
     * Корректирует текст с использованием Yandex Speller API.
     *
     * @param originalText исходный текст
     * @param language     язык текста (RU, EN)
     * @return скорректированный текст с исправленными опечатками
     */
    public String correctText(String originalText, Language language) {
        if (originalText == null || originalText.isEmpty()) {
            return originalText;
        }

        int options = optionCalculator.calculateOptions(originalText);
        List<String> chunks = textChunker.splitText(originalText);

        log.info("Correcting text length={} split into {} chunk(s), language={}, calculated options={}",
                originalText.length(), chunks.size(), language, options);

        List<List<YandexSpellError>> errorsPerChunk = spellerClient.checkTexts(chunks, language.name(), options);

        StringBuilder correctedResult = new StringBuilder();
        for (int i = 0; i < chunks.size(); i++) {
            String chunk = chunks.get(i);
            List<YandexSpellError> chunkErrors = (i < errorsPerChunk.size()) ? errorsPerChunk.get(i) : List.of();
            String correctedChunk = applyCorrections(chunk, chunkErrors);
            correctedResult.append(correctedChunk);
        }

        return correctedResult.toString();
    }

    /**
     * Применяет список орфографических замен к фрагменту текста.
     * Замены выполняются в обратном порядке,
     * чтобы сохранять корректность индексации при изменении длины слов.
     *
     * @param text   исходный чанк текста
     * @param errors список опечаток для данного чанка
     * @return чанк с замененными опечатками
     */
    public String applyCorrections(String text, List<YandexSpellError> errors) {
        if (text == null || errors == null || errors.isEmpty()) {
            return text;
        }

        List<YandexSpellError> sortedErrors = errors.stream()
                .sorted(Comparator.comparingInt(YandexSpellError::getPos).reversed())
                .toList();

        StringBuilder sb = new StringBuilder(text);
        for (YandexSpellError error : sortedErrors) {
            if (error.getS() != null && !error.getS().isEmpty()) {
                String replacement = error.getS().get(0);
                int start = error.getPos();
                int end = start + error.getLen();

                if (start >= 0 && end <= sb.length()) {
                    sb.replace(start, end, replacement);
                } else {
                    log.warn("Spell error pos [{}-{}] out of bounds for chunk length {}", start, end, sb.length());
                }
            }
        }

        return sb.toString();
    }
}
