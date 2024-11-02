package com.TaskManegment.irusriGroup.controller;

import com.TaskManegment.irusriGroup.dto.RequestDto.RequestTodoDto;
import com.TaskManegment.irusriGroup.dto.paginateddto.PaginatedTodoDto;
import com.TaskManegment.irusriGroup.service.TodoService;
import com.TaskManegment.irusriGroup.utill.StandardResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class TodoControllerTest {

    @Mock
    private TodoService todoService;

    @InjectMocks
    private TodoController todoController;

    private RequestTodoDto requestTodoDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        requestTodoDto = new RequestTodoDto("Task 1", "Description", LocalDateTime.now().plusDays(1), "High", false);
    }

    @Test
    void testSaveTodo() {

        String userId = "user123";
        RequestTodoDto requestTodoDto = new RequestTodoDto();
        when(todoService.saveTodo(any(RequestTodoDto.class), eq(userId))).thenReturn("123");


        ResponseEntity<StandardResponse> response = todoController.saveCustomer(userId, requestTodoDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("123", response.getBody().getData());
        assertEquals(" todo added! ", response.getBody().getMessage());
    }
    @Test
    void testDeleteTodo() {
        when(todoService.deleteTodo("123")).thenReturn(true);

        ResponseEntity<StandardResponse> response = todoController.deleteTodo("123");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(true, response.getBody().getData());
    }

    @Test
    void testUpdateTodo() throws Exception {
        when(todoService.updateTodo(any(RequestTodoDto.class), any(String.class))).thenReturn(true);

        ResponseEntity<StandardResponse> response = todoController.updateTodo(requestTodoDto, "123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().getData());
    }

    @Test
    void testSearchTodos() {
        PaginatedTodoDto paginatedTodoDto = new PaginatedTodoDto();
        when(todoService.searchTodos(0, 10)).thenReturn(paginatedTodoDto);

        ResponseEntity<StandardResponse> response = todoController.searchTodos(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paginatedTodoDto, response.getBody().getData());
    }

    @Test
    void testUpdateCompletionStatus() {
        when(todoService.updateCompletedStatus(true, "123")).thenReturn(true);

        ResponseEntity<StandardResponse> response = todoController.updateCompletionStatus(true, "123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().getData());
    }
}
