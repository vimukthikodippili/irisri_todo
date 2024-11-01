package com.TaskManegment.irusriGroup.dto.RequestDto;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class RequestTodoDto {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String priority;
    private boolean completed;
}
