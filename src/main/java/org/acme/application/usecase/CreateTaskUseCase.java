package org.acme.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.application.dto.CreateTaskDto;
import org.acme.domain.models.Task;
import org.acme.domain.repository.TaskListRepository;
import org.acme.domain.repository.TaskRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CreateTaskUseCase {

    @Inject
    TaskListRepository taskListRepository;

    @Inject
    TaskRepository taskRepository;

    public Optional<Task> execute(UUID listId, CreateTaskDto dto, UUID userId) {
        if (taskListRepository.findByIdAndUserId(listId, userId).isEmpty()) {
            return Optional.empty();
        }

        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCompleted(false);
        task.setPriority(dto.getPriority() != null ? dto.getPriority() : "medium");
        task.setDueDate(dto.getDueDate() != null ? LocalDate.parse(dto.getDueDate()) : null);
        task.setListId(listId);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        return Optional.of(taskRepository.save(task));
    }
}
