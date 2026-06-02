package org.acme.infrastructure.mapper;

import org.acme.domain.models.Task;
import org.acme.infrastructure.entities.TaskEntity;

public class TaskMapper {

    public static Task toDomain(TaskEntity entity) {
        Task model = new Task();
        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        model.setDescription(entity.getDescription());
        model.setCompleted(entity.isCompleted());
        model.setPriority(entity.getPriority());
        model.setDueDate(entity.getDueDate());
        model.setListId(entity.getListId());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        return model;
    }

    public static TaskEntity toEntity(Task model) {
        TaskEntity entity = new TaskEntity();
        entity.setId(model.getId());
        entity.setTitle(model.getTitle());
        entity.setDescription(model.getDescription());
        entity.setCompleted(model.isCompleted());
        entity.setPriority(model.getPriority());
        entity.setDueDate(model.getDueDate());
        entity.setListId(model.getListId());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        return entity;
    }
}
