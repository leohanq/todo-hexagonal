package com.detalles.todo_hexagonal.dataprovider.mapper;

import com.detalles.todo_hexagonal.domain.model.Task;
import com.detalles.todo_hexagonal.dataprovider.entity.TaskJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskPersistenceMapper {

    public TaskJpaEntity toJpaEntity(Task task) {
        return TaskJpaEntity.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .completedAt(task.getCompletedAt())
                .build();
    }

    public Task toDomainModel(TaskJpaEntity taskJpaEntity) {
        return com.detalles.todo_hexagonal.domain.model.Task.builder()
                .id(taskJpaEntity.getId())
                .title(taskJpaEntity.getTitle())
                .description(taskJpaEntity.getDescription())
                .status(taskJpaEntity.getStatus())
                .createdAt(taskJpaEntity.getCreatedAt())
                .completedAt(taskJpaEntity.getCompletedAt())
                .build();
    }
}
