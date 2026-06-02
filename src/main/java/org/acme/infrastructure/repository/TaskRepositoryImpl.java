package org.acme.infrastructure.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.acme.domain.models.Task;
import org.acme.domain.repository.TaskRepository;
import org.acme.infrastructure.entities.TaskEntity;
import org.acme.infrastructure.mapper.TaskMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TaskRepositoryImpl implements TaskRepository, PanacheRepositoryBase<TaskEntity, UUID> {

    @Inject
    EntityManager entityManager;

    @Override
    public List<Task> findAllByListId(UUID listId) {
        return find("listId", listId).stream()
                .map(TaskMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Task> findByIdAndListId(UUID id, UUID listId) {
        return find("id = ?1 AND listId = ?2", id, listId)
                .firstResultOptional()
                .map(TaskMapper::toDomain);
    }

    @Override
    @Transactional
    public Task save(Task task) {
        TaskEntity entity = TaskMapper.toEntity(task);
        persist(entity);
        return TaskMapper.toDomain(entity);
    }

    @Override
    @Transactional
    public Optional<Task> update(Task task) {
        return findByIdOptional(task.getId()).map(entity -> {
            entity.setTitle(task.getTitle());
            entity.setDescription(task.getDescription());
            entity.setCompleted(task.isCompleted());
            entity.setPriority(task.getPriority());
            entity.setDueDate(task.getDueDate());
            entity.setUpdatedAt(task.getUpdatedAt());
            return TaskMapper.toDomain(entity);
        });
    }

    @Override
    @Transactional
    public boolean deleteByIdAndListId(UUID id, UUID listId) {
        return delete("id = ?1 AND listId = ?2", id, listId) > 0;
    }

    @Override
    public long countByListId(UUID listId) {
        return count("listId", listId);
    }

    @Override
    public long countCompletedByListId(UUID listId) {
        return count("listId = ?1 AND completed = true", listId);
    }

    @Override
    public List<Task> searchByUserIdAndQuery(UUID userId, String query) {
        String like = "%" + query.toLowerCase() + "%";
        String jpql = "SELECT t FROM TaskEntity t " +
                "WHERE t.listId IN (SELECT l.id FROM TaskListEntity l WHERE l.userId = :userId) " +
                "AND (LOWER(t.title) LIKE :q OR LOWER(t.description) LIKE :q)";
        return entityManager.createQuery(jpql, TaskEntity.class)
                .setParameter("userId", userId)
                .setParameter("q", like)
                .getResultList()
                .stream()
                .map(TaskMapper::toDomain)
                .toList();
    }
}
