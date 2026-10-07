package com.project.errorcorrection.service;

import com.project.errorcorrection.domain.TaskStatus;
import com.project.errorcorrection.dto.CreateTaskRequest;
import com.project.errorcorrection.dto.CreateTaskResponse;
import com.project.errorcorrection.dto.TaskStatusResponse;
import com.project.errorcorrection.entity.TaskEntity;
import com.project.errorcorrection.exception.TaskNotFoundException;
import com.project.errorcorrection.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Сервис управления жизненным циклом задач на корректировку текста (создание, получение по ID).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    /**
     * Сохраняет новую задачу в базу данных со статусом NEW и возвращает её UUID.
     *
     * @param request тело запроса с текстом и языком
     * @return ответ со сгенерированным UUID задачи
     */
    @Transactional
    public CreateTaskResponse createTask(CreateTaskRequest request) {
        TaskEntity entity = TaskEntity.builder()
                .originalText(request.getText())
                .language(request.getLanguage())
                .status(TaskStatus.NEW)
                .build();

        TaskEntity saved = taskRepository.save(entity);
        log.info("Created new text correction task id={}, language={}", saved.getId(), saved.getLanguage());
        return new CreateTaskResponse(saved.getId());
    }

    /**
     * Находит задачу по UUID и формирует соответствующий DTO статуса ответа.
     *
     * @param id уникальный идентификатор задачи
     * @return DTO ответа со статусом и скорректированным текстом (при наличии)
     * @throws TaskNotFoundException если задача не найдена
     */
    @Transactional(readOnly = true)
    public TaskStatusResponse getTask(UUID id) {
        TaskEntity entity = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        TaskStatusResponse.TaskStatusResponseBuilder builder = TaskStatusResponse.builder()
                .id(entity.getId())
                .status(entity.getStatus());

        if (entity.getStatus() == TaskStatus.COMPLETED) {
            builder.correctedText(entity.getCorrectedText());
        } else if (entity.getStatus() == TaskStatus.FAILED) {
            builder.errorMessage(entity.getErrorMessage());
        }

        return builder.build();
    }
}
