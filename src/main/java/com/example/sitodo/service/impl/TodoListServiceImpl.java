package com.example.sitodo.service.impl;

import com.example.sitodo.dto.TodoItemDto;
import com.example.sitodo.dto.TodoListDto;
import com.example.sitodo.form.TodoItemForm;
import com.example.sitodo.model.TodoItem;
import com.example.sitodo.model.TodoList;
import com.example.sitodo.repository.TodoListRepository;
import com.example.sitodo.service.TodoListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class TodoListServiceImpl implements TodoListService {

    private TodoListRepository todoListRepository;

    @Autowired
    public void setTodoListRepository(TodoListRepository todoListRepository) {
        this.todoListRepository = todoListRepository;
    }

    @Override
    public TodoListDto getTodoListById(Long listId) throws NoSuchElementException {
        Optional<TodoList> queryResult = todoListRepository.findById(listId);

        if (queryResult.isEmpty()) {
            log.warn("Cannot find a todo list with ID {}", listId);
            throw new NoSuchElementException();
        }

        TodoList foundTodoList = queryResult.get();

        return createTodoListDto(foundTodoList);
    }

    @Override
    public TodoListDto addTodoItem(TodoItemForm todoItemForm) {
        TodoItem todoItem = new TodoItem();
        todoItem.setTitle(todoItemForm.getTitle());
        TodoList todoList = new TodoList();
        todoList.addTodoItem(todoItem);

        TodoList savedTodoList = todoListRepository.save(todoList);

        return createTodoListDto(savedTodoList);
    }

    @Override
    public TodoListDto addTodoItem(Long listId, TodoItemForm todoItemForm) throws NoSuchElementException {
        Optional<TodoList> queryResult = todoListRepository.findById(listId);

        if (queryResult.isEmpty()) {
            log.warn("Cannot find a todo list with ID {}", listId);
            throw new NoSuchElementException();
        }

        TodoItem todoItem = new TodoItem();
        todoItem.setTitle(todoItemForm.getTitle());
        TodoList foundTodoList = queryResult.get();
        foundTodoList.addTodoItem(todoItem);

        TodoList savedTodoList = todoListRepository.save(foundTodoList);

        return createTodoListDto(savedTodoList);
    }

    @Override
    public TodoListDto setTodoItemFinished(Long listId, Long itemId, Boolean finished) throws NoSuchElementException {
        Optional<TodoList> queryResult = todoListRepository.findById(listId);

        if (queryResult.isEmpty()) {
            log.warn("Cannot find a todo list with ID {}", listId);
            throw new NoSuchElementException();
        }

        TodoList foundTodoList = queryResult.get();

        for (TodoItem item : foundTodoList.getItems()) {
            if (item.getId().equals(itemId)) {
                item.setFinished(finished);
            }
        }

        TodoList updatedTodoList = todoListRepository.save(foundTodoList);

        return createTodoListDto(updatedTodoList);
    }

    private static TodoListDto createTodoListDto(TodoList todoList) {
        Long id = todoList.getId();
        List<TodoItem> items = todoList.getItems();

        return new TodoListDto(id, items.stream().map(
            item -> new TodoItemDto(item.getId(), item.getTitle(), item.getFinished())).toList()
        );
    }
}
