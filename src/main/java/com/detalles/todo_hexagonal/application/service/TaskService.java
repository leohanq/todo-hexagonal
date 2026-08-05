package com.detalles.todo_hexagonal.application.service;

import com.detalles.todo_hexagonal.application.port.in.CompleteTaskUseCase;
import com.detalles.todo_hexagonal.application.port.in.CreateTaskUseCase;
import com.detalles.todo_hexagonal.application.port.in.GetTaskUseCase;
import com.detalles.todo_hexagonal.application.port.in.ListTasksUseCase;
import com.detalles.todo_hexagonal.application.port.in.ReopenTaskUseCase;
import com.detalles.todo_hexagonal.application.port.out.TaskRepositoryPort;
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

    private final TaskRepositoryPort taskRepository;


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