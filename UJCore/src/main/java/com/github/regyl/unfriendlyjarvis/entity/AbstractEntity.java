package com.github.regyl.unfriendlyjarvis.entity;

import com.github.regyl.unfriendlyjarvis.entity.enums.Source;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@SuperBuilder
@OnDelete(action = OnDeleteAction.CASCADE)
public abstract class AbstractEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "source", columnDefinition = "VARCHAR(50) NOT NULL DEFAULT 'OTHER'")
    @Enumerated(EnumType.STRING)
    private Source source;

    @NotNull
    @CreationTimestamp
    private OffsetDateTime created;
}
