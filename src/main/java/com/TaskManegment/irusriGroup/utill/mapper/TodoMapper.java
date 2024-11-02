package com.TaskManegment.irusriGroup.utill.mapper;

import com.TaskManegment.irusriGroup.dto.TodoDto;
import com.TaskManegment.irusriGroup.dto.responsedto.ResponseTodoDto;
import com.TaskManegment.irusriGroup.entity.Todo;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    public default Todo toTodo(TodoDto todoDto) {
        Todo todo = new Todo();
        todo.setId(todoDto.getId());
        todo.setTitle(todoDto.getTitle());
        todo.setDescription(todoDto.getDescription());
        todo.setDueDate(todoDto.getDueDate());
        todo.setPriority(todoDto.getPriority());
        todo.setCompleted(todoDto.isCompleted());
        return todo;
    }

    public default List<ResponseTodoDto> toTodoDTO(List<Todo> todos) {
        List<ResponseTodoDto> dtos = new ArrayList<>();
        for (Todo todo : todos) {
            ResponseTodoDto dto = new ResponseTodoDto();
            dto.setId(todo.getId());
            dto.setTitle(todo.getTitle());
            dto.setDescription(todo.getDescription());
            dto.setDueDate(todo.getDueDate());
            dto.setPriority(todo.getPriority());
            dto.setCompleted(todo.isCompleted());
            // Add other fields if needed

            dtos.add(dto);
        }
        return dtos;
    }
}
