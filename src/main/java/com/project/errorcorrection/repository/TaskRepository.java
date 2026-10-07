package com.project.errorcorrection.repository;

import com.project.errorcorrection.domain.TaskStatus;
import com.project.errorcorrection.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий Spring Data JPA для работы с сущностями задач в БД.
 */
@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

    List<TaskEntity> findTop10ByStatusOrderByCreatedAtAsc(TaskStatus status);
}
