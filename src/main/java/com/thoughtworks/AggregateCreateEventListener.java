package com.thoughtworks;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
public class AggregateCreateEventListener {
    private SpikeViewRepository spikeViewRepository;

    public AggregateCreateEventListener(SpikeViewRepository spikeViewRepository) {
        this.spikeViewRepository = spikeViewRepository;
    }

    @ServiceActivator(inputChannel = "event-in")
    public void handle(AggregateCreatedEvent aggregateCreatedEvent) {
        spikeViewRepository.save(new SpikeView(aggregateCreatedEvent.getId(),
                aggregateCreatedEvent.getValue()));
        System.out.println(aggregateCreatedEvent.getId() + " was created and will update read side");
    }
}
