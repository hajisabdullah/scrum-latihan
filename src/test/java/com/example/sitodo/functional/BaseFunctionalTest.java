package com.example.sitodo.functional;

import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Configuration.browser;
import static com.codeborne.selenide.Configuration.headless;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.closeWebDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class BaseFunctionalTest {

    private static final Logger LOG = LoggerFactory.getLogger(BaseFunctionalTest.class);

    protected static final int DEFAULT_NUMBER_OF_COLUMNS = 4;

    @LocalServerPort
    protected int serverPort;

    /**
     * Reads $CI environment variable to see if the test is running on CI server or not.
     * Usually, $CI is set to true when run on GitLab Runner.
     */
    @Value("${ci:false}")
    private boolean isCI;

    @Value("${sitodo.baseUrl:http://localhost}")
    private String testBaseUrl;

    @BeforeEach
    void setUp() {
        browser = "firefox";
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
        headless = isCI;
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }

    protected void postNewTodoItem(String item) {
        WebElement inputField = $(By.tagName("input"));

        inputField.sendKeys(item, Keys.ENTER);

        try {
            // Introduce artificial delay allowing DOM to be correctly rendered after inserting
            // multiple items consecutively
            Thread.sleep(500);
        } catch (InterruptedException exception) {
            LOG.error("There was a problem during artificial delay", exception);
        }
    }

    protected void checkItemsInList(List<String> expectedItems) {
        try {
            // Introduce artificial delay allowing DOM to be correctly rendered after inserting
            // multiple items consecutively
            Thread.sleep(500);
        } catch (InterruptedException exception) {
            LOG.error("There was a problem during artificial delay", exception);
        }

        $(By.tagName("tbody")).findAll(By.tagName("tr"))
            .shouldHave(CollectionCondition.size(expectedItems.size()))
            .should(CollectionCondition.allMatch("Valid row", (row) -> isRowValid(expectedItems, row)));
    }

    private boolean isRowValid(List<String> expectedItems, WebElement row) {
        List<WebElement> columns = row.findElements(By.tagName("td"));

        assertEquals(DEFAULT_NUMBER_OF_COLUMNS, columns.size(),
            "There were " + columns.size() + " columns in a row");

        String id = columns.get(0).getText();
        String title = columns.get(1).getText();

        return Pattern.matches("\\d+", id) && expectedItems.contains(title);
    }
}
