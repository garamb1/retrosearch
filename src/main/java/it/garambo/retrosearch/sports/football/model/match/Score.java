package it.garambo.retrosearch.sports.football.model.match;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Score(HomeAwayScore halfTime, HomeAwayScore fullTime) {}
