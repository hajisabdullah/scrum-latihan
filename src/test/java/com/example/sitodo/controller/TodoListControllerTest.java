package com.example.sitodo.controller;

import com.example.sitodo.dto.TodoItemDto;
import com.example.sitodo.dto.TodoListDto;
import com.example.sitodo.service.MotivationMessageService;
import com.example.sitodo.service.TodoListService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoListController.class)
@Tag("unit")
class TodoListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoListService todoListService;

    @MockBean
    private MotivationMessageService motivationMessageService;

    @Test
    @DisplayName("HTTP GET '/list' retrieves list view")
    void showList_resolvesToIndex() throws Exception {
        // TODO : check if '/list' is access, return status OK and return 'list.html'
    }

    @Test
    @DisplayName("HTTP GET '/list' returns an HTML page")
    void showList_returnsHtml() throws Exception {
        mockMvc.perform(get("/list")).andExpectAll(
            status().isOk(),
            content().contentTypeCompatibleWith(TEXT_HTML),
            content().encoding(UTF_8),
            content().string(containsString("</html>"))
        );
    }

    @Test
    @DisplayName("HTTP GET '/list/{id}' returns an HTML page with non-empty list")
    void showList_byId_returnsHtml() throws Exception {
        when(todoListService.getTodoListById(1L)).thenReturn(new TodoListDto(1L, List.of(new TodoItemDto(1L, "Buy milk", false))));

        mockMvc.perform(get("/list/1")).andExpectAll(
            status().isOk(),
            content().contentTypeCompatibleWith(TEXT_HTML),
            content().encoding(UTF_8),
            content().string(containsString("<table")),
            content().string(containsString("<tr")),
            content().string(containsString("Buy milk")),
            content().string(containsString("</html>"))
        );
    }

    @Test
    @DisplayName("Suppose the given ID does not exist, HTTP GET '/list/{id}' returns an error page")
    void showList_byId_notFound() throws Exception {
        when(todoListService.getTodoListById(anyLong())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/list/1")).andExpectAll(
            content().string(containsString("Not Found")),
            view().name("404")
        );
    }

    @Test
    @DisplayName("HTTP GET '/list/{id}/update/{item_id}' successfully updated status of an item")
    void updateItem_ok() throws Exception {
        TodoListDto todoListSingleItem = new TodoListDto(1L, List.of(new TodoItemDto(1L, "Buy milk", true)));

        when(todoListService.setTodoItemFinished(1L, 1L, true))
            .thenReturn(todoListSingleItem);
        when(todoListService.getTodoListById(1L)).thenReturn(todoListSingleItem);

        // TODO: create test: when the update is successful,
        // then return redirect status (3xx) and redirect to '/list/{id}'

        // TODO: create test: after redirect to '/list/{id}', check the contains of the TodoList
        // Hint : use todoListSingleItem

        // Note: Notice that we don't actually verify whether the item was successfully
        // updated. It is all pre-scripted in the mock object. We dictate how the SUT
        // (Software Under Test) should response when given a stimulus during execution
        // of a test case. In this example, we told the service layer to provide a TodoList
        // object that supposedly has been modified to the controller. The controller
        // will use the mock object from service layer as input for the view layer.
        // The view layer then use the mock object as data model for rendering the HTML.
    }
}
