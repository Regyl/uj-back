package com.github.regyl.unfriendlyjarvis.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.regyl.unfriendlyjarvis.entity.enums.Priority;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Entity for storing to-do tasks.
 * Tasks are bound to specific user account.
 */
@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "e_todo_task")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class TodoTaskEntity extends AbstractEntity {

    /**
     * Account owner of the task.
     */
    @NotNull
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    /**
     * Task title.
     */
    @NotBlank
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * Task description (optional).
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Task completion status.
     */
    @NotNull
    @Column(name = "completed", nullable = false)
    private Boolean completed;

    /**
     * Task priority level.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, columnDefinition = "VARCHAR(20) NOT NULL DEFAULT 'MEDIUM'")
    private Priority priority;
}

