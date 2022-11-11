package com.example.sitodo.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TodoItemForm {

    @NotBlank
    private String title;
}
