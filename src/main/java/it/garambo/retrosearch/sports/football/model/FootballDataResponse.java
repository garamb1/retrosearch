package it.garambo.retrosearch.sports.football.model;

import it.garambo.retrosearch.sports.football.model.match.Match;
import java.util.List;
import java.util.Map;

public record FootballDataResponse(
    Map<String, String> filters, Map<String, String> resultSet, List<Match> matches) {}
