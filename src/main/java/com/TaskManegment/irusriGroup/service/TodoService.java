package com.TaskManegment.irusriGroup.service;

import com.TaskManegment.irusriGroup.dto.RequestDto.RequestTodoDto;
import com.TaskManegment.irusriGroup.dto.paginateddto.PaginatedTodoDto;
import com.TaskManegment.irusriGroup.dto.responsedto.ResponseTodoDto;

import java.util.List;


public interface TodoService {
    String saveTodo(RequestTodoDto dto);

    boolean deleteTodo(String todoId);

    boolean updateTodo(RequestTodoDto dto, String todoId);

    PaginatedTodoDto searchTodos(int pageNo, int size);

    boolean updateCompletedStatus(boolean value, String id);

    List<ResponseTodoDto> getTodoBySearchText(String searchText);

    PaginatedTodoDto getTodosWithSorting(int pageNo, int size, String sortBy, String sortDirection);
}
