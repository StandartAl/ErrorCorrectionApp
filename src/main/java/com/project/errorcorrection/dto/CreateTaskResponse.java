package com.project.errorcorrection.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO ответа при успешном создании задачи (возвращает UUID созданной задачи).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskResponse {

    private UUID id;
}
