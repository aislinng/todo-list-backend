package org.acme.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.application.dto.CreateTaskListDto;
import org.acme.domain.models.TaskList;
import org.acme.domain.repository.TaskListRepository;
import org.acme.infrastructure.security.AuthContext;

import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class CreateListUseCase {

    @Inject
    TaskListRepository taskListRepository;

    @Inject
    AuthContext authContext;

    public TaskList execute(CreateTaskListDto dto) {
        TaskList taskList = new TaskList();
        taskList.setId(UUID.randomUUID());
        taskList.setTitle(dto.getTitle());
        taskList.setDescription(dto.getDescription());
        taskList.setColor(dto.getColor());
        taskList.setUserId(authContext.getUser().getId());
        taskList.setCreatedAt(LocalDateTime.now());
        taskList.setUpdatedAt(LocalDateTime.now());
        taskList.setTaskCount(0);
        taskList.setCompletedCount(0);
        return taskListRepository.save(taskList);
    }
}
