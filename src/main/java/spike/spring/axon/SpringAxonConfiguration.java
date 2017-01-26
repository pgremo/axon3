package spike.spring.axon;

import org.axonframework.commandhandling.disruptor.DisruptorCommandBus;
import org.axonframework.commandhandling.disruptor.DisruptorConfiguration;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventsourcing.GenericAggregateFactory;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.interceptors.CorrelationDataInterceptor;
import org.axonframework.monitoring.MessageMonitor;
import org.axonframework.spring.config.AxonConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spike.SpikeAggregateRoot;
import spike.axon.LoggingMessageMonitor;

import static java.util.Collections.singletonList;

@Configuration
public class SpringAxonConfiguration {
  private final AxonConfiguration axonConfiguration;
  private final TransactionManager transactionManager;

  private final EventStore eventStore;

  @Autowired
  public SpringAxonConfiguration(AxonConfiguration axonConfiguration, TransactionManager transactionManager, EventStore eventStore) {
    this.axonConfiguration = axonConfiguration;
    this.transactionManager = transactionManager;
    this.eventStore = eventStore;
  }

  @Bean
  MessageMonitor<Message<?>> messageMonitor() {
    return new LoggingMessageMonitor();
  }

  @Qualifier("localSegment")
  @Bean
  DisruptorCommandBus commandBus() {
    DisruptorConfiguration configuration = new DisruptorConfiguration()
      .setTransactionManager(transactionManager)
      .setInvokerInterceptors(singletonList(new CorrelationDataInterceptor<>(axonConfiguration.correlationDataProviders())))
      .setMessageMonitor(messageMonitor());
    return new DisruptorCommandBus(eventStore, configuration);
  }

  @Bean
  Repository<SpikeAggregateRoot> spikeAggregateRootRepository() {
    return commandBus().createRepository(new GenericAggregateFactory<>(SpikeAggregateRoot.class));
  }
}
