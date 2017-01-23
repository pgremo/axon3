package spike.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.dsl.jms.JmsMessageDrivenChannelAdapter;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.jms.ConnectionFactory;

import static org.springframework.integration.dsl.support.Transformers.fromJson;

@Configuration
public class ReadIntegrationConfiguration {
  private ConnectionFactory connectionFactory;
  private ObjectMapper objectMapper;
  private PlatformTransactionManager transactionManager;

  @Autowired
  public ReadIntegrationConfiguration(ConnectionFactory connectionFactory, ObjectMapper objectMapper, PlatformTransactionManager transactionManager) {
    this.connectionFactory = connectionFactory;
    this.objectMapper = objectMapper;
    this.transactionManager = transactionManager;
  }

  @Bean
  public IntegrationFlow jmsInboundFlow(@Value("${eventBus.in.destination}") String destination) {
    JmsMessageDrivenChannelAdapter source = Jms.messageDrivenChannelAdapter(connectionFactory)
      .configureListenerContainer(c -> c.sessionTransacted(true))
      .destination(destination).get();
    return IntegrationFlows
      .from(source)
      .transform(fromJson(new Jackson2JsonObjectMapper(objectMapper)))
      .channel(eventIn())
      .get();
  }

  @Bean(name = "event-in")
  public PublishSubscribeChannel eventIn() {
    return MessageChannels
      .publishSubscribe("event-in")
      .interceptor(transactionChannelInterceptor())
      .get();
  }

  @Bean
  public TransactionChannelInterceptor transactionChannelInterceptor() {
    return new TransactionChannelInterceptor(transactionManager);
  }

}
