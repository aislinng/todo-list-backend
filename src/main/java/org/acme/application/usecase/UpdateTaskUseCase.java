package org.acme.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.application.dto.UpdateTaskDto;
import org.acme.domain.models.Task;
import org.acme.domain.repository.TaskListRepository;
import org.acme.domain.repository.TaskRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UpdateTaskUseCase {

    @Inject
    TaskListRepository taskListRepository;

    @Inject
    TaskRepository taskRepository;

    public Optional<Task> execute(UUID listId, UUID taskId, UpdateTaskDto dto, UUID userId) {
        if (taskListRepository.findByIdAndUserId(listId, userId).isEmpty()) {
            return Optional.empty();
        }

        Optional<Task> existing = taskRepository.findByIdAndListId(taskId, listId);
        if (existing.isEmpty()) return Optional.empty();

        Task toUpdate = existing.get();
        if (dto.getTitle() != null) toUpdate.setTitle(dto.getTitle());
        if (dto.getDescription() != null) toUpdate.setDescription(dto.getDescription());
        if (dto.getPriority() != null) toUpdate.setPriority(dto.getPriority());
        if (dto.getDueDate() != null) toUpdate.setDueDate(LocalDate.parse(dto.getDueDate()));
        toUpdate.setUpdatedAt(LocalDateTime.now());

        return taskRepository.update(toUpdate);
    }
}
