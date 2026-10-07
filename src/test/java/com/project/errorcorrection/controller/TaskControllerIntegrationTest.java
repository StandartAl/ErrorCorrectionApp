package com.project.errorcorrection.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Интеграционные тесты REST API контроллера {@link TaskController}.
 * Проверяют создание задач, получение статуса, а также форматирование ошибок 400 и 404.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /tasks должен создавать задачу и возвращать 201 Created с ID")
    void createTask_ValidPayload_Returns201AndId() throws Exception {
        String payload = """
                {
                    "text": "Текст с опечяткой для теста",
                    "language": "RU"
                }
                """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    @DisplayName("POST /tasks с невалидным текстом должен возвращать 400 Bad Request с единым DTO ошибки")
    void createTask_InvalidText_Returns400() throws Exception {
        String payload = """
                {
                    "text": "123",
                    "language": "RU"
                }
                """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", is(40001)))
                .andExpect(jsonPath("$.errorMessage", notNullValue()))
                .andExpect(jsonPath("$.path", is("/tasks")));
    }

    @Test
    @DisplayName("GET /tasks/{id} для несуществующего ID должен возвращать 404 Not Found с кодом 40401")
    void getTask_NotFound_Returns404() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(get("/tasks/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is(40401)))
                .andExpect(jsonPath("$.errorMessage", containsString(nonExistentId.toString())))
                .andExpect(jsonPath("$.path", is("/tasks/" + nonExistentId)));
    }
}
