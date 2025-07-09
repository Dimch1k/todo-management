package com.example.todomanagement.service.impl;

import com.example.todomanagement.dto.TodoDto;
import com.example.todomanagement.entity.Todo;
import com.example.todomanagement.exeption.ResourceAlreadyExistsException;
import com.example.todomanagement.exeption.ResourceNotFoundException;
import com.example.todomanagement.repository.TodoRepository;
import com.example.todomanagement.service.TodoService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TodoServiceImpl implements TodoService {

    private ModelMapper modelMapper;
    private TodoRepository todoRepository;

    @Override
    public TodoDto createTodo(TodoDto todoDto) {
        Todo todo = modelMapper.map(todoDto, Todo.class);
        if (todoRepository.existsByTitle(todo.getTitle())) {
            throw new ResourceAlreadyExistsException("ToDo", "Title", todo.getTitle());
        }
        todoRepository.save(todo);
        return modelMapper.map(todo, TodoDto.class);
    }

    @Override
    public TodoDto findTodoById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ToDo", "id", id.toString()));
        return modelMapper.map(todo, TodoDto.class);
    }

    @Override
    public List<TodoDto> findAllTodos() {
        List<Todo> todoList = todoRepository.findAll();
        return todoList.stream().map(t -> modelMapper.map(t, TodoDto.class)).collect(Collectors.toList());
    }

    @Override
    public TodoDto updateTodo(TodoDto todoDto, Long id) {
        todoDto.setId(id);
        Todo todo = todoRepository.findById(todoDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("ToDo", "id", todoDto.getId().toString()));
        todo.setTitle(todoDto.getTitle());
        todo.setDescription(todoDto.getDescription());
        todo.setCompleted(todoDto.isCompleted());
        todoRepository.save(todo);
        return modelMapper.map(todo, TodoDto.class);
    }

    @Override
    public void deleteTodo(Long id) {
        todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ToDo", "id", id.toString()));
        todoRepository.deleteById(id);
    }

    @Override
    public TodoDto completeTodo(Long id) {
        Todo complitedTodo = todoRepository
                .findById(id).orElseThrow(() -> new ResourceNotFoundException("ToDo", "id", id.toString()));
        complitedTodo.setCompleted(true);
        todoRepository.save(complitedTodo);
        return modelMapper.map(complitedTodo, TodoDto.class);
    }

    @Override
    public TodoDto incompleteTodo(Long id) {
        Todo incomlitedTodo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ToDo", "id", id.toString()));
        incomlitedTodo.setCompleted(false);
        todoRepository.save(incomlitedTodo);
        return modelMapper.map(incomlitedTodo, TodoDto.class);
    }

}
