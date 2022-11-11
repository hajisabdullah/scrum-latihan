package com.example.sitodo.controller;

import com.example.sitodo.dto.TodoListDto;
import com.example.sitodo.form.TodoItemForm;
import com.example.sitodo.service.MotivationMessageService;
import com.example.sitodo.service.TodoListService;
import com.example.sitodo.util.VisitorCounter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@Controller
@Slf4j
public class TodoListController {

    private TodoListService todoListService;

    private MotivationMessageService motivationMessageService;

    // TODO: Perbaiki isu duplikasi kode dan perbaikan abstraksi fungsi terkait
    //       perhitungan jumlah kunjungan.
    private VisitorCounter visitorCounter;

    @Autowired
    public void setTodoListService(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @Autowired
    public void setMotivationMessageService(MotivationMessageService motivationMessageService) {
        this.motivationMessageService = motivationMessageService;
    }

    @Autowired
    public void setVisitorCounter(VisitorCounter visitorCounter) {
        this.visitorCounter = visitorCounter;
    }

    @GetMapping("/list")
    public String showList(Model model) {
        model.addAttribute("todoItemForm", new TodoItemForm());
        model.addAttribute("motivationMessage", motivationMessageService.computeMotivationMessage(0, 0));
        model.addAttribute("visitorCount", visitorCounter.getCount().incrementAndGet());

        return "list";
    }

    @GetMapping("/list/{id}")
    public String showList(@PathVariable("id") Long id, Model model) {
        TodoListDto foundTodoList = todoListService.getTodoListById(id);
        log.debug("Show list with ID {}", foundTodoList.id());

        model.addAttribute("todoList", foundTodoList);
        model.addAttribute("todoItemForm", new TodoItemForm());
        model.addAttribute("motivationMessage", motivationMessageService.computeMotivationMessage(foundTodoList.countTotal(), foundTodoList.countFinishedItems()));
        model.addAttribute("visitorCount", visitorCounter.getCount().incrementAndGet());

        return "list";
    }

    @PostMapping("/list")
    public String newItem(@Valid TodoItemForm todoItemForm, BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> log.warn(error.toString()));
            model.addAttribute("visitorCount", visitorCounter.getCount().incrementAndGet());

            return "list";
        }

        TodoListDto saved = todoListService.addTodoItem(todoItemForm);
        log.debug("Saved a new item into a new list (ID: {})", saved.id());

        return redirectToList(saved.id());
    }

    @PostMapping("/list/{id}")
    public String newItem(@PathVariable("id") Long id, @Valid TodoItemForm todoItemForm,
                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> log.warn(error.toString()));

            TodoListDto foundTodoList = todoListService.getTodoListById(id);
            log.debug("Show list with ID {}", foundTodoList.id());

            model.addAttribute("todoList", foundTodoList);
            model.addAttribute("motivationMessage", motivationMessageService.computeMotivationMessage(foundTodoList.countTotal(), foundTodoList.countFinishedItems()));
            model.addAttribute("visitorCount", visitorCounter.getCount().incrementAndGet());

            return "list";
        }

        TodoListDto saved = todoListService.addTodoItem(id, todoItemForm);
        log.debug("Saved a new item into a list (ID {})", saved.id());

        return redirectToList(saved.id());
    }

    @GetMapping("/list/{list_id}/update/{item_id}")
    public String updateItem(@PathVariable("list_id") Long listId,
                             @PathVariable("item_id") Long itemId,
                             @RequestParam("finished") Boolean finished) {
        TodoListDto updated = todoListService.setTodoItemFinished(listId, itemId, finished);
        log.debug("Updated an item in a list with ID {}", updated.id());

        return redirectToList(updated.id());
    }

    @ExceptionHandler
    public String handleException(NoSuchElementException exception) {
        return "404";
    }

    private String redirectToList(Long id) {
        return String.format("redirect:/list/%d", id);
    }
}
