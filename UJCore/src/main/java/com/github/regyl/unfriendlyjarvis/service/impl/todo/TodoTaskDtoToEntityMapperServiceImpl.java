package com.github.regyl.unfriendlyjarvis.service.impl.todo;

import com.github.regyl.unfriendlyjarvis.controller.dto.todo.TodoTaskDto;
import com.github.regyl.unfriendlyjarvis.entity.TodoTaskEntity;
import com.github.regyl.unfriendlyjarvis.entity.enums.Priority;
import com.github.regyl.unfriendlyjarvis.entity.enums.Source;
import com.github.regyl.unfriendlyjarvis.service.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Mapper from TodoTaskDto to TodoTaskEntity.
 */
@Component
@RequiredArgsConstructor
public class TodoTaskDtoToEntityMapperServiceImpl implements Function<TodoTaskDto, TodoTaskEntity> {

    private final Supplier<OffsetDateTime> dateTimeSupplier;
    private final SecurityContextService securityContextService;

    @Override
    public TodoTaskEntity apply(TodoTaskDto dto) {
        return TodoTaskEntity.builder()
                .id(dto.getId())
                .accountId(securityContextService.getUserId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .completed(dto.getCompleted() != null && dto.getCompleted())
                .priority(dto.getPriority() != null ? dto.getPriority() : Priority.MEDIUM)
                .source(Source.OTHER)
                .created(dateTimeSupplier.get())
                .build();
    }
}

