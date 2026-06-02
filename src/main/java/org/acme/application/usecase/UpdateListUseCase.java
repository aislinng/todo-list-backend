package org.acme.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.application.dto.UpdateTaskListDto;
import org.acme.domain.models.TaskList;
import org.acme.domain.repository.TaskListRepository;
import org.acme.domain.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UpdateListUseCase {

    @Inject
    TaskListRepository taskListRepository;

    @Inject
    TaskRepository taskRepository;

    public Optional<TaskList> execute(UUID id, UpdateTaskListDto dto, UUID userId) {
        Optional<TaskList> existing = taskListRepository.findByIdAndUserId(id, userId);
        if (existing.isEmpty()) return Optional.empty();

        TaskList toUpdate = existing.get();
        if (dto.getTitle() != null) toUpdate.setTitle(dto.getTitle());
        if (dto.getDescription() != null) toUpdate.setDescription(dto.getDescription());
        if (dto.getColor() != null) toUpdate.setColor(dto.getColor());
        toUpdate.setUpdatedAt(LocalDateTime.now());

        return taskListRepository.update(toUpdate).map(updated -> {
            updated.setTaskCount(taskRepository.countByListId(updated.getId()));
            updated.setCompletedCount(taskRepository.countCompletedByListId(updated.getId()));
            return updated;
        });
    }
}
