package br.com.luizalabs.wishlist.configuration;

import br.com.luizalabs.wishlist.configuration.properties.ApiConfigurationProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ApiConfigurationProperties.class)
public class ApiDocumentationConfiguration {

  private final ApiConfigurationProperties apiProperties;


  @Bean
  public OpenAPI api() {
    return new OpenAPI()
        .info(
            new Info().title(apiProperties.getApiName()).description(apiProperties.getDescription())
                .version(apiProperties.getVersion())
                .contact(apiProperties.getContact()));
  }

}
