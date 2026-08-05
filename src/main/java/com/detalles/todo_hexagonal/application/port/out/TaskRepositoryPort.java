package com.detalles.todo_hexagonal.application.port.out;

import com.detalles.todo_hexagonal.domain.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepositoryPort {

    Task save(Task task);

    Optional<Task> findById(Long id);

    List<Task> listAll();

    Task update(Task task);
}