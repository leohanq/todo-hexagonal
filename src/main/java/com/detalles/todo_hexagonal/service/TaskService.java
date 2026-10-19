package com.detalles.todo_hexagonal.service;

import com.detalles.todo_hexagonal.usecase.task.CompleteTaskUseCase;
import com.detalles.todo_hexagonal.usecase.task.CreateTaskUseCase;
import com.detalles.todo_hexagonal.usecase.task.GetTaskUseCase;
import com.detalles.todo_hexagonal.usecase.task.ListTasksUseCase;
import com.detalles.todo_hexagonal.usecase.task.ReopenTaskUseCase;
import com.detalles.todo_hexagonal.usecase.task.TaskPersistenceGateway;
import com.detalles.todo_hexagonal.domain.exception.TaskNotFoundException;
import com.detalles.todo_hexagonal.domain.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService implements CreateTaskUseCase,
        ListTasksUseCase,
        GetTaskUseCase,
        CompleteTaskUseCase,
        ReopenTaskUseCase {

    private final TaskPersistenceGateway taskRepository;


    @Override
    public Task create(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.listAll();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Override
    public Task complete(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        task.completed();
        return taskRepository.update(task);
    }

    @Override
    public Task reopen(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        task.reopen();
        return taskRepository.update(task);
    }
}