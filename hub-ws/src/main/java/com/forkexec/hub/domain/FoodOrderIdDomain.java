package com.forkexec.hub.domain;

import java.util.concurrent.atomic.AtomicInteger;

public class FoodOrderIdDomain {

    private static AtomicInteger idCounter = new AtomicInteger();
    private String id;

    public FoodOrderIdDomain() {
        this.id = createID();
    }

    private static String createID() {
        return String.valueOf(idCounter.getAndIncrement());
    }

    public String getId() {
        return this.id;
    }

}
