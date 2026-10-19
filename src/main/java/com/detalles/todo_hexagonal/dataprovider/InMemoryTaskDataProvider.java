package com.detalles.todo_hexagonal.dataprovider;

import com.detalles.todo_hexagonal.usecase.task.TaskPersistenceGateway;
import com.detalles.todo_hexagonal.domain.model.Task;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryTaskDataProvider implements TaskPersistenceGateway {

    private final Map<Long, Task> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);


    @Override
    public Task save(Task task) {
        task.initDefaults();
        if (task.getId() == null) {
            task.setId(idGenerator.getAndIncrement());
        }
        store.put(task.getId(), task);
        return task;
    }

    @Override
    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Task> listAll() {
        return List.copyOf(store.values());
    }

    @Override
    public Task update(Task task) {
        return store.computeIfPresent(task.getId(), (id, existingTask) -> {
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            return existingTask;
        });
    }
}
