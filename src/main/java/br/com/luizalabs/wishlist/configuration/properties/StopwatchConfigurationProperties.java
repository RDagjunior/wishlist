package br.com.luizalabs.wishlist.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("luizalabs.stopwatch")
public class StopwatchConfigurationProperties {

  private boolean auto = false;

}
