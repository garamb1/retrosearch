package it.garambo.retrosearch.configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BandwidthBuilder;
import io.github.bucket4j.Bucket;
import it.garambo.retrosearch.browse.parser.ModernHtmlParser;
import it.garambo.retrosearch.browse.parser.ModernHtmlParserImpl;
import it.garambo.retrosearch.controller.RateLimitingFilter;
import it.garambo.retrosearch.ddg.client.DDGClient;
import it.garambo.retrosearch.ddg.client.DDGClientImpl;
import it.garambo.retrosearch.ddg.scraper.DDGScraper;
import it.garambo.retrosearch.http.HttpService;
import it.garambo.retrosearch.http.HttpServiceImpl;
import it.garambo.retrosearch.news.client.GNewsApiClient;
import it.garambo.retrosearch.sports.football.client.FootballDataOrgClient;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

  private CloseableHttpClient createCloseableHttpClient(
      int requestConnectionTimeout, int responseTimeout) {
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

    RequestConfig requestConfig =
        RequestConfig.custom()
            .setRedirectsEnabled(true)
            .setConnectionRequestTimeout(Timeout.ofMilliseconds(requestConnectionTimeout))
            .setResponseTimeout(Timeout.ofMilliseconds(responseTimeout))
            .build();

    return HttpClients.custom()
        .setConnectionManager(connectionManager)
        .setConnectionManagerShared(true)
        .setDefaultRequestConfig(requestConfig)
        .evictExpiredConnections()
        .disableAutomaticRetries()
        .evictIdleConnections(TimeValue.ofSeconds(2))
        .build();
  }

  private CloseableHttpClient createCloseableHttpClient() {
    final int defaultTimeout = 5000;
    return createCloseableHttpClient(defaultTimeout, defaultTimeout);
  }

  @Bean("generalHttpService")
  public HttpService generalHttpService(
      @Value("${retrosearch.httpclient.timeout.request.connection:10000}")
          int requestConnectionTimeout,
      @Value("${retrosearch.httpclient.timeout.response:10000}") int responseTimeout) {
    return new HttpServiceImpl(
        createCloseableHttpClient(requestConnectionTimeout, responseTimeout));
  }

  @Bean("modernHtmlParser")
  public ModernHtmlParser ModernHtmlParser(
      @Qualifier("generalHttpService") HttpService generalHttpService,
      ApplicationSettings applicationSettings) {
    return new ModernHtmlParserImpl(generalHttpService, applicationSettings);
  }

  @Bean("searchHttpService")
  public HttpService searchHttpService() {
    return new HttpServiceImpl(createCloseableHttpClient());
  }

  @Bean
  public DDGClient ddgClient(
      @Qualifier("searchHttpService") HttpService searchHttpService, DDGScraper ddgScraper) {
    return new DDGClientImpl(searchHttpService, ddgScraper);
  }

  @Bean("newsHttpService")
  public HttpService newsHttpService() {
    return new HttpServiceImpl(createCloseableHttpClient());
  }

  @Bean
  @ConditionalOnProperty(value = "retrosearch.news.enable", havingValue = "true")
  public GNewsApiClient gNewsApiClient(
      @Value("${retrosearch.news.api.url:https://gnews.io/api/v4/top-headlines}") String apiUrl,
      @Value("${retrosearch.news.api.key:}") String apiKey,
      @Qualifier("newsHttpService") HttpService newsHttpService) {
    return new GNewsApiClient(apiUrl, apiKey, newsHttpService);
  }

  @Bean("footballHttpService")
  public HttpService footballHttpService() {
    CloseableHttpClient closeableHttpClient = createCloseableHttpClient();
    return new HttpServiceImpl(closeableHttpClient);
  }

  @Bean
  @ConditionalOnProperty(value = "retrosearch.sports.football.enable", havingValue = "true")
  public FootballDataOrgClient footballDataOrgClient(
      @Qualifier("footballHttpService") HttpService footballHttpService,
      @Value("${retrosearch.sports.football.api.key:}") String apiKey,
      @Value("${retrosearch.sports.football.api.url:https://api.football-data.org/v4/matches}")
          String apiUrl) {
    return new FootballDataOrgClient(apiUrl, apiKey, footballHttpService);
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
