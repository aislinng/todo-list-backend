package org.acme.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.domain.models.TaskList;
import org.acme.domain.repository.TaskListRepository;
import org.acme.domain.repository.TaskRepository;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class GetListByIdUseCase {

    @Inject
    TaskListRepository taskListRepository;

    @Inject
    TaskRepository taskRepository;

    public Optional<TaskList> execute(UUID id, UUID userId) {
        return taskListRepository.findByIdAndUserId(id, userId).map(list -> {
            list.setTaskCount(taskRepository.countByListId(list.getId()));
            list.setCompletedCount(taskRepository.countCompletedByListId(list.getId()));
            return list;
        });
    }
}
