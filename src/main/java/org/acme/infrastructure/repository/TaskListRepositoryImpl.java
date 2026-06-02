package org.acme.infrastructure.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.acme.domain.models.TaskList;
import org.acme.domain.repository.TaskListRepository;
import org.acme.infrastructure.entities.TaskListEntity;
import org.acme.infrastructure.mapper.TaskListMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TaskListRepositoryImpl implements TaskListRepository, PanacheRepositoryBase<TaskListEntity, UUID> {

    @Override
    public List<TaskList> findAllByUserId(UUID userId) {
        return find("userId", userId).stream()
                .map(TaskListMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<TaskList> findByIdAndUserId(UUID id, UUID userId) {
        return find("id = ?1 AND userId = ?2", id, userId)
                .firstResultOptional()
                .map(TaskListMapper::toDomain);
    }

    @Override
    @Transactional
    public TaskList save(TaskList taskList) {
        TaskListEntity entity = TaskListMapper.toEntity(taskList);
        persist(entity);
        return TaskListMapper.toDomain(entity);
    }

    @Override
    @Transactional
    public Optional<TaskList> update(TaskList taskList) {
        return findByIdOptional(taskList.getId()).map(entity -> {
            entity.setTitle(taskList.getTitle());
            entity.setDescription(taskList.getDescription());
            entity.setColor(taskList.getColor());
            entity.setUpdatedAt(taskList.getUpdatedAt());
            return TaskListMapper.toDomain(entity);
        });
    }

    @Override
    @Transactional
    public boolean deleteByIdAndUserId(UUID id, UUID userId) {
        return delete("id = ?1 AND userId = ?2", id, userId) > 0;
    }

    @Override
    public List<TaskList> searchByUserIdAndQuery(UUID userId, String query) {
        String like = "%" + query.toLowerCase() + "%";
        return find("userId = ?1 AND (LOWER(title) LIKE ?2 OR LOWER(description) LIKE ?2)", userId, like)
                .stream()
                .map(TaskListMapper::toDomain)
                .toList();
    }
}
