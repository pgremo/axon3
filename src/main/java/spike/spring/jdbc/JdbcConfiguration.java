package spike.spring.jdbc;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.axonframework.common.jdbc.ConnectionProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.saga.repository.SagaStore;
import org.axonframework.eventhandling.saga.repository.jdbc.GenericSagaSqlSchema;
import org.axonframework.eventhandling.saga.repository.jdbc.JdbcSagaStore;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.jdbc.JdbcTokenStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.jdbc.EventSchema;
import org.axonframework.eventsourcing.eventstore.jdbc.JdbcEventStorageEngine;
import org.axonframework.serialization.Serializer;
import org.axonframework.spring.jdbc.SpringDataSourceConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdbcConfiguration {
  private final DataSource dataSource;
  private final TransactionManager transactionManager;
  private final Serializer serializer;

  @Autowired
  public JdbcConfiguration(DataSource dataSource, TransactionManager transactionManager, Serializer serializer) {
    this.dataSource = dataSource;
    this.transactionManager = transactionManager;
    this.serializer = serializer;
  }

  @ConditionalOnMissingBean
  @Bean
  public ConnectionProvider connectionProvider() {
    return new SpringDataSourceConnectionProvider(dataSource);
  }

  @ConditionalOnMissingBean
  @Bean
  public EventStorageEngine eventStorageEngine() {
    return new JdbcEventStorageEngine(serializer,
      null,
      null,
      null,
      connectionProvider(),
      transactionManager,
      String.class,
      EventSchema.builder()
        .withEventTable("DOMAIN_EVENT_ENTRY")
        .withEventIdentifierColumn("EVENT_IDENTIFIER")
        .withGlobalIndexColumn("GLOBAL_INDEX")
        .withMetaDataColumn("META_DATA")
        .withPayloadRevisionColumn("PAYLOAD_REVISION")
        .withPayloadTypeColumn("PAYLOAD_TYPE")
        .withTimestampColumn("TIME_STAMP")
        .withAggregateIdentifierColumn("AGGREGATE_IDENTIFIER")
        .withSequenceNumberColumn("SEQUENCE_NUMBER")
        .build(),
      null,
      null);
  }

  @ConditionalOnMissingBean
  @Bean
  public TokenStore tokenStore() {
    return new JdbcTokenStore(connectionProvider(), serializer);
  }

  @ConditionalOnMissingBean
  @Bean
  public SagaStore sagaStore() {
    return new JdbcSagaStore(connectionProvider(), new GenericSagaSqlSchema(), serializer);
  }

}
