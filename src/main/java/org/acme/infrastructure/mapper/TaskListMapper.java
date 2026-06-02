package org.acme.infrastructure.mapper;

import org.acme.domain.models.TaskList;
import org.acme.infrastructure.entities.TaskListEntity;

public class TaskListMapper {

    public static TaskList toDomain(TaskListEntity entity) {
        TaskList model = new TaskList();
        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        model.setDescription(entity.getDescription());
        model.setColor(entity.getColor());
        model.setUserId(entity.getUserId());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        return model;
    }

    public static TaskListEntity toEntity(TaskList model) {
        TaskListEntity entity = new TaskListEntity();
        entity.setId(model.getId());
        entity.setTitle(model.getTitle());
        entity.setDescription(model.getDescription());
        entity.setColor(model.getColor());
        entity.setUserId(model.getUserId());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        return entity;
    }
}
