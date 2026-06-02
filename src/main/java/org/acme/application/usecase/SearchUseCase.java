package org.acme.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.application.dto.SearchResultDto;
import org.acme.domain.models.Task;
import org.acme.domain.models.TaskList;
import org.acme.domain.repository.TaskListRepository;
import org.acme.domain.repository.TaskRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class SearchUseCase {

    @Inject
    TaskListRepository taskListRepository;

    @Inject
    TaskRepository taskRepository;

    public SearchResultDto execute(String query, UUID userId) {
        List<TaskList> lists = taskListRepository.searchByUserIdAndQuery(userId, query);
        for (TaskList list : lists) {
            list.setTaskCount(taskRepository.countByListId(list.getId()));
            list.setCompletedCount(taskRepository.countCompletedByListId(list.getId()));
        }
        List<Task> tasks = taskRepository.searchByUserIdAndQuery(userId, query);
        return new SearchResultDto(lists, tasks);
    }
}
