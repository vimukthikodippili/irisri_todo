package com.TaskManegment.irusriGroup.controller;

import com.TaskManegment.irusriGroup.dto.RequestDto.RequestTodoDto;
import com.TaskManegment.irusriGroup.dto.TodoDto;
import com.TaskManegment.irusriGroup.dto.paginateddto.PaginatedTodoDto;
import com.TaskManegment.irusriGroup.dto.responsedto.ResponseTodoDto;
import com.TaskManegment.irusriGroup.service.TodoService;
import com.TaskManegment.irusriGroup.utill.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

import static org.hibernate.sql.ast.SqlTreeCreationLogger.LOGGER;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin
@RequiredArgsConstructor
public class TodoController {
   private final TodoService todoService;
   private final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);

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
        LOGGER.info("Received request to create a todo: {}", dto);
        String id = todoService.saveTodo(dto,userId);
        LOGGER.info("Todo created with ID: {}", id);
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
        boolean todo = todoService.deleteTodo(todoId);
        LOGGER.info(" Deleted:" + todo);
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
        boolean todo = todoService.updateTodo(dto, todoId);
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
        PaginatedTodoDto paginatedTodoDto = todoService.searchTodos( pageNo, size);

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
        boolean updateCompletedStatus = todoService.updateCompletedStatus(value, id);
        System.out.println(value + id);
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
        List<ResponseTodoDto> todoDtos = todoService.getTodoBySearchText(searchText);
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


        PaginatedTodoDto sortingTodos = todoService.getTodosWithSorting( pageNo, size,sortBy,sortDirection);

        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200, "success", sortingTodos),
                HttpStatus.OK);
    }


}
