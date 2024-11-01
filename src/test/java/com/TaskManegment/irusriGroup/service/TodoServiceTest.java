package com.TaskManegment.irusriGroup.service;

import com.TaskManegment.irusriGroup.dto.RequestDto.RequestTodoDto;
import com.TaskManegment.irusriGroup.dto.TodoDto;
import com.TaskManegment.irusriGroup.entity.Todo;
import com.TaskManegment.irusriGroup.exception.EntryDuplicateException;
import com.TaskManegment.irusriGroup.repo.TodoRepo;
import com.TaskManegment.irusriGroup.service.impl.TodoServiceImpl;
import com.TaskManegment.irusriGroup.utill.mapper.TodoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TodoServiceTest {
    @Mock
    private TodoRepo todoRepo;

    @Mock
    private TodoMapper todoMapper;

    @InjectMocks
    private TodoServiceImpl todoService;

    private RequestTodoDto requestTodoDto;

    private Todo mappedTodo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample input data for testing
        requestTodoDto = new RequestTodoDto("Title", "Description", LocalDateTime.now(), "High", false);
        mappedTodo = new Todo("mockId", "Title", "Description", LocalDateTime.now(), "High", false);

        when(todoMapper.toTodo(any(TodoDto.class))).thenReturn(mappedTodo);
    }

    @Test
    void testSaveTodo_Success() {
        when(todoRepo.existsById(anyString())).thenReturn(false); // Simulate Todo ID does not exist
        when(todoRepo.save(mappedTodo)).thenReturn(mappedTodo); // Mock save operation

        String id = todoService.saveTodo(requestTodoDto);

        assertEquals("mockId", id, "The saved Todo ID should match the expected mock ID");
        verify(todoRepo, times(1)).save(mappedTodo);
    }

    @Test
    void testSaveTodo_DuplicateEntryException() {
        when(todoRepo.existsById(anyString())).thenReturn(true); // Simulate duplicate Todo

        assertThrows(EntryDuplicateException.class, () -> todoService.saveTodo(requestTodoDto));
        verify(todoRepo, never()).save(any(Todo.class));
    }

    @Test
    void testDeleteTodo() {
        when(todoRepo.existsById("123")).thenReturn(true);
        doNothing().when(todoRepo).deleteById("123");

        boolean result = todoService.deleteTodo("123");

        assertEquals(true, result);
        verify(todoRepo, times(1)).deleteById("123");
    }

    @Test
    void testUpdateTodo_NotFound() {
        when(todoRepo.findById("123")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> todoService.updateTodo(requestTodoDto, "123"));
    }

    @Test
    void testUpdateTodo_Success() {
        Todo existingTodo = new Todo();
        existingTodo.setId("123");
        when(todoRepo.findById("123")).thenReturn(Optional.of(existingTodo));

        boolean result = todoService.updateTodo(requestTodoDto, "123");

        assertEquals(true, result);
        verify(todoRepo, times(1)).save(any(Todo.class));
    }

    @Test
    void testUpdateCompletedStatus() {
        Todo existingTodo = new Todo();
        existingTodo.setId("123");
        when(todoRepo.findById("123")).thenReturn(Optional.of(existingTodo));

        boolean result = todoService.updateCompletedStatus(true, "123");

        assertEquals(true, result);
        assertEquals(true, existingTodo.isCompleted());
    }
}
