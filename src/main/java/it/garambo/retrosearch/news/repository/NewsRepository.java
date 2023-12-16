package it.garambo.retrosearch.news.repository;

import it.garambo.retrosearch.news.model.Article;
import java.util.Date;
import java.util.List;

public interface NewsRepository {

  List<Article> getAllArticles();

  Article getArticle(int index);

  void updateAll(List<Article> newArticles);

  Date getUpdatedAt();
}
