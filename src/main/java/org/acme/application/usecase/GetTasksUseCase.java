package org.acme.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.domain.models.Task;
import org.acme.domain.repository.TaskListRepository;
import org.acme.domain.repository.TaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class GetTasksUseCase {

    @Inject
    TaskListRepository taskListRepository;

    @Inject
    TaskRepository taskRepository;

    public Optional<List<Task>> execute(UUID listId, UUID userId) {
        if (taskListRepository.findByIdAndUserId(listId, userId).isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(taskRepository.findAllByListId(listId));
    }
}
