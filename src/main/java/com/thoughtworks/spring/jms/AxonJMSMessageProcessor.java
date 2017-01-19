package com.thoughtworks.spring.jms;

import org.axonframework.common.Registration;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.messaging.SubscribableMessageSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.MessageProducer;
import java.util.List;

public class AxonJMSMessageProcessor {
    private final SubscribableMessageSource<EventMessage<?>> messageSource;
    private final JmsTemplate template;
    private String destination;
    private Registration eventBusRegistration;

    public AxonJMSMessageProcessor(SubscribableMessageSource<EventMessage<?>> messageSource,
                                   JmsTemplate template) {
        this.messageSource = messageSource;
        this.template = template;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    private void send(List<? extends EventMessage<?>> events) {
        MessageConverter converter = template.getMessageConverter();
        template.execute(session -> {
            MessageProducer producer = session.createProducer(session.createTopic(destination));
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
