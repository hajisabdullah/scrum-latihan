package com.example.sitodo.service.impl;

import com.example.sitodo.dto.TodoListDto;
import com.example.sitodo.form.TodoItemForm;
import com.example.sitodo.model.TodoItem;
import com.example.sitodo.model.TodoList;
import com.example.sitodo.repository.TodoListRepository;
import com.example.sitodo.service.TodoListService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Tag("unit")
@SpringBootTest
class TodoListServiceImplTest {

    @Autowired
    private TodoListService todoListService;

    @MockBean
    private TodoListRepository todoListRepository;

    @Test
    @DisplayName("Given an existing ID, getTodoListById should return an existing list")
    void getTodoListById_ok() {
        TodoList todoList = createTodoListEntity("Buy milk");
        when(todoListRepository.findById(anyLong())).thenReturn(Optional.of(todoList));

        TodoListDto savedList = todoListService.getTodoListById(1L);

        assertFalse(savedList.items().isEmpty());
    }

    @Test
    @DisplayName("Suppose the list does not exist, getTodoListById should throw an exception")
    void getTodoListById_exception() {
        when(todoListRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> todoListService.getTodoListById(1L));
    }

    @Test
    @DisplayName("Given a new todo item, addTodoItem should save the item into a new list")
    void addTodoItem_ok() {
        TodoItem fakeTodoItem = new TodoItem(1L, "Buy milk");
        TodoList fakeTodoList = new TodoList();
        fakeTodoList.setId(1L);
        fakeTodoList.setItems(List.of(fakeTodoItem));
        when(todoListRepository.save(any(TodoList.class)))
            .thenReturn(fakeTodoList);

        TodoListDto newTodoList = todoListService.addTodoItem(createTodoItemForm("Buy milk"));

        assertFalse(newTodoList.items().isEmpty());
    }

    @Test
    @DisplayName("Given a todo item, addTodoItem should save the item into an existing list")
    void addTodoItem_existingList_ok() {
        TodoItem fakeTodoItem = new TodoItem(1L, "Buy milk");
        TodoList fakeTodoList = new TodoList();
        fakeTodoList.setId(1L);
        fakeTodoList.addTodoItem(fakeTodoItem);
        when(todoListRepository.findById(anyLong()))
            .thenReturn(Optional.of(fakeTodoList));
        when(todoListRepository.save(fakeTodoList))
            .thenReturn(fakeTodoList);

        TodoListDto updatedTodoList = todoListService.addTodoItem(1L, createTodoItemForm("Touch grass"));

        assertEquals(2L, updatedTodoList.countTotal());
    }

    @Test
    @DisplayName("Suppose the list does not exist, addTodoItem should throw an exception")
    void addTodoItem_existingList_exception() {
        assertThrows(NoSuchElementException.class, () -> todoListService.addTodoItem(1L, createTodoItemForm("Buy milk")));
    }

    @Test
    @DisplayName("Given an existing list with an item, setTodoItemFinished should update the status of an item")
    void updateTodoItem_ok() {
        TodoList fakeTodoList = new TodoList();
        fakeTodoList.setId(1L);
        fakeTodoList.addTodoItem(new TodoItem(1L, "Buy milk"));
        when(todoListRepository.findById(anyLong())).thenReturn(Optional.of(fakeTodoList));
        when(todoListRepository.save(fakeTodoList)).thenReturn(fakeTodoList);

        TodoListDto updatedTodoList = todoListService.setTodoItemFinished(1L, 1L, true);

        assertEquals(1, updatedTodoList.countFinishedItems());
    }

    @Test
    @DisplayName("Suppose the list does not exist, setTodoItemFinished should throw an exception")
    void updateTodoItem_exception() {
        assertThrows(NoSuchElementException.class, () -> todoListService.setTodoItemFinished(1L, 2L, true));
    }

    private TodoList createTodoListEntity(String... items) {
        TodoList list = new TodoList(new ArrayList<>());

        Arrays.stream(items)
            .map(TodoItem::new)
            .forEach(list::addTodoItem);

        return list;
    }

    private static TodoItemForm createTodoItemForm(String title) {
        TodoItemForm newTodoItem = new TodoItemForm();
        newTodoItem.setTitle(title);

        return newTodoItem;
    }
}
