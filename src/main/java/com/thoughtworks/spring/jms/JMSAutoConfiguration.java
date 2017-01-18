package com.thoughtworks.spring.jms;

import com.thoughtworks.spring.jms.AxonJMSMessageConverter;
import com.thoughtworks.spring.jms.AxonJMSMessageProcessor;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.serialization.Serializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.ConnectionFactory;

/**
 * Created by Avishek Sen Gupta on 1/18/2017.
 */
@Configuration
@ConditionalOnClass({AxonJMSMessageProcessor.class, ConnectionFactory.class})
public class JMSAutoConfiguration {

    private EventBus eventBus;
    private JmsTemplate template;

    public JMSAutoConfiguration(EventBus eventBus, JmsTemplate template) {
        this.eventBus = eventBus;
        this.template = template;
    }

    @ConditionalOnMissingBean
    @Bean
    public MessageConverter messageConverter(Serializer serializer) {
        return new AxonJMSMessageConverter(serializer);
    }

    @ConditionalOnProperty("eventBus.out.destination")
    @ConfigurationProperties(prefix = "eventBus.out")
    @Bean(initMethod = "start", destroyMethod = "shutDown")
    public AxonJMSMessageProcessor jmsBridge() {
        return new AxonJMSMessageProcessor(eventBus, template);
    }
}
