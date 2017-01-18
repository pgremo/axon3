package com.thoughtworks.jackson;

import com.fasterxml.jackson.annotation.*;

/**
 * Created by Avishek Sen Gupta on 1/18/2017.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties({"localizedMessage", "suppressed"})
public abstract class ThrowableMixin {

    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "$id")
    private Throwable cause;

    public ThrowableMixin(@JsonProperty("message") String message, @JsonProperty("cause") Throwable cause) {
    }
}
