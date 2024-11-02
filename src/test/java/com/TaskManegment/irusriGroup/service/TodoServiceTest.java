package com.TaskManegment.irusriGroup.service;

import com.TaskManegment.irusriGroup.dto.RequestDto.RequestTodoDto;
import com.TaskManegment.irusriGroup.dto.TodoDto;
import com.TaskManegment.irusriGroup.entity.Todo;
import com.TaskManegment.irusriGroup.entity.User;
import com.TaskManegment.irusriGroup.exception.EntryDuplicateException;
import com.TaskManegment.irusriGroup.repo.TodoRepo;
import com.TaskManegment.irusriGroup.repo.UserRepository;
import com.TaskManegment.irusriGroup.service.impl.TodoServiceImpl;
import com.TaskManegment.irusriGroup.utill.mapper.TodoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashSet;
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

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        requestTodoDto = new RequestTodoDto("Title", "Description", LocalDateTime.now(), "High", false);
        User mockUser = new User("user123", "user@example.com", "password", new HashSet<>());
        mappedTodo = new Todo("mockId", "Title", "Description", LocalDateTime.now(), "High", false, mockUser);


        when(userRepository.findById(anyString())).thenReturn(Optional.of(mockUser)); // Simulate user retrieval
        when(todoMapper.toTodo(any(TodoDto.class))).thenReturn(mappedTodo);
    }

    @Test
    void testSaveTodo_Success() {
        String userId = "user123";
        when(todoRepo.existsById(anyString())).thenReturn(false);
        String id = todoService.saveTodo(requestTodoDto, userId);
        assertEquals("mockId", id, "The saved Todo ID should match the expected mock ID");
        verify(userRepository, times(1)).findById(userId);
        verify(todoRepo, times(1)).save(mappedTodo);
    }

    @Test
    void testSaveTodo_DuplicateEntryException() {
        String userId = "user123";


        when(todoRepo.existsById(anyString())).thenReturn(false);
        when(todoRepo.save(mappedTodo)).thenReturn(mappedTodo);
        String id = todoService.saveTodo(requestTodoDto, userId);
        assertEquals("mockId", id, "The saved Todo ID should match the expected mock ID");
        verify(userRepository, times(1)).findById(userId); // Verify user lookup
        verify(todoRepo, times(1)).save(mappedTodo);
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
