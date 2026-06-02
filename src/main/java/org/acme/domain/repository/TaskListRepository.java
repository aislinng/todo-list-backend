package org.acme.domain.repository;

import org.acme.domain.models.TaskList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskListRepository {
    List<TaskList> findAllByUserId(UUID userId);
    Optional<TaskList> findByIdAndUserId(UUID id, UUID userId);
    TaskList save(TaskList taskList);
    Optional<TaskList> update(TaskList taskList);
    boolean deleteByIdAndUserId(UUID id, UUID userId);
    List<TaskList> searchByUserIdAndQuery(UUID userId, String query);
}
