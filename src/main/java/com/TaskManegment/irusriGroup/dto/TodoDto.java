package com.TaskManegment.irusriGroup.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class TodoDto {
    private String id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String priority;
    private boolean completed;
}
