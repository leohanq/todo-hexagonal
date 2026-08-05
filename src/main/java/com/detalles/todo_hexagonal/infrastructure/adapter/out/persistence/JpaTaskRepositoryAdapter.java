package com.detalles.todo_hexagonal.infrastructure.adapter.out.persistence;

import com.detalles.todo_hexagonal.application.port.out.TaskRepositoryPort;
import com.detalles.todo_hexagonal.domain.model.Task;
import com.detalles.todo_hexagonal.infrastructure.adapter.out.persistence.mapper.TaskPersistenceMapper;
import com.detalles.todo_hexagonal.infrastructure.adapter.out.persistence.repository.SpringDataTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Primary
public class JpaTaskRepositoryAdapter implements TaskRepositoryPort {

    private final SpringDataTaskRepository jpaTaskRepository;
    private final TaskPersistenceMapper taskPersistenceMapper;


    @Override
    public Optional<Task> findById(Long id) {
        return jpaTaskRepository.findById(id)
                .map(taskPersistenceMapper::toDomainModel);
    }

    @Override
    public List<Task> listAll() {
        return jpaTaskRepository.findAll()
                .stream()
                .map(taskPersistenceMapper::toDomainModel)
                .toList();
    }

    @Override
    public Task save(Task task) {
        task.initDefaults();
        return taskPersistenceMapper.toDomainModel(
                jpaTaskRepository.save(taskPersistenceMapper.toJpaEntity(task))
        );
    }


    @Override
    public Task update(Task task) {
        return taskPersistenceMapper.toDomainModel(
                jpaTaskRepository.save(taskPersistenceMapper.toJpaEntity(task))
        );
    }
}