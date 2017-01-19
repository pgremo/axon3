package com.thoughtworks.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.thoughtworks.AggregateCreatedEvent;
import com.thoughtworks.CreateAggregateCommand;
import com.thoughtworks.FieldUpdatedEvent;
import com.thoughtworks.UpdateAggregateCommand;
import com.thoughtworks.jackson.axon.GapAwareTrackingTokenMixin;
import org.axonframework.eventsourcing.eventstore.GapAwareTrackingToken;

public class Axon3SpikeModule extends SimpleModule {
  public Axon3SpikeModule() {
    setMixInAnnotation(AggregateCreatedEvent.class, AggregateCreatedEventMixin.class);
    setMixInAnnotation(CreateAggregateCommand.class, CreateAggregateCommandMixin.class);
    setMixInAnnotation(FieldUpdatedEvent.class, FieldUpdatedEventMixin.class);
    setMixInAnnotation(GapAwareTrackingToken.class, GapAwareTrackingTokenMixin.class);
    setMixInAnnotation(UpdateAggregateCommand.class, UpdateAggregateCommandMixin.class);
  }
}
