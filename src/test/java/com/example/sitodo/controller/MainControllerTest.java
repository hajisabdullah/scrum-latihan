package com.example.sitodo.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MainController.class)
@Tag("unit")
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("HTTP GET '/' redirects to '/list")
    void showMainPage_resolvesToIndex() throws Exception {
        mockMvc.perform(get("/")).andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl("/list")
        );
    }
}
