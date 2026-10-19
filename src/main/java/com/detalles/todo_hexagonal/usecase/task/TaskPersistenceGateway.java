package com.detalles.todo_hexagonal.usecase.task;

import com.detalles.todo_hexagonal.domain.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskPersistenceGateway {

    Task save(Task task);

    Optional<Task> findById(Long id);

    List<Task> listAll();

    Task update(Task task);
}