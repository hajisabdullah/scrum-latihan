package com.example.sitodo.service;

import com.example.sitodo.dto.TodoListDto;
import com.example.sitodo.form.TodoItemForm;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public interface TodoListService {

    TodoListDto getTodoListById(Long listId) throws NoSuchElementException;

    TodoListDto addTodoItem(TodoItemForm todoItemForm);

    TodoListDto addTodoItem(Long listId, TodoItemForm todoItemForm) throws NoSuchElementException;

    TodoListDto setTodoItemFinished(Long listId, Long itemId, Boolean finished) throws NoSuchElementException;
}
