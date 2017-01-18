package com.thoughtworks.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Avishek Sen Gupta on 1/18/2017.
 */
public abstract class AggregateCreatedEventMixin {
    public AggregateCreatedEventMixin(@JsonProperty("id") String id, @JsonProperty("value") String value) {
    }
}
