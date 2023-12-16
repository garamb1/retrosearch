package it.garambo.retrosearch.news.scheduled;

import it.garambo.retrosearch.news.client.GNewsApiClient;
import it.garambo.retrosearch.news.model.Article;
import it.garambo.retrosearch.news.repository.NewsRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "retrosearch.news.enable", havingValue = "true")
public class NewsScheduledTask {

  @Autowired private GNewsApiClient apiClient;

  @Autowired private NewsRepository repository;

  @Scheduled(fixedRate = 60 * 60 * 1000)
  private void updateNews() {
    try {
      log.info("Updating article lists...");
      List<Article> articles = apiClient.fetchNews().articles();
      repository.updateAll(articles);
    } catch (Exception e) {
      log.error("Article list update failed:", e);
    }
  }
}
