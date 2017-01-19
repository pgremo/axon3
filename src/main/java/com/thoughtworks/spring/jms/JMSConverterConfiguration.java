package com.thoughtworks.spring.jms;

import org.axonframework.serialization.Serializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MessageConverter;

@Configuration
public class JMSConverterConfiguration {
  private final Serializer serializer;

  public JMSConverterConfiguration(Serializer serializer) {
    this.serializer = serializer;
  }

  @Bean
  public MessageConverter messageConverter() {
    return new AxonJMSMessageConverter(serializer);
  }


}
