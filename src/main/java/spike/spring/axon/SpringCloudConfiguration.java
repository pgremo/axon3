package spike.spring.axon;

import org.axonframework.boot.AxonAutoConfiguration;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.distributed.*;
import org.axonframework.serialization.Serializer;
import org.axonframework.springcloud.commandhandling.SpringCloudCommandRouter;
import org.axonframework.springcloud.commandhandling.SpringHttpCommandBusConnector;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@ConditionalOnClass(SpringHttpCommandBusConnector.class)
@ConditionalOnProperty("axon.distributed.spring.cloud.enabled")
@AutoConfigureAfter(AxonAutoConfiguration.JpaConfiguration.class)
@Configuration
public class SpringCloudConfiguration {
  private Serializer serializer;
  private DiscoveryClient discoveryClient;
  private CommandBus localCommandBus;

  public SpringCloudConfiguration(Serializer serializer, DiscoveryClient discoveryClient, @Qualifier("localSegment") CommandBus localCommandBus) {
    this.serializer = serializer;
    this.discoveryClient = discoveryClient;
    this.localCommandBus = localCommandBus;
  }

  @Bean
  public RestTemplate restTemplate(){
    return new RestTemplate();
  }

  @Bean
  public RoutingStrategy routingStrategy() {
    return new AnnotationRoutingStrategy();
  }

  @Bean
  public CommandRouter commandRouter() {
    return new SpringCloudCommandRouter(discoveryClient, routingStrategy());
  }

  @Bean
  public CommandBusConnector commandBusConnector() {
    return new SpringHttpCommandBusConnector(localCommandBus, restTemplate(), serializer);
  }

  @ConditionalOnMissingBean
  @Primary
  @Bean
  public DistributedCommandBus distributedCommandBus() {
    DistributedCommandBus commandBus = new DistributedCommandBus(commandRouter(), commandBusConnector());
    commandBus.updateLoadFactor(100);
    return commandBus;
  }
}
