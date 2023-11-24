package it.garambo.retrosearch.ddg.model;

import java.util.List;
import lombok.Builder;

@Builder
public record SearchResults(String query, String locale, List<ResultEntry> resultEntries) {}
