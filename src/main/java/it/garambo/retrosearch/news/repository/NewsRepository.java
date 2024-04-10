package it.garambo.retrosearch.news.repository;

import it.garambo.retrosearch.news.model.Article;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface NewsRepository {

  List<Article> getArticlesByCountry(String country);

  void updateAll(Map<String, List<Article>> newArticles);

  Date getUpdatedAt();

  boolean isCountrySupported(String country);
}
