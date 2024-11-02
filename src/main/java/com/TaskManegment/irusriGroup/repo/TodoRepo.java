package com.TaskManegment.irusriGroup.repo;

import com.TaskManegment.irusriGroup.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface TodoRepo extends JpaRepository<Todo, String> {

    @Query(value = "SELECT * FROM todo", nativeQuery = true)
    List<Todo> getAllBySearch(Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM todo", nativeQuery = true)
    long countBySearch();

    @Query(value = "SELECT * FROM todo WHERE todo_id LIKE %:searchText% OR title LIKE %:searchText% OR description LIKE %:searchText% OR due_date LIKE %:searchText% OR priority LIKE %:searchText% OR completed LIKE %:searchText% ", nativeQuery = true)
    List<Todo> findAllTodoBySearchText(String searchText);



    Page<Todo> findAll(Pageable pageable);
}
