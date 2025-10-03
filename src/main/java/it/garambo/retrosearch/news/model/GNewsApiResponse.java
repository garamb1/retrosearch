package it.garambo.retrosearch.news.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GNewsApiResponse(int totalArticles, List<Article> articles) {}
