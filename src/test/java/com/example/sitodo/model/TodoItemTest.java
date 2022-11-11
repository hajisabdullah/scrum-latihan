package com.example.sitodo.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class TodoItemTest {

    @Test
    void testEquals() {
        TodoItem first = new TodoItem("Buy milk");
        TodoItem second = new TodoItem("Cut grass");

        assertNotEquals(first, second);
    }

    @Test
    void testHashCode() {
        TodoItem first = new TodoItem("Buy milk");
        TodoItem second = new TodoItem("Cut grass");

        assertNotEquals(first.hashCode(), second.hashCode());
    }
}
