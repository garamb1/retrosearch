package it.garambo.retrosearch.ddg.model;

import java.net.URI;
import lombok.Builder;

@Builder
public record ResultEntry(String title, URI uri, String description) {}
