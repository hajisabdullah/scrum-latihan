package com.example.sitodo.functional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("User Story 3: See Motivation Message")
@Tag("e2e")
class SeeMotivationMessageTest extends BaseFunctionalTest {

    private static final Logger LOG = LoggerFactory.getLogger(SeeMotivationMessageTest.class);

    @Value("${sitodo.motivation.empty:EMPTY}")
    private String emptyListMessage;

    @Value("${sitodo.motivation.noFinished:NO_FINISH}")
    private String noFinishedMessage;

    @Value("${sitodo.motivation.halfFinished:HALF_FINISH}")
    private String halfFinishedMessage;

    @Value("${sitodo.motivation.someFinished:SOME_FINISH}")
    private String someFinishedMessage;

    @Value("${sitodo.motivation.allFinished:ALL_FINISH}")
    private String allFinishedMessage;

    @Value("${sitodo.motivation.fewItems:FEW_ITEMS}")
    private String fewItemsMessage;

    @Value("${sitodo.motivation.manyItems:MANY_ITEMS}")
    private String manyItemsMessage;

    @Value("${sitodo.motivation.fewItemsThreshold:5}")
    private int fewItemsThreshold;

    @Value("${sitodo.motivation.manyItemsThreshold:10}")
    private int manyItemsThreshold;

    @Test
    @DisplayName("A user can see a motivation message when there is only one item in the list")
    void todoList_singleItem() {
        open("/");

        // Add one item
        postNewTodoItem("Buy milk");

        // See the motivation message when there is no finished item
        WebElement motivationMessage = $(By.id("motivation_message"));
        assertThat(motivationMessage.getText(), allOf(
            containsString(fewItemsMessage),
            containsString(noFinishedMessage)
        ));

        // Finish that one item
        WebElement markFinishLink = $(By.className("sitodo-finish-link"));
        markFinishLink.click();

        // The message should be different
        motivationMessage = $(By.id("motivation_message"));
        assertThat(motivationMessage.getText(), allOf(
            containsString(fewItemsMessage),
            containsString(allFinishedMessage)
        ));
    }

    @Test
    @DisplayName("A user can see a motivation message when half of the items are finished")
    void todoList_multipleItems_halfFinished() {
        open("/");

        // Create 10 items
        List<String> items = IntStream.range(0, manyItemsThreshold)
            .mapToObj(i -> "Task " + i)
            .toList();

        items.forEach(item -> {
            postNewTodoItem(item);

            try {
                // Introduce artificial delay allowing DOM to be correctly rendered after inserting
                // multiple items consecutively
                Thread.sleep(500);
            } catch (InterruptedException exception) {
                LOG.error("There was a problem during artificial delay", exception);
            }
        });

        // Check the initial motivation message
        WebElement motivationMessage = $(By.id("motivation_message"));
        assertThat(motivationMessage.getText(), allOf(
            containsString(manyItemsMessage),
            containsString(noFinishedMessage)
        ));

        // Mark half of the items as finished sequentially
        IntStream.range(0, items.size() / 2).forEach(i ->
            $(By.tagName("tbody")).findAll(By.tagName("tr"))
                .stream()
                .filter(row -> {
                    List<WebElement> columns = row.findElements(By.tagName("td"));
                    WebElement statusColumn = columns.get(columns.size() - 2);

                    return statusColumn.getText().equalsIgnoreCase("Not Finished");
                })
                .findFirst()
                .ifPresentOrElse(
                    row -> {
                        List<WebElement> columns = row.findElements(By.tagName("td"));
                        WebElement linkColumn = columns.get(3);
                        WebElement link = linkColumn.findElement(By.tagName("a"));

                        link.click();
                    }, () -> fail("There was no clickable link in the todo item")
                )
        );

        // Check final motivation message
        motivationMessage = $(By.id("motivation_message"));
        assertThat(motivationMessage.getText(), allOf(
            containsString(manyItemsMessage),
            containsString(halfFinishedMessage)
        ));
    }
}
