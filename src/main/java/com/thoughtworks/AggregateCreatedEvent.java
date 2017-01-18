package com.thoughtworks;

/**
 * Created by Avishek Sen Gupta on 1/18/2017.
 */
public class AggregateCreatedEvent {
    private String id;
    private String value;

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public AggregateCreatedEvent(String id, String value) {


        this.id = id;
        this.value = value;
    }
}
