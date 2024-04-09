package it.garambo.retrosearch.news.repository;

import it.garambo.retrosearch.news.model.Article;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "retrosearch.news.enable", havingValue = "true")
public class InMemoryNewsRepository implements NewsRepository {

  private Map<String, List<Article>> articles;
  private Date updatedAt;

  @Override
  public Map<String, List<Article>> getAllArticles() {
    return articles;
  }

  @Override
  public void updateAll(Map<String, List<Article>> newArticles) {
    articles = newArticles;
    updatedAt = new Date();
    log.info("Article list updated, news loaded for {}", articles.keySet());
  }

  @Override
  public Date getUpdatedAt() {
    return updatedAt;
  }
}
