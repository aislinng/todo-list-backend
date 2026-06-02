package org.acme.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.application.dto.ToggleTaskDto;
import org.acme.domain.models.Task;
import org.acme.domain.repository.TaskListRepository;
import org.acme.domain.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ToggleTaskUseCase {

    @Inject
    TaskListRepository taskListRepository;

    @Inject
    TaskRepository taskRepository;

    public Optional<Task> execute(UUID listId, UUID taskId, ToggleTaskDto dto, UUID userId) {
        if (taskListRepository.findByIdAndUserId(listId, userId).isEmpty()) {
            return Optional.empty();
        }

        Optional<Task> existing = taskRepository.findByIdAndListId(taskId, listId);
        if (existing.isEmpty()) return Optional.empty();

        Task task = existing.get();
        task.setCompleted(dto.isCompleted());
        task.setUpdatedAt(LocalDateTime.now());

        return taskRepository.update(task);
    }
}
