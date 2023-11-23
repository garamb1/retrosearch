package it.garambo.retrosearch.ddg.model;

import java.util.List;
import java.util.Locale;
import lombok.Builder;

@Builder
public record SearchResults(String query, Locale locale, List<ResultEntry> resultEntries) {}
