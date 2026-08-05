package com.detalles.todo_hexagonal.infrastructure.adapter.out.persistence.repository;

import com.detalles.todo_hexagonal.infrastructure.adapter.out.persistence.entity.TaskJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTaskRepository extends JpaRepository<TaskJpaEntity, Long> {

}
