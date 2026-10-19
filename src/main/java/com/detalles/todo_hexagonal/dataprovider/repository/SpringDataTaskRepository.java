package com.detalles.todo_hexagonal.dataprovider.repository;

import com.detalles.todo_hexagonal.dataprovider.entity.TaskJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTaskRepository extends JpaRepository<TaskJpaEntity, Long> {

}
