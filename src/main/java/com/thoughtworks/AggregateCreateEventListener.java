package com.thoughtworks;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Avishek Sen Gupta on 1/18/2017.
 */
@Component
public class AggregateCreateEventListener {
    @Autowired
    SpikeViewRepository spikeViewRepository;

    @EventHandler
    public void handle(AggregateCreatedEvent aggregateCreatedEvent) {
        spikeViewRepository.save(new SpikeView(aggregateCreatedEvent.getId(),
                aggregateCreatedEvent.getValue()));
        System.out.println(aggregateCreatedEvent.getId() + " was created and will update read side");
    }
}
