package it.garambo.retrosearch.sports.football.model.match;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Team(int id, String name, String shortName) {}
