package com.example.sitodo.dto;

import java.util.List;

public record TodoListDto(Long id, List<TodoItemDto> items) {

    public Long countTotal() {
        return (long) items.size();
    }
    public Long countFinishedItems() {
        return items.stream().filter(TodoItemDto::finished).count();
    }

}
