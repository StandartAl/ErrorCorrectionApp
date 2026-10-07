package com.project.errorcorrection.dto;

import com.project.errorcorrection.domain.Language;
import com.project.errorcorrection.validation.ValidTaskText;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO запроса на создание новой задачи по корректировке текста.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {

    @ValidTaskText
    private String text;

    @NotNull(message = "Language is required (RU or EN)")
    private Language language;
}
