package com.detalles.todo_hexagonal.application.port.in;

import com.detalles.todo_hexagonal.domain.model.Task;

import java.util.List;

public interface ListTasksUseCase {

    List<Task> findAll();
}