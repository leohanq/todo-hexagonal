package com.detalles.todo_hexagonal.usecase.task;

import com.detalles.todo_hexagonal.domain.model.Task;

public interface ReopenTaskUseCase {

    Task reopen(Long taskId);
}
