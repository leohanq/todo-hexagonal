package com.detalles.todo_hexagonal.application.port.in;

import com.detalles.todo_hexagonal.domain.model.Task;

public interface GetTaskUseCase {

    Task getTaskById(Long id);
}