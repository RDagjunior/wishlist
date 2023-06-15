package br.com.luizalabs.wishlist.configuration;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.logstash.logback.decorate.JsonFactoryDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig implements JsonFactoryDecorator {

  @Bean
  public ObjectMapper objectMapper() {
    var objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    objectMapper.setDefaultPropertyInclusion(Include.NON_EMPTY);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return objectMapper;
  }

  @Override
  public JsonFactory decorate(JsonFactory factory) {
    var objectMapper = (ObjectMapper) factory.getCodec();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return factory;
  }


}
