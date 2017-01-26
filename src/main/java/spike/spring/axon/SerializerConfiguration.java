package spike.spring.axon;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.axonframework.serialization.AnnotationRevisionResolver;
import org.axonframework.serialization.RevisionResolver;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SerializerConfiguration {
  private ObjectMapper objectMapper;

  public SerializerConfiguration(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Bean
  RevisionResolver revisionResolver() {
    return new AnnotationRevisionResolver();
  }

  @Bean
  Serializer serializer() {
    return new JacksonSerializer(objectMapper, revisionResolver());
  }

}
