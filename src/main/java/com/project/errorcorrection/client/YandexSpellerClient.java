package com.project.errorcorrection.client;

import com.project.errorcorrection.dto.YandexSpellError;
import com.project.errorcorrection.exception.YandexSpellerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * HTTP-клиент для взаимодействия с Yandex Speller API методом POST checkTexts.
 */
@Slf4j
@Component
public class YandexSpellerClient {

    private final RestClient restClient;

    public YandexSpellerClient(RestClient.Builder restClientBuilder,
                               @Value("${app.yandex.speller-url:https://speller.yandex.net/services/spellservice.json/checkTexts}") String url) {
        this.restClient = restClientBuilder.baseUrl(url).build();
    }

    /**
     * Отправляет массив фрагментов текста в Yandex Speller API для проверки орфографии.
     *
     * @param texts   список текстовых фрагментов
     * @param lang    код языка ("ru" или "en")
     * @param options битовая маска параметров проверки орфографии
     * @return двумерный список найденных ошибок орфографии для каждого чанка
     */
    public List<List<YandexSpellError>> checkTexts(List<String> texts, String lang, int options) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            for (String text : texts) {
                formData.add("text", text);
            }
            formData.add("lang", lang.toLowerCase());
            formData.add("options", String.valueOf(options));

            log.info("Sending request to Yandex Speller API with {} text chunks, lang={}, options={}", texts.size(), lang, options);

            List<List<YandexSpellError>> response = restClient.post()
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<List<YandexSpellError>>>() {});

            if (response == null) {
                throw new YandexSpellerException("Received null response from Yandex Speller API");
            }

            return response;
        } catch (Exception e) {
            log.error("Failed to execute Yandex Speller API checkTexts request", e);
            throw new YandexSpellerException("Yandex Speller API error: " + e.getMessage(), e);
        }
    }
}
