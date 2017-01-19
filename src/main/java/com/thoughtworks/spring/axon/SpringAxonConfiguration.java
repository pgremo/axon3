package com.thoughtworks.spring.axon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.LoggingMessageMonitor;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.interceptors.CorrelationDataInterceptor;
import org.axonframework.monitoring.MessageMonitor;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.spring.config.AxonConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAxonConfiguration {
  private final AxonConfiguration axonConfiguration;

  private final ObjectMapper objectMapper;

  private final TransactionManager transactionManager;

  public SpringAxonConfiguration(AxonConfiguration axonConfiguration, ObjectMapper objectMapper, TransactionManager transactionManager) {
    this.axonConfiguration = axonConfiguration;
    this.objectMapper = objectMapper;
    this.transactionManager = transactionManager;
  }

  @Bean
  Serializer serializer() {
    return new JacksonSerializer(objectMapper);
  }

  @Bean
  MessageMonitor<Message<?>> messageMonitor() {
    return new LoggingMessageMonitor();
  }

  @Qualifier("localSegment")
  @Bean
  public CommandBus commandBus() {
    SimpleCommandBus commandBus = new SimpleCommandBus(transactionManager, messageMonitor());
    commandBus.registerHandlerInterceptor(new CorrelationDataInterceptor<>(axonConfiguration.correlationDataProviders()));
    return commandBus;
  }

}
