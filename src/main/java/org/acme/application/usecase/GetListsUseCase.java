package org.acme.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.domain.models.TaskList;
import org.acme.domain.repository.TaskListRepository;
import org.acme.domain.repository.TaskRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class GetListsUseCase {

    @Inject
    TaskListRepository taskListRepository;

    @Inject
    TaskRepository taskRepository;

    public List<TaskList> execute(UUID userId) {
        List<TaskList> lists = taskListRepository.findAllByUserId(userId);
        for (TaskList list : lists) {
            list.setTaskCount(taskRepository.countByListId(list.getId()));
            list.setCompletedCount(taskRepository.countCompletedByListId(list.getId()));
        }
        return lists;
    }
}
