package com.thoughtworks.spring.jms;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.GenericEventMessage;
import org.axonframework.eventsourcing.DomainEventMessage;
import org.axonframework.eventsourcing.GenericDomainEventMessage;
import org.axonframework.messaging.MetaData;
import org.axonframework.serialization.*;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.time.Instant;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Avishek Sen Gupta on 1/18/2017.
 */
public class AxonJMSMessageConverter implements MessageConverter {
    private Serializer serializer;

    public AxonJMSMessageConverter(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        EventMessage eventMessage = (EventMessage) object;
        SerializedObject<byte[]> serializedObject = serializer.serialize(eventMessage.getPayload(), byte[].class);
        BytesMessage result = session.createBytesMessage();
        result.writeBytes(serializedObject.getData());

        for (Map.Entry<String, Object> s : eventMessage.getMetaData().entrySet()) {
            result.setObjectProperty("axon-metatdata-" + s.getKey(), s.getValue());
        }

        result.setStringProperty("axon-message-id", eventMessage.getIdentifier());
        result.setStringProperty("axon-message-type", serializedObject.getType().getName());
        result.setStringProperty("axon-message-revision", serializedObject.getType().getRevision());
        result.setStringProperty("axon-message-timestamp", eventMessage.getTimestamp().toString());

        if (eventMessage instanceof DomainEventMessage) {
            result.setStringProperty("axon-message-aggregate-id", ((DomainEventMessage) eventMessage).getAggregateIdentifier());
            result.setLongProperty("axon-message-aggregate-seq", ((DomainEventMessage) eventMessage).getSequenceNumber());
            result.setStringProperty("axon-message-aggregate-type", ((DomainEventMessage) eventMessage).getType());
        }

        result.setJMSType(serializedObject.getType().getName());
        return result;
    }

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        HashMap<String, Object> headers = new HashMap<>();
        Enumeration propertyNames = message.getPropertyNames();
        while(propertyNames.hasMoreElements()) {
            String name = (String) propertyNames.nextElement();
            headers.put(name, message.getObjectProperty(name));
        }

        Map<String, Object> metaData = headers.entrySet().stream().filter(x -> x.getKey().startsWith("axon-metadata-"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        BytesMessage bytesMessage = (BytesMessage) message;
        byte[] data = new byte[(int) bytesMessage.getBodyLength()];
        bytesMessage.readBytes(data);

        SimpleSerializedObject<byte[]> serializedMessage = new SimpleSerializedObject<>(data, byte[].class,
                Objects.toString(headers.get("axon-message-type")),
                Objects.toString(headers.get("axon-message-revision"), null));

        SerializedMessage<Object> serialized = new SerializedMessage<>(Objects.toString(headers.get("axon-message-id")),
                new LazyDeserializingObject<Object>(serializedMessage, serializer),
                new LazyDeserializingObject<MetaData>(MetaData.from(metaData)));
        String timestamp = Objects.toString(headers.get("axon-message-timestamp"));
        if (headers.containsKey("axon-message-aggregate-id")) {
            return new GenericDomainEventMessage<>(
                    Objects.toString(headers.get("axon-message-aggregate-type")),
                    Objects.toString(headers.get("axon-message-aggregate-id")),
                    (Long) headers.get("axon-message-aggregate-seq"),
                    serialized,
                    () -> Instant.parse(timestamp));
        } else {
            return new GenericEventMessage<>(serialized, () -> Instant.parse(timestamp));
        }
    }
}
