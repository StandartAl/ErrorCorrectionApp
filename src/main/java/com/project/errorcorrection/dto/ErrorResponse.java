package com.project.errorcorrection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Единый DTO структуры ответа при возникновении ошибок обработки REST-запросов.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String errorMessage;

    private Integer errorCode;

    private String timestamp;

    private String path;
}
