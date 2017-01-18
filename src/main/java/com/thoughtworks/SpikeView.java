package com.thoughtworks;

import javax.persistence.Id;

/**
 * Created by Avishek Sen Gupta on 1/18/2017.
 */
public class SpikeView {
    @Id
    private final String id;
    private final String value;

    public SpikeView(String id, String value) {
        this.id = id;
        this.value = value;
    }
}
