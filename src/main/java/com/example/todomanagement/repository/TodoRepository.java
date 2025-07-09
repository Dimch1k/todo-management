package com.example.todomanagement.repository;

import com.example.todomanagement.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    boolean getByTitle(String title);

    boolean existsByTitle(String title);
}
