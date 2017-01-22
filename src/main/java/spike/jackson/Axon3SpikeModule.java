package spike.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import spike.AggregateCreatedEvent;
import spike.CreateAggregateCommand;
import spike.FieldUpdatedEvent;
import spike.UpdateAggregateCommand;
import spike.jackson.axon.GapAwareTrackingTokenMixin;
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
