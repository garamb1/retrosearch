package it.garambo.retrosearch.configuration;

import java.nio.charset.StandardCharsets;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

@Configuration
public class BeanConfig {

  @Bean
  public UrlValidator urlValidator() {
    String[] schemes = {"http", "https"};
    return new UrlValidator(schemes);
  }

  @Bean
  @Scope("prototype")
  public String logoPath() {
    int randomValue = (int) (Math.random() * 3) + 1;
    return "img/logos/logo-" + randomValue + ".gif";
  }

  @Bean
  public ThymeleafViewResolver thymeleafViewResolver(
      @Autowired SpringTemplateEngine templateEngine) {
    ThymeleafViewResolver resolver = new ThymeleafViewResolver();
    resolver.setTemplateEngine(templateEngine);
    resolver.setCharacterEncoding(StandardCharsets.ISO_8859_1.displayName());
    return resolver;
  }
}
