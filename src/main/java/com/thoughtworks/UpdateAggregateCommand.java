package com.thoughtworks;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class UpdateAggregateCommand {
    @TargetAggregateIdentifier
    private String id;

    public UpdateAggregateCommand(String id) {

        this.id = id;
    }
}
