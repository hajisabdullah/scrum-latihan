package com.example.sitodo.functional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.title;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Smoke Test")
@Tag("e2e")
class SmokeTest extends BaseFunctionalTest {

    @Test
    @DisplayName("Verify text in <title> tag")
    void site_hasTitle() {
        open("/");

        assertTrue(title().contains("SITODO"), "The page title: " + title());
    }
}
