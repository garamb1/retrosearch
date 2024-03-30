package it.garambo.retrosearch.sports.football.repository;

import it.garambo.retrosearch.sports.football.model.match.Match;
import java.util.Date;
import java.util.Set;

public interface SportsRepository {

  Set<Match> getAllMatchesByArea(String areaName);

  void updateAll(Set<Match> newMatches);

  Date getUpdatedAt();
}
