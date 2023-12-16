package it.garambo.retrosearch.news.model;

import java.util.Date;

public record Article(
    String title,
    String description,
    String content,
    String url,
    String image,
    Date publishedAt,
    ArticleSource source) {}
