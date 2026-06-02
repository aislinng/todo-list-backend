package org.acme.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.domain.repository.TaskListRepository;
import org.acme.domain.repository.TaskRepository;

import java.util.UUID;

@ApplicationScoped
public class DeleteTaskUseCase {

    @Inject
    TaskListRepository taskListRepository;

    @Inject
    TaskRepository taskRepository;

    public boolean execute(UUID listId, UUID taskId, UUID userId) {
        if (taskListRepository.findByIdAndUserId(listId, userId).isEmpty()) {
            return false;
        }
        return taskRepository.deleteByIdAndListId(taskId, listId);
    }
}
