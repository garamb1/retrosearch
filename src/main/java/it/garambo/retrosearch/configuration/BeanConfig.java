package it.garambo.retrosearch.configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BandwidthBuilder;
import io.github.bucket4j.Bucket;
import it.garambo.retrosearch.controller.RateLimitingFilter;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StringUtils;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

@Slf4j
@Configuration
@EnableScheduling
public class BeanConfig {

  @Bean
  @ConditionalOnProperty(value = "retrosearch.ratelimiting.enable", havingValue = "true")
  public Bucket rateLimitingBucket(
      @Value("${retrosearch.ratelimiting.duration.seconds:1}") Integer secondsDuration,
      @Value("${retrosearch.ratelimiting.requests.count:5}") Integer numberOfRequests) {
    Bandwidth limit =
        BandwidthBuilder.builder()
            .capacity(numberOfRequests)
            .refillGreedy(numberOfRequests, Duration.of(secondsDuration, ChronoUnit.SECONDS))
            .build();
    return Bucket.builder().addLimit(limit).build();
  }

  @Bean
  @ConditionalOnProperty(value = "retrosearch.ratelimiting.enable", havingValue = "true")
  public FilterRegistrationBean<RateLimitingFilter> rateLimitingFilter(
      Bucket rateLimitingBucket,
      @Value("${retrosearch.ratelimiting.exclude.paths:img/*}") List<String> excludedPaths) {
    return new FilterRegistrationBean<>(new RateLimitingFilter(rateLimitingBucket, excludedPaths));
  }

  @Bean
  public HttpClient httpClient() {
    return HttpClientBuilder.create().build();
  }

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
  public SearchSettings searchSettings(
      @Value("${retrosearch.search.locales}") List<String> localeList) {
    List<Locale> locales = localeList.stream().map(StringUtils::parseLocaleString).toList();
    log.info("Initialising search locales: {}", locales);
    return new SearchSettings(locales);
  }

  @Bean
  public NewsSettings newsSettings(
      @Value("${retrosearch.news.enable}") boolean enabled,
      @Value("${retrosearch.news.api.locales}") List<String> localeList,
      @Value("${retrosearch.news.api.rate.limiter}") long rateLimiter) {
    List<Locale> locales = localeList.stream().map(StringUtils::parseLocaleString).toList();
    return new NewsSettings(enabled, rateLimiter, locales);
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
