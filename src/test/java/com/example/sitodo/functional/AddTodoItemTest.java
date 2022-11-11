package com.example.sitodo.functional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Story 1: Add Todo Item")
@Tag("e2e")
@Transactional
class AddTodoItemTest extends BaseFunctionalTest {

    @Test
    @DisplayName("A user can create a single todo item")
    void addTodoItem_single() {
        open("/");
        checkOverallPageLayout();

        // Create a new item
        postNewTodoItem("Buy milk");

        // See the list for the newly inserted item
        checkItemsInList(List.of("Buy milk"));

        // The page can be accessed at the new, unique URL
        String currentUrl = webdriver().driver().url();
        assertTrue(currentUrl.matches(".+/list/\\d+$"), "The URL was: " + currentUrl);
    }

    @Test
    @DisplayName("A user can create multiple todo items")
    void addTodoItem_multiple() {
        open("/");
        checkOverallPageLayout();

        // Create a new item
        postNewTodoItem("Buy milk");

        // See the list for the newly inserted item
        checkItemsInList(List.of("Buy milk"));

        // The page can be accessed at the new, unique URL
        String currentUrl = webdriver().driver().url();
        assertTrue(currentUrl.matches(".+/list/\\d+$"), "The URL was: " + currentUrl);

        // Create another item
        postNewTodoItem("Cut grass");

        // See the list again to see the new items
        checkItemsInList(List.of("Buy milk", "Cut grass"));

        // The URL is still the same from previous items
        assertEquals(currentUrl, webdriver().driver().url());
    }

    @Test
    @DisplayName("A user can create two todo lists consecutively")
    void addTodoItem_twoUsers() {
        // First list
        open("/");
        checkOverallPageLayout();

        postNewTodoItem("Buy milk");
        checkItemsInList(List.of("Buy milk"));

        String firstUrl = webdriver().driver().url();
        assertTrue(firstUrl.matches(".+/list/\\d+$"), "The URL was: " + firstUrl);

        // Second list
        open("/");
        checkOverallPageLayout();

        postNewTodoItem("Buy coffee");
        checkItemsInList(List.of("Buy coffee"));

        String secondUrl = webdriver().driver().url();
        assertTrue(secondUrl.matches(".+/list/\\d+$"), "The URL was: " + secondUrl);

        // Ensure first and second list have different URL
        assertNotEquals(firstUrl, secondUrl, "Both lists must not have the same URL");
    }

    private void checkOverallPageLayout() {
        WebElement heading = $(By.tagName("caption"));
        WebElement inputField = $(By.tagName("input"));
        String headingText = heading.getText();
        String placeholderText = inputField.getAttribute("placeholder");

        assertEquals("Your Todo List", heading.getText(), "The heading title was: " + headingText);
        assertEquals("Enter an item", inputField.getAttribute("placeholder"), "The placeholder text was: " + placeholderText);
    }
}
