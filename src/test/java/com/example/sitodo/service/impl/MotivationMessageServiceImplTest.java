package com.example.sitodo.service.impl;

import com.example.sitodo.service.MotivationMessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unit")
@SpringBootTest
class MotivationMessageServiceImplTest {

    @Value("${sitodo.motivation.empty:EMPTY}")
    private String emptyListMessage;

    @Value("${sitodo.motivation.noFinished:NO_FINISH}")
    private String noFinishedMessage;

    @Value("${sitodo.motivation.allFinished:ALL_FINISH}")
    private String allFinishedMessage;

    @Value("${sitodo.motivation.halfFinished:HALF_FINISH}")
    private String halfFinishedMessage;

    @Value("${sitodo.motivation.someFinished:SOME_FINISH}")
    private String someFinishedMessage;

    @Value("${sitodo.motivation.fewItems:FEW_ITEMS}")
    private String fewItemsMessage;

    @Value("${sitodo.motivation.manyItems:MANY_ITEMS}")
    private String manyItemsMessage;

    @Value("${sitodo.motivation.manyItemsThreshold:10}")
    private int manyItemsThreshold;

    @Autowired
    private MotivationMessageService motivationMessageService;

    @Test
    @DisplayName("Given an empty list, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_emptyList() {
        // TODO: Implementasi test sesungguhnya, yaitu method computeMotivationMessage
        //       harus mengembalikan pesan motivasi yang tepat apabila todo list kosong.
    }

    @Test
    @DisplayName("Given a list with few items all unfinished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_fewItems_noFinished() {
        // TODO: Implementasi test sesungguhnya, yaitu method computeMotivationMessage
        //       harus mengembalikan pesan motivasi yang tepat apabila todo list mengandung
        //       sejumlah kecil todo item dan belum ada yang todo item yang selesai.
    }

    @Test
    @DisplayName("Given a list with few items all finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_fewItems_allFinished() {
        String message = motivationMessageService.computeMotivationMessage(4, 4);

        assertThat(message, allOf(
            containsString(fewItemsMessage),
            containsString(allFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with few items half finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_fewItems_halfFinished() {
        // TODO: Implementasi test sesungguhnya, yaitu method computeMotivationMessage
        //       harus mengembalikan pesan motivasi yang tepat apabila todo list mengandung
        //       sejumlah kecil todo item dan setengah dari semua todo item atau lebih telah selesai.
    }

    @Test
    @DisplayName("Given a list with few items and single item finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_fewItems_singleFinished() {
        String message = motivationMessageService.computeMotivationMessage(4, 1);

        assertThat(message, allOf(
            containsString(fewItemsMessage),
            containsString(someFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with many items and none finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_manyItems_noFinished() {
        String[] items = IntStream
            .range(0, manyItemsThreshold)
            .mapToObj(i -> "Task " + i)
            .toArray(String[]::new);

        String message = motivationMessageService.computeMotivationMessage(items.length, 0);

        assertThat(message, allOf(
            containsString(manyItemsMessage),
            containsString(noFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with many items and all finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_manyItems_allFinished() {
        String[] items = IntStream
            .range(0, manyItemsThreshold)
            .mapToObj(i -> "Task " + i)
            .toArray(String[]::new);

        // TODO: Implementasi test sesungguhnya, yaitu method computeMotivationMessage
        //       harus mengembalikan pesan motivasi yang tepat apabila todo list mengandung
        //       sejumlah besar todo item dan semua todo item telah selesai.
    }

    @Test
    @DisplayName("Given a list with many items and half finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_manyItems_halfFinished() {
        String[] items = IntStream
            .range(0, manyItemsThreshold * 2)
            .mapToObj(i -> "Task " + i)
            .toArray(String[]::new);

        String message = motivationMessageService.computeMotivationMessage(items.length, items.length / 2);

        assertThat(message, allOf(
            containsString(manyItemsMessage),
            containsString(halfFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with many items and single finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_manyItems_singleFinished() {
        String[] items = IntStream
            .range(0, manyItemsThreshold * 2)
            .mapToObj(i -> "Task " + i)
            .toArray(String[]::new);

        // TODO: Implementasi test sesungguhnya, yaitu method computeMotivationMessage
        //       harus mengembalikan pesan motivasi yang tepat apabila todo list mengandung
        //       sejumlah besar todo item dan sebagian kecil dari todo item telah selesai.
    }
}
