package org.acme.domain.repository;

import org.acme.domain.models.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {
    List<Task> findAllByListId(UUID listId);
    Optional<Task> findByIdAndListId(UUID id, UUID listId);
    Task save(Task task);
    Optional<Task> update(Task task);
    boolean deleteByIdAndListId(UUID id, UUID listId);
    long countByListId(UUID listId);
    long countCompletedByListId(UUID listId);
    List<Task> searchByUserIdAndQuery(UUID userId, String query);
}
