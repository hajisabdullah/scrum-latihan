package com.example.sitodo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class TodoList {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<TodoItem> items = new ArrayList<>();

    public TodoList(List<TodoItem> items) {
        this.items = items;
    }

    public void addTodoItem(TodoItem item) {
        items.add(item);
    }
}
