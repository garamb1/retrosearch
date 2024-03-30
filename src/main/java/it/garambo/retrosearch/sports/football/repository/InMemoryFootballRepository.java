package it.garambo.retrosearch.sports.football.repository;

import it.garambo.retrosearch.sports.football.model.match.Match;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "retrosearch.sports.football.enable", havingValue = "true")
public class InMemoryFootballRepository implements FootballRepository {

  private Map<String, Set<Match>> matchesByArea;
  private Date updatedAt;

  @Override
  public Map<String, Set<Match>> getAllMatches() {
    return matchesByArea;
  }

  @Override
  public Set<Match> getAllMatchesByArea(String areaName) {
    return matchesByArea.get(areaName);
  }

  @Override
  public void updateAll(List<Match> newMatches) {
    Map<String, Set<Match>> updatedMatches = new HashMap<>();
    newMatches.forEach(
        match -> {
          String areaName = match.area().name();
          updatedMatches.putIfAbsent(areaName, new HashSet<>());
          updatedMatches.get(areaName).add(match);
        });
    log.info("Football Results updated");
    matchesByArea = updatedMatches;
    updatedAt = new Date();
  }

  @Override
  public Date getUpdatedAt() {
    return updatedAt;
  }
}
