package it.garambo.retrosearch.news.repository;

import it.garambo.retrosearch.news.model.Article;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface NewsRepository {

  Map<String, List<Article>> getAllArticles();

  void updateAll(Map<String, List<Article>> newArticles);

  Date getUpdatedAt();
}
