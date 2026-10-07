package com.project.errorcorrection.scheduler;

import com.project.errorcorrection.domain.TaskStatus;
import com.project.errorcorrection.entity.TaskEntity;
import com.project.errorcorrection.repository.TaskRepository;
import com.project.errorcorrection.service.TextCorrectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Фоновый планировщик для автоматической асинхронной обработки задач на корректировку текста.
 * Периодически извлекает задачи со статусом NEW из базы данных,
 * вызывает внешний сервис Yandex Speller API и обновляет статус задачи на COMPLETED или FAILED.
 */
@Slf4j
@Component("textCorrectionScheduler")
@RequiredArgsConstructor
public class TextCorrectionScheduler {

    private final TaskRepository taskRepository;
    private final TextCorrectionService textCorrectionService;

    @Value("${app.scheduler.enabled:true}")
    private boolean schedulerEnabled;

    /**
     * Фоновый метод, выполняемый с настроенной периодичностью.
     * Запрашивает порцию новых задач со статусом NEW и отправляет их на обработку.
     */
    @Scheduled(fixedDelayString = "${app.scheduler.interval-ms:3000}")
    public void processPendingTasks() {
        if (!schedulerEnabled) {
            return;
        }

        List<TaskEntity> pendingTasks = taskRepository.findTop10ByStatusOrderByCreatedAtAsc(TaskStatus.NEW);
        if (pendingTasks.isEmpty()) {
            return;
        }

        log.info("Scheduler found {} NEW task(s) to process", pendingTasks.size());

        for (TaskEntity task : pendingTasks) {
            processSingleTask(task);
        }
    }

    /**
     * Обрабатывает одну задачу: переводит статус в IN_PROGRESS, выполняет запрос в API Яндекса,
     * а затем сохраняет результат со статусом COMPLETED или FAILED.
     *
     * @param task обрабатываемая задача
     */
    @Transactional
    public void processSingleTask(TaskEntity task) {
        log.info("Processing task id={}", task.getId());
        task.setStatus(TaskStatus.IN_PROGRESS);
        taskRepository.saveAndFlush(task);

        try {
            String corrected = textCorrectionService.correctText(task.getOriginalText(), task.getLanguage());
            task.setCorrectedText(corrected);
            task.setStatus(TaskStatus.COMPLETED);
            log.info("Task id={} successfully completed", task.getId());
        } catch (Exception e) {
            log.error("Failed to correct text for task id={}: {}", task.getId(), e.getMessage());
            task.setStatus(TaskStatus.FAILED);
            task.setErrorMessage(e.getMessage());
        }

        taskRepository.save(task);
    }
}
