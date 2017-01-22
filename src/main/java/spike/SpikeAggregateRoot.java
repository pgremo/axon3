package spike;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.commandhandling.model.AggregateLifecycle.*;

@Aggregate
public class SpikeAggregateRoot {

    @AggregateIdentifier
    private String id;

    private String fieldToBeUpdated = " has not been updated";

    public SpikeAggregateRoot() {
    }

    @CommandHandler
    public SpikeAggregateRoot(CreateAggregateCommand createAggregateCommand) {
        System.out.println("Got command");
        apply(new AggregateCreatedEvent(createAggregateCommand.getId(), fieldToBeUpdated));
    }

    @CommandHandler
    public void updateField(UpdateAggregateCommand updateAggregateCommand) {
        apply(new FieldUpdatedEvent(id));
    }

    @EventSourcingHandler
    public void handleAggregateCreated(AggregateCreatedEvent event) {
        id = event.getId();
        System.out.println(event.getId());
    }

    @EventSourcingHandler
    public void handleFieldUpdated(FieldUpdatedEvent event) {
        this.fieldToBeUpdated = " has been updated";
        System.out.println(fieldToBeUpdated);
    }
}

