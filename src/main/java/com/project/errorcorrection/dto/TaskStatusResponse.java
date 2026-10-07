package com.project.errorcorrection.dto;

import com.project.errorcorrection.domain.TaskStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO ответа при запросе статуса и результатов задачи по ID.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskStatusResponse {

    private UUID id;

    private TaskStatus status;

    private String correctedText;

    private String errorMessage;
}
