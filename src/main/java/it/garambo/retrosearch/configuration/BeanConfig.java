package it.garambo.retrosearch.configuration;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

  @Bean
  public UrlValidator urlValidator() {
    return new UrlValidator();
  }
}
