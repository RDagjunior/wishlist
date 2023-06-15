package br.com.luizalabs.wishlist.configuration;

import br.com.luizalabs.wishlist.stopwatch.StopwatchAdvice;
import br.com.luizalabs.wishlist.stopwatch.StopwatchAutoAdvice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;

@Configuration
@EnableAspectJAutoProxy
public class StopwatchConfiguration {

  @Bean
  @Lazy
  @ConditionalOnProperty(name = "luizalabs.stopwatch.auto", havingValue = "true", matchIfMissing = false)
  public StopwatchAutoAdvice stopwatchAutoAdvice() {
    return new StopwatchAutoAdvice();
  }

  @Bean
  @Lazy
  @ConditionalOnProperty(name = "luizalabs.stopwatch.annotated", havingValue = "true", matchIfMissing = true)
  public StopwatchAdvice stopwatchAdvice() {
    return new StopwatchAdvice();
  }
}
