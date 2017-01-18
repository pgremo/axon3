package com.thoughtworks.jdbc;

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

/**
 * Created by Avishek Sen Gupta on 1/18/2017.
 */
@Configuration
public class JdbcConfiguration {

    @Autowired
    DataSource dataSource;

    @Autowired
    TransactionManager transactionManager;

    @Autowired
    Serializer serializer;

    @ConditionalOnMissingBean
    @Bean
    public EventStorageEngine eventStorageEngine() {
        return new JdbcEventStorageEngine(serializer, null, null, null,
                connectionProvider(), transactionManager, String.class,
                new EventSchema(), null, null);
    }

    @ConditionalOnMissingBean
    @Bean
    public ConnectionProvider connectionProvider() {
        return new SpringDataSourceConnectionProvider(dataSource);
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
