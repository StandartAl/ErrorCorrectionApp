package com.project.errorcorrection.exception;

import java.util.UUID;

/**
 * Исключение, выбрасываемое при отсутствии задачи с указанным UUID в базе данных.
 */
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(UUID id) {
        super("Task with id: " + id + " not found");
    }
}
