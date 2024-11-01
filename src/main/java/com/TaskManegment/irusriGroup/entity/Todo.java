package com.TaskManegment.irusriGroup.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "todo")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 80, name = "todo_id")
    private String id;

    @Column(length = 45, name = "title")
    private String title;

    @Column(length = 45, name = "description")
    private String description;

    @Column(name = "dueDate")
    private LocalDateTime dueDate;

    @Column(name = "priority")
    private String priority;

    @Column(name = "completed", columnDefinition = "TINYINT")
    private boolean completed;
}
