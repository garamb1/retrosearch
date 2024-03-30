package it.garambo.retrosearch.sports.football.model.match;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Competition(int id, String name, String code) {}
