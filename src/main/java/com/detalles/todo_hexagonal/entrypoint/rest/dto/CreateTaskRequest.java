package com.detalles.todo_hexagonal.entrypoint.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateTaskRequest {

    @NotBlank(message = "Title is required")
    private String title;
    private String description;
}
