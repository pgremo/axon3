package com.thoughtworks;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

/**
 * Created by Avishek Sen Gupta on 1/18/2017.
 */
public class UpdateAggregateCommand {
    @TargetAggregateIdentifier
    private String id;

    public UpdateAggregateCommand(String id) {

        this.id = id;
    }
}
