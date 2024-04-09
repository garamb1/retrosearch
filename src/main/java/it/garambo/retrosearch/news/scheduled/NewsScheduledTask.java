package it.garambo.retrosearch.news.scheduled;

import it.garambo.retrosearch.configuration.NewsSettings;
import it.garambo.retrosearch.news.client.GNewsApiClient;
import it.garambo.retrosearch.news.model.Article;
import it.garambo.retrosearch.news.repository.NewsRepository;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "retrosearch.news.enable", havingValue = "true")
public class NewsScheduledTask {
  @Autowired private NewsSettings settings;

  @Autowired private GNewsApiClient apiClient;

  @Autowired private NewsRepository repository;

  @Scheduled(fixedRate = 60 * 60 * 1000)
  private void updateNews() throws Exception {
    List<Locale> locales = settings.getLocales();
    log.info("Updating article lists... Requested Locales: {}", locales);

    Map<String, List<Article>> newArticles = new HashMap<>();

    Executors.newScheduledThreadPool(1)
        .execute(
            () -> {
              for (Locale locale : locales) {
                try {
                  List<Article> articles =
                      apiClient.fetchNews(locale.getLanguage(), locale.getCountry()).articles();
                  newArticles.put(locale.getDisplayCountry(), articles);
                  log.info("Fetched news for {}", locale);
                  Thread.sleep(settings.getRateLimiter());
                } catch (IOException | URISyntaxException | InterruptedException e) {
                  log.error("Could not fetch news for {}", locale, e);
                }
              }

              if (newArticles.keySet().size() < locales.size()) {
                log.error(
                    "Could not retrieve news for all countries/locales. The current news map won't be updated.");
              } else {
                log.info("News fetching complete for requested locales: {}", locales);
                repository.updateAll(newArticles);
              }
            });
  }
}
