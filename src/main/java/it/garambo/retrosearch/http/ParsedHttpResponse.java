package it.garambo.retrosearch.http;

import java.net.URI;
import lombok.Builder;

@Builder
public record ParsedHttpResponse(URI originalUri, URI redirectionUri, String content) {}
