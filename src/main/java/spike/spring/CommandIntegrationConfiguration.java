package spike.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventsourcing.DomainEventMessage;
import org.axonframework.serialization.RevisionResolver;
import org.axonframework.spring.messaging.OutboundEventMessageChannelAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.jms.JmsOutboundGateway;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

import javax.jms.ConnectionFactory;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.integration.dsl.support.Transformers.toJson;

@Configuration
public class CommandIntegrationConfiguration {
  private final RevisionResolver revisionResolver;
  private final ConnectionFactory connectionFactory;
  private final EventBus eventBus;
  private final ObjectMapper objectMapper;

  @Autowired
  public CommandIntegrationConfiguration(RevisionResolver revisionResolver, ConnectionFactory connectionFactory, EventBus eventBus, ObjectMapper objectMapper) {
    this.revisionResolver = revisionResolver;
    this.connectionFactory = connectionFactory;
    this.eventBus = eventBus;
    this.objectMapper = objectMapper;
  }

  @Bean
  public IntegrationFlow jmsOutboundFlow(@Value("${eventBus.out.destination}") String destination) {
    JmsOutboundGateway messageHandler = Jms.outboundGateway(connectionFactory)
      .requestPubSubDomain(true)
      .requestDestination(destination)
      .get();
    messageHandler.setRequiresReply(false);
    return IntegrationFlows
      .from(outbound())
      .transform(toJson(new Jackson2JsonObjectMapper(objectMapper)))
      .enrichHeaders(h -> h.headerFunction(JmsHeaders.TYPE, x -> x.getHeaders().get("axon-message-type")))
      .handle(messageHandler)
      .get();
  }

  @Bean
  public MessageChannel outbound() {
    return MessageChannels.direct().get();
  }

  @Bean
  public OutboundEventMessageChannelAdapter axonToSpring() {
    return new OutboundEventMessageChannelAdapter(eventBus, outbound()) {
      @Override
      protected Message<?> transform(EventMessage<?> event) {
        Map<String, Object> headers = new HashMap<>(event.getMetaData());
        headers.put("axon-message-id", event.getIdentifier());
        headers.put("axon-message-type", event.getPayloadType().getName());
        headers.put("axon-message-revision", revisionResolver.revisionOf(event.getPayloadType()));
        headers.put("axon-message-timestamp", event.getTimestamp().toString());

        if (event instanceof DomainEventMessage) {
          headers.put("axon-message-aggregate-id", ((DomainEventMessage) event).getAggregateIdentifier());
          headers.put("axon-message-aggregate-seq", ((DomainEventMessage) event).getSequenceNumber());
          headers.put("axon-message-aggregate-type", ((DomainEventMessage) event).getType());
        }

        return new GenericMessage<>(event.getPayload(), headers);
      }
    };
  }
}
