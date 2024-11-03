package com.TaskManegment.irusriGroup.service.impl;
import com.TaskManegment.irusriGroup.dto.RequestDto.RequestTodoDto;
import com.TaskManegment.irusriGroup.dto.TodoDto;
import com.TaskManegment.irusriGroup.dto.paginateddto.PaginatedTodoDto;
import com.TaskManegment.irusriGroup.dto.responsedto.ResponseTodoDto;
import com.TaskManegment.irusriGroup.entity.Todo;
import com.TaskManegment.irusriGroup.entity.User;
import com.TaskManegment.irusriGroup.exception.EntryDuplicateException;
import com.TaskManegment.irusriGroup.exception.EntryNotFoundException;
import com.TaskManegment.irusriGroup.repo.TodoRepo;
import com.TaskManegment.irusriGroup.repo.UserRepository;
import com.TaskManegment.irusriGroup.service.TodoService;
import com.TaskManegment.irusriGroup.utill.mapper.TodoMapper;
import com.TaskManegment.irusriGroup.utill.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class TodoServiceImpl implements TodoService {
    private final Logger LOGGER = LoggerFactory.getLogger(TodoServiceImpl.class);
    @Autowired
    private TodoRepo todoRepo;

    @Autowired
    private TodoMapper todoMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    /**
     * Saves a new todo item in the repository.
     *
     * @param dto the RequestTodoDto containing details of the todo to save.
     * @return the ID of the newly created todo.
     * @throws EntryDuplicateException if a todo with the same ID already exists.
     */
    @Override
    public String saveTodo(RequestTodoDto dto,String userId) {
        Optional<User> user = userRepository.findById(userId);
        log.info("Attempting to save todo for user ID: {}", userId);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        TodoDto todoDto = new TodoDto(
                "",
                dto.getTitle(),
                dto.getDescription(),
                dto.getDueDate(),
                dto.getPriority(),
                dto.isCompleted(),
                userMapper.toUserDto(user.get())

        );
        if (!todoRepo.existsById(todoDto.getId())) {
            log.info("Todo with ID: {} successfully saved");
            return todoRepo.save(todoMapper.toTodo(todoDto)).getId();
        } else {
            log.warn("Duplicate entry detected for todo with ID: {}", todoDto.getId());
            throw new EntryDuplicateException("Todo exists");
        }
    }

    /**
     * Deletes a todo by its ID.
     *
     * @param todoId the ID of the todo to delete.
     * @return true if the todo is successfully deleted.
     * @throws RuntimeException if no todo with the given ID is found.
     */
    @Override
    public boolean deleteTodo(String todoId) {
        log.info("Attempting to delete todo with ID: {}", todoId);
        if (todoRepo.existsById(todoId)) {
            todoRepo.deleteById(todoId);

        } else {
            log.error("Todo with ID: {} not found for deletion", todoId);
            throw new RuntimeException("No todo delete" + todoId);
        }
        return true;
    }

    /**
     * Updates an existing todo with new data.
     *
     * @param dto    the updated data for the todo.
     * @param todoId the ID of the todo to update.
     * @return true if the update is successful.
     * @throws EntryNotFoundException if the todo with the specified ID is not found.
     */
    @Override
    public boolean updateTodo(RequestTodoDto dto, String todoId) {
        log.info("Attempting to update todo with ID: {}", todoId);
        Optional<Todo> todo = todoRepo.findById(todoId);
        if (todo.isPresent()) {
            todo.get().setTitle(dto.getTitle());
            todo.get().setDescription(dto.getDescription());
            todo.get().setDueDate(dto.getDueDate());
            todo.get().setPriority(dto.getPriority());
            todo.get().setCompleted(dto.isCompleted());
            todoRepo.save(todo.get());
            log.info("Todo with ID: {} successfully updated", todoId);
            return true;
        } else {
            log.error("Todo with ID: {} not found for update", todoId);
            throw new EntryNotFoundException("todo is not available");
        }
    }

    /**
     * Retrieves a paginated list of todos.
     *
     * @param pageNo the page number to retrieve.
     * @param size   the number of records per page.
     * @return a PaginatedTodoDto containing the list of todos and total count.
     */
    @Override
    public PaginatedTodoDto searchTodos(int pageNo, int size) {
        log.info("Retrieving todos with pagination - page: {}, size: {}", pageNo, size);
        List<Todo> todos = todoRepo.getAllBySearch(PageRequest.of(pageNo, size));
        log.info("Found {} todos");
        return new PaginatedTodoDto(
                todoMapper.toTodoDTO(todos),
                todoRepo.countBySearch()
        );
    }

    /**
     * Updates the completion status of a todo.
     *
     * @param value the new completion status.
     * @param id    the ID of the todo to update.
     * @return true if the update is successful.
     * @throws EntryNotFoundException if the todo with the specified ID is not found.
     */
    @Override
    public boolean updateCompletedStatus(boolean value, String id) {
        log.info("Updating completion status of todo with ID: {} to {}", id, value);
        Optional<Todo> updateStatus = todoRepo.findById(id);
        if (updateStatus.isPresent()) {
            updateStatus.get().setCompleted(value);
            todoRepo.save(updateStatus.get());
            log.info("Todo with ID: {} marked as {}", id, value ? "completed" : "incomplete");
            return true;
        } else {
            log.warn("Todo with ID: {} not found", id);
            throw new EntryNotFoundException("todo is not available");
        }
    }

    /**
     * Searches todos by a specific text.
     *
     * @param searchText the text to search in todo titles or descriptions.
     * @return a list of todos matching the search criteria.
     */
    @Override
    public List<ResponseTodoDto> getTodoBySearchText(String searchText) {
        log.info("Searching todos by text: {}", searchText);
        List<Todo> todoList = todoRepo.findAllTodoBySearchText(searchText);
        log.info("Found {} todos matching search text: {}", todoList.size(), searchText);
        return todoMapper.toTodoDTO(todoList);
    }

    /**
     * Retrieves a paginated and sorted list of todos.
     *
     * @param pageNo        the page number to retrieve.
     * @param size          the number of records per page.
     * @param sortBy        the field to sort by (e.g., dueDate).
     * @param sortDirection the direction of the sort (asc/desc).
     * @return a PaginatedTodoDto containing the sorted list of todos and total count.
     */
    @Override
    public PaginatedTodoDto getTodosWithSorting(int pageNo, int size, String sortBy, String sortDirection) {
        log.info("Retrieving todos with pagination and sorting - page: {}, size: {}, sortBy: {}, sortDirection: {}", pageNo, size, sortBy, sortDirection);
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(direction, sortBy));

        Page<Todo> todoPage = todoRepo.findAll(pageable);
        List<Todo> todos = todoPage.getContent();
        long totalElements = todoPage.getTotalElements();
        log.info("Retrieved {} todos sorted by {} in {} order", todos.size(), sortBy, sortDirection);
        return new PaginatedTodoDto(
                todoMapper.toTodoDTO(todos),
                totalElements
        );
    }
}
