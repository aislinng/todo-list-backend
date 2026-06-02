package org.acme.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.domain.repository.TaskListRepository;

import java.util.UUID;

@ApplicationScoped
public class DeleteListUseCase {

    @Inject
    TaskListRepository taskListRepository;

    public boolean execute(UUID id, UUID userId) {
        return taskListRepository.deleteByIdAndUserId(id, userId);
    }
}
