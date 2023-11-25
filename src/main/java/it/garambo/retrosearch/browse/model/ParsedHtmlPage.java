package it.garambo.retrosearch.browse.model;

import lombok.Builder;

@Builder
public record ParsedHtmlPage(String title, String htmlContent) {}
