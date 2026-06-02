package org.acme.application.dto;

import org.acme.domain.models.Task;
import org.acme.domain.models.TaskList;

import java.util.List;

public class SearchResultDto {

    private List<TaskList> lists;
    private List<Task> tasks;

    public SearchResultDto(List<TaskList> lists, List<Task> tasks) {
        this.lists = lists;
        this.tasks = tasks;
    }

    public List<TaskList> getLists() { return lists; }
    public List<Task> getTasks() { return tasks; }
}
