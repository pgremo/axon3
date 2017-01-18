package com.thoughtworks.spring.jms;

import org.axonframework.common.Registration;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.messaging.SubscribableMessageSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;

import java.util.List;

/**
 * Created by Avishek Sen Gupta on 1/18/2017.
 */
@ConfigurationProperties(prefix = "eventBus.out")
public class AxonJMSMessageProcessor {
    private SubscribableMessageSource<EventMessage<?>> messageSource;
    private final JmsTemplate template;
    private String destination;
    private Registration eventBusRegistration;

    public AxonJMSMessageProcessor(SubscribableMessageSource<EventMessage<?>> messageSource,
                                   JmsTemplate template) {
        this.messageSource = messageSource;
        this.template = template;
    }

    private void setDestination(String destination) {
        this.destination = destination;
    }

    private void send(List<? extends EventMessage<?>> events) {
        MessageConverter converter = template.getMessageConverter();
        template.execute(session -> {
         session.createProducer(session.createTopic(destination));
            for (EventMessage<?> event: events) {
                producer.send(converter.toMessage(event, session));
            }
            return null;
        });
    }

    private void shutDown() {
        if (eventBusRegistration != null) eventBusRegistration.cancel();
    }

    private void start() {
        eventBusRegistration = messageSource.subscribe(this::send);
    }
}
