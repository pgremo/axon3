package com.thoughtworks;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

/**
 * Created by Avishek Sen Gupta on 1/18/2017.
 */
public class CreateAggregateCommand {
    @TargetAggregateIdentifier
    private String id;

    public CreateAggregateCommand(String id) {
        System.out.println("Command ctor is = " + id);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
