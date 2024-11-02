package com.TaskManegment.irusriGroup.dto;


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
    private UserDto user;
}
