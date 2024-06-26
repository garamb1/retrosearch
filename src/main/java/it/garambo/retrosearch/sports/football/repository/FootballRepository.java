package it.garambo.retrosearch.sports.football.repository;

import it.garambo.retrosearch.sports.football.model.match.Match;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FootballRepository {

  Map<String, Set<Match>> getAllMatches();

  Set<Match> getAllMatchesByArea(String areaName);

  void updateAll(List<Match> newMatches);

  Date getUpdatedAt();
}
