package com.detalles.todo_hexagonal.application.port.in;

import com.detalles.todo_hexagonal.domain.model.Task;

public interface CreateTaskUseCase {

    Task create(Task task);
}