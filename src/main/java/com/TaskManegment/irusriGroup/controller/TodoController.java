package com.TaskManegment.irusriGroup.controller;

import com.TaskManegment.irusriGroup.dto.RequestDto.RequestTodoDto;
import com.TaskManegment.irusriGroup.dto.paginateddto.PaginatedTodoDto;
import com.TaskManegment.irusriGroup.dto.responsedto.ResponseTodoDto;
import com.TaskManegment.irusriGroup.service.TodoService;
import com.TaskManegment.irusriGroup.utill.StandardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;



@RestController
@RequestMapping("/api/todos")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class TodoController {
   private final TodoService todoService;


    /**
     * Creates a new todo item.
     *
     * @param dto the request data for creating a new todo.
     * @return ResponseEntity containing a success message and the created todo ID.
     */
    @PostMapping(path = {"/save"}, params = {"userId"})
    public ResponseEntity<StandardResponse> saveCustomer(
            @RequestParam String userId,
            @RequestBody RequestTodoDto dto) {
        log.debug("Received request to create a todo for userId: {}, request data: {}", userId, dto);
        String id = todoService.saveTodo(dto,userId);
        log.info("Todo created successfully with ID: {}", id);
        return new ResponseEntity<StandardResponse>
                (new StandardResponse(201," todo added! ", id),
                        HttpStatus.CREATED);

    }

    /**
     * Deletes a todo item by its ID.
     *
     * @param todoId the ID of the todo to be deleted.
     * @return ResponseEntity containing a success message.
     */

    @DeleteMapping(path = "/delete/{todoId}")

    public ResponseEntity<StandardResponse> deleteTodo(
            @PathVariable String todoId
    ) {
        log.debug("Received request to delete todo with ID: {}", todoId);
        boolean todo = todoService.deleteTodo(todoId);
        log.info("Todo deleted with ID: {}, Status: {}", todoId, todo);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(201, "Deleted!", todo),
                HttpStatus.CREATED);
    }
    /**
     * Updates a todo item by its ID.
     *
     * @param dto    the request data containing updated todo information.
     * @param todoId the ID of the todo to be updated.
     * @return ResponseEntity with a success message and the updated todo status.
     */

    @PutMapping(path = "/update/{todoId}")
    public ResponseEntity<StandardResponse> updateTodo(
             @RequestBody RequestTodoDto dto,
            @PathVariable String todoId) throws SQLException {
        log.debug("Received request to update todo with ID: {}, update data: {}", todoId, dto);
        boolean todo = todoService.updateTodo(dto, todoId);
        log.info("Todo updated with ID: {}, Update Status: {}", todoId, todo);
        return new ResponseEntity(
                new StandardResponse(204, "Successfully Updated!!!", todo),
                HttpStatus.OK
        );

    }
    /**
     * Retrieves a paginated list of all todos.
     *
     * @param pageNo the page number to retrieve.
     * @param size   the number of todos per page.
     * @return ResponseEntity containing the paginated list of todos.
     */

    @GetMapping(path = {"/all"})
    public ResponseEntity<StandardResponse> searchTodos(
            @RequestParam int pageNo,
            @RequestParam int size) {
        log.debug("Received request to retrieve all todos with pageNo: {}, size: {}", pageNo, size);
        PaginatedTodoDto paginatedTodoDto = todoService.searchTodos( pageNo, size);
        log.info("Todos retrieved successfully for pageNo: {}, size: {}", pageNo, size);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200, "success", paginatedTodoDto),
                HttpStatus.OK);
    }
    /**
     * Updates the completion status of a todo item.
     *
     * @param value the new completion status.
     * @param id    the ID of the todo to update.
     * @return ResponseEntity with a success message and updated status.
     */

    @PatchMapping(path = "/update/status/{id}", params = {"value"})
    public ResponseEntity<StandardResponse> updateCompletionStatus(
            @RequestParam(value = "value") boolean value,
            @PathVariable String id) {
        log.debug("Received request to update completion status for todo ID: {}, new status: {}", id, value);
        boolean updateCompletedStatus = todoService.updateCompletedStatus(value, id);
        log.info("Todo completion status updated for ID: {}, Status: {}", id, updateCompletedStatus);
        return new ResponseEntity(
                new StandardResponse(204, "Successfully Updated!!!", updateCompletedStatus),
                HttpStatus.OK
        );

    }

    /**
     * Searches for todos based on a search text.
     *
     * @param searchText the text to search in todo items.
     * @return ResponseEntity containing the list of todos matching the search criteria.
     */
    @GetMapping(params = {"searchText"},
            path = {"/search"})
    public ResponseEntity<StandardResponse> getRecordById(
            @RequestParam(value = "searchText") String searchText) {
        log.debug("Received request to search todos with text: {}", searchText);
        List<ResponseTodoDto> todoDtos = todoService.getTodoBySearchText(searchText);
        log.info("Search completed for text: {}, Results found: {}", searchText, todoDtos.size());
        return new ResponseEntity<StandardResponse>(new StandardResponse(200, "Success!!!", todoDtos), HttpStatus.OK);
    }
    /**
     * Retrieves a paginated and sorted list of todos.
     *
     * @param pageNo        the page number to retrieve.
     * @param size          the number of todos per page.
     * @param sortBy        the field to sort by (default: dueDate).
     * @param sortDirection the direction of the sort (asc/desc).
     * @return ResponseEntity containing the sorted list of todos.
     */
    @GetMapping("/sorted")
    public ResponseEntity<StandardResponse> getSortedTodos(@RequestParam(defaultValue = "0") int pageNo,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "dueDate") String sortBy,
                                                           @RequestParam(defaultValue = "asc") String sortDirection){

        log.debug("Received request to retrieve sorted todos with pageNo: {}, size: {}, sortBy: {}, sortDirection: {}", pageNo, size, sortBy, sortDirection);
        PaginatedTodoDto sortingTodos = todoService.getTodosWithSorting( pageNo, size,sortBy,sortDirection);
        log.info("Sorted todos retrieved successfully with sortBy: {}, sortDirection: {}", sortBy, sortDirection);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200, "success", sortingTodos),
                HttpStatus.OK);
    }


}
