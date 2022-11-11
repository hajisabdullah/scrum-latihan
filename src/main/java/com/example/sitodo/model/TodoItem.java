package com.example.sitodo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class TodoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private Boolean finished = Boolean.FALSE;

    public TodoItem(String title) {
        this.title = title;
    }

    public TodoItem(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
