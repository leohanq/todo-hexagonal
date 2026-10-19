package com.detalles.todo_hexagonal.usecase.task;

import com.detalles.todo_hexagonal.domain.model.Task;

import java.util.List;

public interface ListTasksUseCase {

    List<Task> findAll();
}