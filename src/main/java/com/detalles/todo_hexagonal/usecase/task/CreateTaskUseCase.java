package com.detalles.todo_hexagonal.usecase.task;

import com.detalles.todo_hexagonal.domain.model.Task;

public interface CreateTaskUseCase {

    Task create(Task task);
}