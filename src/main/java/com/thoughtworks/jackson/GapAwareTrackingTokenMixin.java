package com.thoughtworks.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.SortedSet;

/**
 * Created by Avishek Sen Gupta on 1/18/2017.
 */
public class GapAwareTrackingTokenMixin {
    public GapAwareTrackingTokenMixin(@JsonProperty("index") long index, @JsonProperty("gaps")SortedSet<Long> gaps) {
    }
}
