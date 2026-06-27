package it.garambo.retrosearch.browse.model;

import java.net.URI;
import java.util.List;
import lombok.Builder;

@Builder
public record ParsedHtmlPage(
    String title, String htmlContent, List<String> navigation, URI redirectTo) {}
