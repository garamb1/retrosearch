package it.garambo.retrosearch.news.model;

import java.util.List;

public record GNewsApiResponse(int totalArticles, List<Article> articles) {}
