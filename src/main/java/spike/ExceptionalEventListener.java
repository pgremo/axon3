package spike;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
public class ExceptionalEventListener {
  @ServiceActivator(inputChannel = "event-in")
  public void handle(AggregateCreatedEvent aggregateCreatedEvent) {
    throw new RuntimeException("THROWING RANDOM EXCEPTION");
  }
}
