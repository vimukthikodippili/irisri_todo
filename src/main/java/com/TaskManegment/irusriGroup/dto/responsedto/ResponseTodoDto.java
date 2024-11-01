package com.TaskManegment.irusriGroup.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTodoDto {
    private String id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String priority;
    private boolean completed;
}
