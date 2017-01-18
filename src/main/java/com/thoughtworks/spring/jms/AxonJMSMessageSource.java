package com.thoughtworks.spring.jms;

import org.axonframework.common.Registration;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.messaging.SubscribableMessageSource;
import org.springframework.jms.annotation.JmsListener;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class AxonJMSMessageSource implements SubscribableMessageSource<EventMessage<?>> {
    private final List<Consumer<List<? extends EventMessage<?>>>> eventProcessors = new CopyOnWriteArrayList<>();

    @Override
    public Registration subscribe(Consumer<List<? extends EventMessage<?>>> consumer) {
        eventProcessors.add(consumer);
        return () -> eventProcessors.remove(consumer);
    }

    @JmsListener(destination = "${eventBus.in.destination}")
    public void processMessage(EventMessage<?> message) {
        eventProcessors.forEach(listConsumer -> listConsumer.accept(Collections.singletonList(message)));
    }
}
