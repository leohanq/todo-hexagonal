package com.detalles.todo_hexagonal.entrypoint.rest;

import com.detalles.todo_hexagonal.usecase.task.CompleteTaskUseCase;
import com.detalles.todo_hexagonal.usecase.task.CreateTaskUseCase;
import com.detalles.todo_hexagonal.usecase.task.GetTaskUseCase;
import com.detalles.todo_hexagonal.usecase.task.ListTasksUseCase;
import com.detalles.todo_hexagonal.usecase.task.ReopenTaskUseCase;
import com.detalles.todo_hexagonal.domain.model.Task;
import com.detalles.todo_hexagonal.entrypoint.rest.dto.CreateTaskRequest;
import com.detalles.todo_hexagonal.entrypoint.rest.dto.TaskResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {


    private final CreateTaskUseCase createTaskUseCase;
    private final ListTasksUseCase listTasksUseCase;
    private final GetTaskUseCase getTaskUseCase;
    private final CompleteTaskUseCase completeTaskUseCase;
    private final ReopenTaskUseCase reopenTaskUseCase;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<Task> tasks = listTasksUseCase.findAll();
        List<TaskResponse> taskResponses = tasks.stream()
                .map(TaskResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(taskResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        Task task = getTaskUseCase.getTaskById(id);
        TaskResponse taskResponse = TaskResponse.fromDomain(task);
        return ResponseEntity.ok(taskResponse);
    }



    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest taskRequest) {
        Task taskModel = Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .build();
        var task = createTaskUseCase.create(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(TaskResponse.fromDomain(task));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> completeTask(@PathVariable Long id) {
        Task task = completeTaskUseCase.complete(id);
        return ResponseEntity.ok(TaskResponse.fromDomain(task));
    }

    @PatchMapping("/{id}/reopen")
    public ResponseEntity<TaskResponse> reopenTask(@PathVariable Long id) {
        Task task = reopenTaskUseCase.reopen(id);
        return ResponseEntity.ok(TaskResponse.fromDomain(task));
    }
}
