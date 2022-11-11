package com.example.sitodo.service;

import org.springframework.stereotype.Service;

@Service
public interface MotivationMessageService {

    /**
     * Determines the correct motivation message based on the statistics of tasks completion.
     *
     * @param total
     * @param finished
     * @return a motivation message
     */
    String computeMotivationMessage(long total, long finished);
}
