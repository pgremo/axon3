package com.thoughtworks.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Avishek Sen Gupta on 1/18/2017.
 */
public abstract class CreateAggregateCommandMixin {
    public CreateAggregateCommandMixin(@JsonProperty("id") String id) {
    }
}
