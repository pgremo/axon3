package com.thoughtworks.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.thoughtworks.AggregateCreatedEvent;
import com.thoughtworks.CreateAggregateCommand;
import com.thoughtworks.FieldUpdatedEvent;
import org.axonframework.eventsourcing.eventstore.GapAwareTrackingToken;

/**
 * Created by Avishek Sen Gupta on 1/18/2017.
 */
public class Axon3SpikeModule extends SimpleModule {
    public Axon3SpikeModule() {
        this.setMixInAnnotation(GapAwareTrackingToken.class, GapAwareTrackingTokenMixin.class)
                .setMixInAnnotation(CreateAggregateCommand.class, CreateAggregateCommandMixin.class)
                .setMixInAnnotation(AggregateCreatedEvent.class, AggregateCreatedEventMixin.class)
                .setMixInAnnotation(FieldUpdatedEvent.class, FieldUpdatedEventMixin.class)
                .setMixInAnnotation(Throwable.class, ThrowableMixin.class)
                .setMixInAnnotation(StackTraceElement.class, StackTraceElementMixin.class)
    }
}
