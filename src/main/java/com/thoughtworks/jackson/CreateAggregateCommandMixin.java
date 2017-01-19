package com.thoughtworks.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class CreateAggregateCommandMixin {
    public CreateAggregateCommandMixin(@JsonProperty("id") String id) {
    }
}
