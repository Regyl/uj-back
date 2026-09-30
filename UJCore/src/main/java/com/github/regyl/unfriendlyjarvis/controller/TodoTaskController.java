package com.github.regyl.unfriendlyjarvis.controller;

import com.github.regyl.unfriendlyjarvis.controller.dto.todo.TodoTaskDto;
import com.github.regyl.unfriendlyjarvis.service.todo.TodoTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Controller for to-do task management.
 */
@RestController
@RequestMapping("/todo-tasks")
@RequiredArgsConstructor
public class TodoTaskController {

    private final TodoTaskService service;

    /**
     * Get all tasks for current user.
     *
     * @return collection of task DTOs
     */
    @GetMapping
    public Collection<TodoTaskDto> findAll() {
        return service.findAll();
    }

    /**
     * Create new task.
     *
     * @param dto task DTO
     * @return created task DTO
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoTaskDto create(@Valid @RequestBody TodoTaskDto dto) {
        return service.create(dto);
    }

    /**
     * Update existing task.
     *
     * @param id  task id
     * @param dto task DTO with updated data
     * @return updated task DTO or 404 if not found
     */
    @PatchMapping("/{id}")
    public TodoTaskDto update(@PathVariable Long id,
                                              @Valid @RequestBody TodoTaskDto dto) {
        return service.update(id, dto);
    }

    /**
     * Delete task by id.
     *
     * @param id task id
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

