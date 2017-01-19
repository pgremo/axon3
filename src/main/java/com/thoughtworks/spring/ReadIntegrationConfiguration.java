package com.thoughtworks.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.jms.support.SimpleJmsHeaderMapper;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessagingMessageConverter;

import javax.jms.ConnectionFactory;

@Configuration
public class ReadIntegrationConfiguration {
  private ConnectionFactory connectionFactory;
  private ObjectMapper objectMapper;

  public ReadIntegrationConfiguration(ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
    this.connectionFactory = connectionFactory;
    this.objectMapper = objectMapper;
  }

  @Bean(name = PollerMetadata.DEFAULT_POLLER)
  public PollerMetadata poller() {
    return Pollers.fixedRate(500).get();
  }

  @Bean
  public IntegrationFlow jmsInboundFlow(@Value("${eventBus.in.destination}") String destination) {
    return IntegrationFlows
      .from(Jms.inboundAdapter(connectionFactory)
        .configureJmsTemplate(t -> t.deliveryPersistent(true).jmsMessageConverter(messageConverter()))
        .destination(destination))
      .channel("event-in")
      .get();
  }

  @Bean
  public MessageConverter messageConverter() {
    MappingJackson2MessageConverter jsonConverter = new MappingJackson2MessageConverter();
    jsonConverter.setObjectMapper(objectMapper);
    jsonConverter.setTypeIdPropertyName("JMSType");
    return new MessagingMessageConverter(jsonConverter, new SimpleJmsHeaderMapper());
  }

}
