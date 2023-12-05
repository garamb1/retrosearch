package it.garambo.retrosearch.configuration;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
  public ApplicationSettings applicationSettings(
      @Value("${retrosearch.html.version:2.0}") String htmlVersionValue,
      @Value("${retrosearch.encoding:UTF-8}") String encodingValue) {
    return new ApplicationSettings(HTMLVersion.getByVersionName(htmlVersionValue), encodingValue);
  }

  @Bean
  public ThymeleafViewResolver thymeleafViewResolver(
      @Autowired SpringTemplateEngine templateEngine,
      @Autowired ApplicationSettings applicationSettings) {
    ThymeleafViewResolver resolver = new ThymeleafViewResolver();
    resolver.setTemplateEngine(templateEngine);
    resolver.setCharacterEncoding(applicationSettings.getEncoding());
    return resolver;
  }
}
