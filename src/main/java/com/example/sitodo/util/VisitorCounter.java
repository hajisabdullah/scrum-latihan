package com.example.sitodo.util;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class VisitorCounter {

    private AtomicInteger count;

    public VisitorCounter() {
        count = new AtomicInteger(0);
    }

    public void reset() {
        count.set(0);
    }
}
