package com.thoughtworks.spring.jms;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.ConnectionFactory;

@Configuration
@ConditionalOnClass({AxonJMSMessageProcessor.class, ConnectionFactory.class})
public class JMSProcessorConfiguration {

  private final EventBus eventBus;
  private final JmsTemplate template;

  @Autowired
  public JMSProcessorConfiguration(EventBus eventBus, JmsTemplate template) {
    this.eventBus = eventBus;
    this.template = template;
  }

  @ConditionalOnProperty("eventBus.out.destination")
  @ConfigurationProperties(prefix = "eventBus.out")
  @Bean(initMethod = "start", destroyMethod = "shutDown")
  public AxonJMSMessageProcessor jmsBridge() {
    return new AxonJMSMessageProcessor(eventBus, template);
  }
}
