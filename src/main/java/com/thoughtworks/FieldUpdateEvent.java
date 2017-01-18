package com.thoughtworks;

/**
 * Created by Avishek Sen Gupta on 1/18/2017.
 */
public class FieldUpdateEvent {
    private String id;

    public String getId() {
        return id;
    }

    public FieldUpdateEvent(String id) {

        this.id = id;
    }
}
